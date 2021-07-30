package com.qiuyu.demo.wechatAPI.vo;

/**
 * 给前台返回的签名对象，前台使用JS-SDK的页面必须先注入配置信息（配置类）
 * @CreateUser:QY
 * @CreateTime:2019-10-15
 */
public class WxSignature {

    private String nonceStr;// 必填，生成签名的随机串

    private String timestamp;// 必填，生成签名的时间戳

    private String url;// 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致

    private String signature;// 必填，签名

    private String appId;// 必填，公众号的唯一标识

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
