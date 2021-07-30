package com.qiuyu.demo.wechatAPI.util;


import com.qiuyu.demo.wechatAPI.service.WeChatService;
import com.qiuyu.demo.wechatAPI.vo.AccessToken;
import com.qiuyu.demo.wechatAPI.vo.AccessTokenModel;
import com.qiuyu.demo.wechatAPI.vo.JsApiTicket;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 公共工具类
 *
 * @CreateUser:QIUYU
 * @CreateTime：2019-10-12
 */
@Component
public class WxCommonUtil {

    public static final Logger log = LoggerFactory.getLogger(WxCommonUtil.class);

    private static String appId;

    private static String appSecret;
    private static WxCommonUtil wxCommonUtil;
    @Autowired
    private HttpRequestUtil httpRequestUtil;
    @Autowired
    private WeChatService weChatService;
    @Autowired
    private EntityManager entityManager;

    /**
     * 获取操作者ip地址，获取access_token使用
     *
     * @return
     * @CreateUser:QIUYU
     * @CreateTime：2019-10-12
     */
    public static String getLocalIp() {
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
            String localname = ia.getHostName();
            String localip = ia.getHostAddress();
            // log.info("本机名称是："+ localname);
            log.info("本机的ip是 ：" + localip);
            return localip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取操作者主机名称，获取access_token使用
     *
     * @return
     * @CreateUser:QIUYU
     * @CreateTime：2019-10-12
     */
    public static String getLocalName() {
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
            String localname = ia.getHostName();
            String localip = ia.getHostAddress();
            log.info("本机名称是：" + localname);
            return localname;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description: 获取微信调用高级接口的凭证（access_token），这个接口一天最多2000次调用
     * 请不要随便调用，请用存在数据库或redis里的AccessToken
     * @Parameters:
     * @Return:
     * @CreateDate: 2019年10月12日上午10:38
     * @Version: V1.00
     * @author: QiuYu
     */
    public static AccessToken getAccessToken(String appid, String appsecret) {
        //获取access_token接口
        String token_url = WxConstants.ACCESS_TOKEN_URL;
        //将公众号的appid和appsecret替换进url
        String url = token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        AccessToken accessToken = new AccessToken();
        //发起get请求获取凭证
        JSONObject jsonObject = HttpRequestUtil.httpsRequest(url, "GET", null);
        log.info("获取到的json格式的Token为:" + jsonObject);
        if (jsonObject != null) {
            try {
                accessToken.setAccess_token(jsonObject.getString("access_token"));
                accessToken.setExpires_in(jsonObject.getInt("expires_in"));
            } catch (Exception e) {
                accessToken = null;
                //获取token失败
                log.error("获取token失败 errcode:{} errmsg:{}",
                        jsonObject.getInt("errcode"),
                        jsonObject.getString("errmsg"));
            }
        }
        return accessToken;
    }

    /**
     * @Description: jsapi_ticket是公众号用于调用微信JS接口的临时票据。正常情况下，jsapi_ticket的有效期为7200秒，通过access_token来获取。由于获取jsapi_ticket的api调用次数非常有限，频繁刷新jsapi_ticket会导致api调用受限，影响自身业务.
     * 请不要随便调用，请用存在数据库或redis里的jsapi_ticket
     * @Parameters:
     * @Return:
     * @CreateDate: 2019年10月15日上午10:15
     * @Version: V1.00
     * @author: QiuYu
     */
    public static JsApiTicket getTicket(String accessToken) {
        if (accessToken.isEmpty()) {
            return null;
        }
        String jsapi_ticket = WxConstants.JSAPI_TICKET;
        String ticketUrl = jsapi_ticket.replace("ACCESS_TOKEN", accessToken);
        log.info("ticketUrl=" + ticketUrl);
        JsApiTicket jsApiTicket = new JsApiTicket();
        JSONObject jsonObject = HttpRequestUtil.httpsRequest(ticketUrl, "GET", null);
        log.info("获取到的json格式的Token为:" + jsonObject);
        if (jsonObject != null) {
            try {
                jsApiTicket.setErrcode(jsonObject.getString("errcode"));
                jsApiTicket.setErrmsg(jsonObject.getString("errmsg"));
                jsApiTicket.setTicket(jsonObject.getString("ticket"));
                jsApiTicket.setExpires_in(jsonObject.getInt("expires_in"));
            } catch (Exception e) {
                jsApiTicket = null;
                //获取token失败
                log.error("获取ticket失败 errcode:{} errmsg:{}",
                        jsonObject.getInt("errcode"),
                        jsonObject.getString("errmsg"));
            }
        }
        return jsApiTicket;
    }

    /**
     * 获取access_token
     *
     * @return
     */
    public static String getAccessToken() {
        // 先从redis里获取
        try {
            RedisUtil redisUtil = RedisUtil.getRedisUtil();
            if (redisUtil.exists("accessToken")) {
                log.info("从redis获取access_token成功!" + redisUtil.get("accessToken"));
                return redisUtil.get("accessToken");
            } else if (!redisUtil.exists("accessToken")) {
                // 防止redis出问题，从数据库里拿，都是同步刷新的
                List<AccessTokenModel> list = new ArrayList<>();
                try {
//                    StringBuffer sql = new StringBuffer();
//                    sql.append("select * from wx_access_token where 1=1");
//                    sql.append(" and app_id='" + appId + "'");
//                    sql.append(" order by id asc ");
//                    Query query = wxCommonUtil.entityManager.createNativeQuery(sql.toString(), AccessTokenModel.class);
//                    list = query.getResultList();
                } catch (Exception e) {
                    e.getStackTrace();
                }
                if (!list.isEmpty()) {
                    log.info("从数据库获取access_token成功！" + list.get(list.size() - 1).getAccessToken());
                    return list.get(list.size() - 1).getAccessToken();
                } else {
                    // 如果数据库也得不到，直接调用get请求获取，并录入redis和数据库
                    AccessToken accessToken = WxCommonUtil.getAccessToken(appId, appSecret);
                    JsApiTicket jsApiTicket = WxCommonUtil.getTicket(accessToken.getAccess_token());
                    // 装redis
                    log.info("插入redis中access_token,以及jsapi_ticket");
                    redisUtil.set("accessToken", accessToken.getAccess_token());
                    redisUtil.set("jsApiTicket", jsApiTicket.getTicket());
                    // 装数据库
                    wxCommonUtil.weChatService.refreshDBAccessToken(appId, accessToken, jsApiTicket);
                    log.info("从数据库获取access_token失败，执行get请求调用,并保存redis与数据库下次使用");
                    return accessToken.getAccess_token();
                }
            }
        } catch (JedisConnectionException e) {
            // redis 拒绝连接，则通过数据库获取
            e.printStackTrace();
            // 防止redis出问题，从数据库里拿，都是同步刷新的
            StringBuffer sql = new StringBuffer();
            sql.append("select * from wx_access_token where 1=1");
            sql.append(" and app_id='" + appId + "'");
            sql.append(" order by id asc ");
            Query query = wxCommonUtil.entityManager.createNativeQuery(sql.toString(), AccessTokenModel.class);
            List<AccessTokenModel> list = query.getResultList();
            if (!list.isEmpty()) {
                log.info("JedisConnectionException-->从数据库获取access_token成功！" + list.get(list.size() - 1).getAccessToken());
                return list.get(list.size() - 1).getAccessToken();
            } else {
                // 如果数据库也得不到，直接调用get请求获取，并录入redis和数据库
                AccessToken accessToken = WxCommonUtil.getAccessToken(appId, appSecret);
                JsApiTicket jsApiTicket = WxCommonUtil.getTicket(accessToken.getAccess_token());
                // 装数据库
                wxCommonUtil.weChatService.refreshDBAccessToken(appId, accessToken, jsApiTicket);
                log.info("JedisConnectionException-->从数据库获取access_token失败，执行get请求调用,并保存数据库下次使用");
                return accessToken.getAccess_token();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过数据库获取ticket,没有get调用，并保存下次使用
     *
     * @return
     */
    public static String getTicket() {
        // 先从redis里获取
        try {
            RedisUtil redisUtil = RedisUtil.getRedisUtil();
            if (redisUtil.exists("jsApiTicket")) {
                log.info("从redis获取jsApiTicket成功!" + redisUtil.get("jsApiTicket"));
                return redisUtil.get("jsApiTicket");
            }
            // 防止redis出问题，从数据库里拿，都是同步刷新的
            else if (!redisUtil.exists("jsApiTicket")) {
                StringBuffer sql = new StringBuffer();
                sql.append("select * from wx_access_token where 1=1");
                sql.append(" and app_id='" + appId + "'");
                sql.append(" order by id asc ");
                Query query = wxCommonUtil.entityManager.createNativeQuery(sql.toString(), AccessTokenModel.class);
                List<AccessTokenModel> list = query.getResultList();
                if (!list.isEmpty()) {
                    log.info("从数据库获取jsapi_ticket成功！" + list.get(list.size() - 1).getJsapiTicket());
                    return list.get(list.size() - 1).getJsapiTicket();
                } else {
                    // 如果数据库也得不到，直接调用get请求获取，并录入redis和数据库
                    AccessToken accessToken = WxCommonUtil.getAccessToken(appId, appSecret);
                    JsApiTicket jsApiTicket = WxCommonUtil.getTicket(accessToken.getAccess_token());
                    // 装redis
                    log.info("插入redis中access_token,以及jsapi_ticket");
                    redisUtil.set("accessToken", accessToken.getAccess_token());
                    redisUtil.set("jsApiTicket", jsApiTicket.getTicket());
                    // 装数据库
                    wxCommonUtil.weChatService.refreshDBAccessToken(appId, accessToken, jsApiTicket);
                    log.info("从数据库获取jsapi_ticket失败，执行get请求调用,并保存数据库下次使用");
                    return jsApiTicket.getTicket();
                }
            }
        } catch (JedisConnectionException e) {
            // redis 拒绝连接，则通过数据库获取
            e.printStackTrace();
            StringBuffer sql = new StringBuffer();
            sql.append("select * from wx_access_token where 1=1");
            sql.append(" and app_id='" + appId + "'");
            sql.append(" order by id asc ");
            Query query = wxCommonUtil.entityManager.createNativeQuery(sql.toString(), AccessTokenModel.class);
            List<AccessTokenModel> list = query.getResultList();
            if (!list.isEmpty()) {
                log.info("JedisConnectionException-->从数据库获取jsapi_ticket成功！" + list.get(list.size() - 1).getJsapiTicket());
                return list.get(list.size() - 1).getJsapiTicket();
            } else {
                // 如果数据库也得不到，直接调用get请求获取，并录入redis和数据库
                AccessToken accessToken = WxCommonUtil.getAccessToken(appId, appSecret);
                JsApiTicket jsApiTicket = WxCommonUtil.getTicket(accessToken.getAccess_token());
                // 装数据库
                wxCommonUtil.weChatService.refreshDBAccessToken(appId, accessToken, jsApiTicket);
                log.info("JedisConnectionException-->从数据库获取jsapi_ticket失败，执行get请求调用,并保存数据库下次使用");
                return jsApiTicket.getTicket();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Value("${wx.appId}")
    public void setAppId(String appId) {
        WxCommonUtil.appId = appId;
    }

    @Value("${wx.appSecret}")
    public void setAppSecret(String appSecret) {
        WxCommonUtil.appSecret = appSecret;
    }

    @PostConstruct
    public void init() {
        wxCommonUtil = this;
        wxCommonUtil.entityManager = this.entityManager;
        wxCommonUtil.httpRequestUtil = this.httpRequestUtil;
        wxCommonUtil.weChatService = this.weChatService;
    }


}
