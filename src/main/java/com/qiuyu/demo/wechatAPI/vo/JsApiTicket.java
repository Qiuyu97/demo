package com.qiuyu.demo.wechatAPI.vo;
/**
 * jsapi_ticket调用的接收对象
 * @CreateUser:QY
 * @CreateTime:2019-10-15
 */
public class JsApiTicket {

    private String errcode;

    private String errmsg;

    private String ticket;

    private Integer expires_in;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }
}
