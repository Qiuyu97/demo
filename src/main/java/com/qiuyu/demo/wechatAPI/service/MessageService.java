package com.qiuyu.demo.wechatAPI.service;


import com.qiuyu.demo.wechatAPI.util.MessageUtil;
import com.qiuyu.demo.wechatAPI.util.TulingApiUtil;
import com.qiuyu.demo.wechatAPI.util.WxConstants;
import com.qiuyu.demo.wechatAPI.vo.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Service
public class MessageService {

    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    public String newMessageRequest(HttpServletRequest request) {
        String respMessage = null;
        try {
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.xmlToMap(request);
            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            // 消息内容
            String content = requestMap.get("Content");
            log.info("FromUserName is:" + fromUserName + ", ToUserName is:" + toUserName + ", MsgType is:" + msgType);
            // 文本消息
            if (msgType.equals(WxConstants.REQ_MESSAGE_TYPE_TEXT)) {
                //这里根据关键字执行相应的逻辑
                if(content.toLowerCase().equals("sb")){
                    TextMessage text = new TextMessage();
                    text.setContent(content);
                    text.setToUserName(fromUserName);
                    text.setFromUserName(toUserName);
                    text.setCreateTime(new Date().getTime());
                    text.setMsgType(msgType);
                    respMessage = MessageUtil.textMessageToXml(text);
                }else {
                    //自动回复(图灵机器人回复)
                    TextMessage text = new TextMessage();
                    String returnContent = TulingApiUtil.getTulingResult(content);
                    text.setContent(returnContent);
                    text.setToUserName(fromUserName);
                    text.setFromUserName(toUserName);
                    text.setCreateTime(new Date().getTime());
                    text.setMsgType(msgType);
                    respMessage = MessageUtil.textMessageToXml(text);
                }
            }
            // 事件推送
            else if (msgType.equals(WxConstants.REQ_MESSAGE_TYPE_EVENT)) {
                String eventType = requestMap.get("Event");// 事件类型
                // 订阅
                if (eventType.equals(WxConstants.EVENT_TYPE_SUBSCRIBE)) {
                    //文本消息
                    TextMessage text = new TextMessage();
                    text.setContent("欢迎关注newcomp的测试公众号！本公众号目前只支持自动回复文本消息，悉知~~~");
                    text.setToUserName(fromUserName);
                    text.setFromUserName(toUserName);
                    text.setCreateTime(new Date().getTime());
                    text.setMsgType(WxConstants.RESP_MESSAGE_TYPE_TEXT);
                    respMessage = MessageUtil.textMessageToXml(text);
                }
                // 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                else if (eventType.equals(WxConstants.EVENT_TYPE_UNSUBSCRIBE)) {// 取消订阅

                }
            }
        }
        catch (Exception e) {
            log.error("error......");
        }
        return respMessage;
    }

}
