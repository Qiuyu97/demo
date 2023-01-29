package com.qiuyu.demo.utils;

import lombok.Getter;

@Getter
public enum ResultEnum {

    /**
     * 请求成功
     */
    OK(200, "Success"),
    /**
     * 请求失败
     */
    BAD_REQUEST(400, "Bad Request"),
    /**
     * 未找到
     */
    NOT_FOUND(404,"Not Found"),
    /**
     * 无权限访问
     */
    PERM(403, "Unauthorized Access"),
    /**
     * token不存在
     */
    TOKEN_NOT_EXIST(411, "Token Not Exist"),
    /**
     * token已过期
     */
    TOKEN_HAS_EXPIRED(413, "Token Has Expired"),
    /**
     * 版本控制
     */
    VERSION_CONTROL(414, "Version Control"),
    /**
     * 其他异常
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    /**
     * session失效
     */
    NO_SESSION(800, "No Session"),
    /**
     * 连接异常（除请求超时）
     */
    CLIENT_EXCEPTION(998, "Client Exception "),
    /**
     * 请求超时
     */
    TIMEOUT(999, "Timeout");

    private final int status;
    private final String message;

    ResultEnum(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
