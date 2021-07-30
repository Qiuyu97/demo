package com.qiuyu.demo.wechatAPI.service;


import com.qiuyu.demo.wechatAPI.util.RedisUtil;
import com.qiuyu.demo.wechatAPI.util.WxCommonUtil;
import com.qiuyu.demo.wechatAPI.vo.AccessToken;
import com.qiuyu.demo.wechatAPI.vo.JsApiTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


@Service
@Transactional
public class WeChatService {
    private final Logger log = LoggerFactory.getLogger(WeChatService.class);

    @Value("${spring.redis.host}")
    private  String host;

    @Value("${spring.redis.port}")
    private  Integer port;

    @Value("${spring.redis.password}")
    private  String password;

    @Autowired
    private EntityManager entityManager;


    // 目前通过数据库保存access_token
    // 先查询表wx_access_token里是否存在access_token，如果没有，则插入access_token
    // 如果有，则更新
    public int refreshDBAccessToken(String appId, AccessToken accessToken, JsApiTicket jsApiTicket){
           /*StringBuffer sql=new StringBuffer();
           sql.append("select * from wx_access_token where 1=1");
          // sql.append(" and local_ip='"+localIp+"'");
          // sql.append(" and local_name='"+localName+"'");
           sql.append(" and app_id='"+appId+"'");
           sql.append(" order by id asc ");
           Query query=entityManager.createNativeQuery(sql.toString(), AccessTokenModel.class);
           List<AccessTokenModel> list=query.getResultList();
           if(list.isEmpty()){
               StringBuffer insertSql=new StringBuffer();
               // insertSql.append(" insert into wx_access_token(local_ip,local_name,access_token,expires_in,app_id) values ");
               insertSql.append(" insert into wx_access_token(access_token,expires_in,app_id,jsapi_ticket) values ");
               insertSql.append("(");
              // insertSql.append("'"+localIp+"',");
              // insertSql.append("'"+localName+"',");
               insertSql.append("'"+accessToken.getAccess_token()+"',");
               insertSql.append(accessToken.getExpires_in()+",");
               insertSql.append("'"+appId+"',");
               insertSql.append("'"+jsApiTicket.getTicket()+"'");
               insertSql.append(")");
               Query query1=entityManager.createNativeQuery(insertSql.toString());
               int num = query1.executeUpdate();
               log.info("插入数据库access_token,以及jsapi_ticket");
               return num;
           }
           else {
               StringBuffer updateSql=new StringBuffer();
               updateSql.append(" update wx_access_token set access_token=");
               updateSql.append("'"+accessToken.getAccess_token()+"',");
               //updateSql.append("where local_ip='"+localIp+"'");
               // updateSql.append("and local_name='"+localName+"'");
              // updateSql.append(" and app_id='"+appId+"'");
               updateSql.append("jsapi_ticket='"+jsApiTicket.getTicket()+"' ");
               updateSql.append(" where app_id='"+appId+"'");
               Query query1=entityManager.createNativeQuery(updateSql.toString());
               int num = query1.executeUpdate();
               log.info("更新数据库access_token,以及jsapi_ticket");
               return num;
           }*/
        return 0;
   }

     public boolean refreshAccessTokenAndJsapiTicket(String appId,String appSecret){
         try {
             // 请求微信接口获取新的AccessToken与Ticket
             AccessToken accessToken  = WxCommonUtil.getAccessToken(appId,appSecret);
             JsApiTicket jsApiTicket = WxCommonUtil.getTicket(accessToken.getAccess_token());
             // 刷新数据库，这一步是为了发布生产后，直接使用数据库查看AccessToken与Ticket
             refreshDBAccessToken(appId,accessToken,jsApiTicket);
             // 刷新redis
             RedisUtil redisUtil = RedisUtil.getRedisUtil();
             if (redisUtil.exists("accessToken")){
                 redisUtil.del("accessToken");
                 redisUtil.set("accessToken",accessToken.getAccess_token());
             }
             else {
                 redisUtil.set("accessToken",accessToken.getAccess_token());
             }
             if (redisUtil.exists("jsApiTicket")){
                 redisUtil.del("jsApiTicket");
                 redisUtil.set("jsApiTicket",jsApiTicket.getTicket());
             }
             else {
                 redisUtil.set("jsApiTicket",jsApiTicket.getTicket());
             }
             log.info("此刻redis中accessToken:"+redisUtil.get("accessToken"));
             log.info("此刻redis中jsApiTicket:"+redisUtil.get("jsApiTicket"));
             return true;
         }
         catch (Exception e){
             e.printStackTrace();
             return false;
         }
     }





}
