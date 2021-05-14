package com.qiuyu.demo.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiuyu.demo.utils.redis.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@ServerEndpoint("/ws/shareBill/{orderId}/{userId}")
// @ServerEndpoint("/wsserver/{userId}")
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineTableCount = 0;

    private static ConcurrentHashMap<String, HashMap<String,WebSocketServer>> orderToUsersMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId="";

    private String orderId="";

    private static final String SHARE_BILL_KEY  = "SHARE_BILL_KEY_";
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("orderId") String orderId,@PathParam("userId") String userId) {
        this.session = session;
        this.userId=userId;
        this.orderId=orderId;
        // 获取拼单号是否存在
        HashMap<String,WebSocketServer> webSocketMap;
        if (orderToUsersMap.containsKey(orderId)){
            webSocketMap = orderToUsersMap.get(orderId);
            if (webSocketMap.containsKey(userId)){
                webSocketMap.remove(userId);
                webSocketMap.put(userId,this);
            } else {
                webSocketMap.put(userId,this);
            }
            orderToUsersMap.put(orderId,webSocketMap);
            // 将订单对应的JSON 发送给对应userId，可以从redis取出来
            try {
                sendMessage(getMessageFromRedisByOrder(orderId));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }else{
            webSocketMap = new HashMap<>();
            webSocketMap.put(userId,this);
            orderToUsersMap.put(orderId,webSocketMap);
            //加入set中
            addOnlineCount();
            //在线数加1
        }
        log.info("用户ID:{},加入当前订单{},该订单拼单人数{},总拼单数为:{}" ,userId,orderId,webSocketMap.size(),getOnlineCount());

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(orderToUsersMap.containsKey(orderId)){
            HashMap<String,WebSocketServer> webSocketMap = orderToUsersMap.get(orderId);
            if (webSocketMap.size()==0){
                // 无参与人，移除订单
                orderToUsersMap.remove(orderId);
                subOnlineCount();
                log.info("订单{}移除,总拼单数为:{}" ,orderId, getOnlineCount());
            }else {
                if (webSocketMap.containsKey(userId)){
                    webSocketMap.remove(userId);
                    if (webSocketMap.size()==0){
                        // 无参与人，移除订单
                        orderToUsersMap.remove(orderId);
                        subOnlineCount();
                        String key = SHARE_BILL_KEY+orderId;
                        RedisUtils.remove(key);
                        log.info("订单{}移除,总拼单数为:{}" ,orderId, getOnlineCount());
                    }
                    log.info("用户ID:{},退出当前订单{},该订单拼单人数{},总拼单数为:{}" ,userId,orderId,webSocketMap.size(),getOnlineCount());
                }
            }
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息ID:"+userId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                // 存储报文到redis
                sendMessageToRedis(orderId,message);
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                // 已选商品同步至其他点单人
                HashMap<String,WebSocketServer> webSocketMap = orderToUsersMap.get(orderId);
                for (Map.Entry entry : webSocketMap.entrySet()){
                    WebSocketServer webSocketServer = (WebSocketServer)entry.getValue();
                    webSocketServer.sendMessage(jsonObject.toJSONString());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误ID:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    private void sendMessageToRedis(String orderId,String message){
        String key = SHARE_BILL_KEY+orderId;
        if (RedisUtils.exists(key)){
            RedisUtils.remove(key);
            RedisUtils.set(key,message,60*15L);
        }
        RedisUtils.set(key,message,60*15L);
    }


    private String getMessageFromRedisByOrder(String orderId){
        String key = SHARE_BILL_KEY+orderId;
        if (RedisUtils.exists(key)){
            return RedisUtils.get(key).toString();
        }
        return "";
    }

    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("orderId") String orderId,@PathParam("userId") String userId) throws IOException {
        log.info("发送消息到ID:"+userId+"，报文:"+message);
        if(StringUtils.isNotBlank(orderId) && orderToUsersMap.get(orderId).containsKey(userId)){
            orderToUsersMap.get(orderId).get(userId).sendMessage(message);
        }else{
            log.error("用户ID"+userId+",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineTableCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineTableCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineTableCount--;
    }
}
