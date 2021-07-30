package com.qiuyu.demo.wechatAPI.controller;


import com.qiuyu.demo.wechatAPI.util.*;
import com.qiuyu.demo.wechatAPI.vo.WxSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;


@RestController
@RequestMapping(value = "/api")
public class WxResource {

    private final Logger log = LoggerFactory.getLogger(WxResource.class);

    @Value("${wx.appId}")
    private String appId;

    /**
     * 验证微信消息
     *
     * @param request
     * @return
     */
    @GetMapping(value ="/wx/check")
    public String checkWxMsg(HttpServletRequest request) {
        /**
         * 微信加密签名
         */
        String signature = request.getParameter("signature");
        /**
         * 随机字符串
         */
        String echostr = request.getParameter("echostr");
        /**
         * 时间戳
         */
        String timestamp = request.getParameter("timestamp");
        /**
         * 随机数
         */
        String nonce = request.getParameter("nonce");

        log.info("signature is :"+signature+"timestamp is"+timestamp+"nonce is :"+nonce);

        String[] str={timestamp,nonce, WxConstants.TOKEN};
        //将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(str);
        StringBuffer sb = new StringBuffer();
        //将三个参数字符串拼接成一个字符串进行sha1加密
        for (String param:str) {
            sb.append(param);
        }
        //获取到加密过后的字符串
        String encryptStr = EncryptionUtil.encrypt("SHA1", sb.toString());
        //判断加密后的字符串是否与微信的签名一致
        log.info("执行微信签名加密认证后的字符串encryptStr is :"+encryptStr);
        if(signature.equalsIgnoreCase(encryptStr)){
            return echostr;
        }
        log.error("这不是微信发来的消息！！");
        return null;
    }

    // JS-SDK使用权限签名算法
    // 负责jsapi_ticket是公众号用于调用微信JS接口的临时票据
    @PostMapping(value ="wx/getSign")
    public WxSignature getSign(@RequestBody WxSignature wxSign) {
        String url = wxSign.getUrl();
        // 校验一下前台是否传来url
        if (url.isEmpty()){
            return null;
        }
        String jsapiTicket = WxCommonUtil.getTicket();
        String nonceStr = UUIDGenerator.getUUID();
        String timestamp = String.valueOf(getSecondTimestamp(new Date()));
        StringBuffer sb = new StringBuffer();
        //步骤1. 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1：
        sb.append("jsapi_ticket=").append(jsapiTicket).append("&noncestr=").append(nonceStr)
            .append("&timestamp=").append(timestamp).append("&url=").append(url);
        System.out.println(sb.toString());
        //步骤2. 对string1进行sha1签名，得到signature：
        String signature = EncryptionUtil.encrypt("SHA1", sb.toString());
        System.out.println("---------->appId is : "+appId);
        System.out.println("---------->timestamp is : "+timestamp);
        System.out.println("---------->nonceStr is : "+nonceStr);
        System.out.println("---------->signature is : "+signature);
        System.out.println("---------->url is : "+url);
        WxSignature wxSignature = new WxSignature();
        wxSignature.setNonceStr(nonceStr);
        wxSignature.setTimestamp(timestamp);
        wxSignature.setUrl(url);
        wxSignature.setSignature(signature);
        wxSignature.setAppId(appId);
        return wxSignature;
    }


    @GetMapping(value ="wx/test")
    public String test(HttpServletRequest request) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("此时的accessToken: ");
        stringBuffer.append(WxCommonUtil.getAccessToken());
        stringBuffer.append("此时的ticket: ");
        stringBuffer.append(WxCommonUtil.getTicket());
        return stringBuffer.toString();
    }

    /**
     * 创建微信公众号菜单
     */
    @GetMapping("/menu/create")
    public String createMenu(){
        String accessToken =  WxCommonUtil.getAccessToken();
        String menu = MenuUtil.initMenu();
        System.out.println(menu);
        int result = MenuUtil.createMenu(accessToken,menu);
        if(result==0){
            System.out.println("菜单创建成功!");
            return "菜单创建成功！";
        }else{
            System.out.println("错误码:"+result);
            return "菜单创建失败！错误码:"+result;
        }
    }

    /**
     * 删除微信公众号菜单
     */
    @GetMapping("/menu/delete")
    public String deleteMenu(){
        String accessToken = WxCommonUtil.getAccessToken();
        boolean result = MenuUtil.deleteMenu(accessToken);
        // AccessToken accessToken  = WxCommonUtil.getAccessToken(appId,appSecret);
        // boolean result = MenuUtil.deleteMenu(accessToken.getAccess_token());
        if(result){
            System.out.println("删除成功！");
            return "删除成功！";
        }
        else {
            System.out.println("删除失败！");
            return "删除失败！";
        }
    }




    private int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0, length - 3));
        } else {
            return 0;
        }
    }



}
