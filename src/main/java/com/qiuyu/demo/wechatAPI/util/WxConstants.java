package com.qiuyu.demo.wechatAPI.util;

/**
 * @createUser:QY
 * @createTime:2019-10-11
 *
 **/
public class WxConstants {

    //这里写测试号的appid,后期放在配置文件中
    public static final String APP_ID="wxf28aa5957b9e1bd4";
    //这里写测试号的appsecret，后期放在配置文件中
    public static final String APP_SECRET="701f38406190b45756ab20bc608a5145";
    //这里写你自己定义的token
    public static final String TOKEN="WxTest";
    // 获取access_token,需要修改appid和appsecret，每日最大调用2000次，有效2hours
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    // 获取jsapi_ticket，是公众号用于调用微信JS接口的临时票据
    public static final String JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    // 创建菜单url
    public static final String CTRATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    // 删除菜单url
    public static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    // 消息回复等
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";
    public static final Object REQ_MESSAGE_TYPE_TEXT = "text";
    public static final Object REQ_MESSAGE_TYPE_IMAGE = "image";
    public static final Object REQ_MESSAGE_TYPE_VOICE = "voice";
    public static final Object REQ_MESSAGE_TYPE_VIDEO = "video";
    public static final Object REQ_MESSAGE_TYPE_LOCATION = "location";
    public static final Object REQ_MESSAGE_TYPE_LINK = "link";
    public static final Object REQ_MESSAGE_TYPE_EVENT = "event";
    public static final Object EVENT_TYPE_SUBSCRIBE = "subscribe";
    public static final Object EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
    public static final Object EVENT_TYPE_SCAN = "SCAN";
    public static final Object EVENT_TYPE_LOCATION = "LOCATION";
    public static final Object EVENT_TYPE_CLICK = "CLICK";
    public static final String FromUserName = "FromUserName";
    public static final String ToUserName = "ToUserName";
    public static final String MsgType = "MsgType";
    public static final String Content = "Content";
    public static final String Event = "Event";


}
