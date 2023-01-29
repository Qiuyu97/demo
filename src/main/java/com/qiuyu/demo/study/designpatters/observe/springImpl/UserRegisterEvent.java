package com.qiuyu.demo.study.designpatters.observe.springImpl;


import org.springframework.context.ApplicationEvent;


public class UserRegisterEvent extends ApplicationEvent {

    /**
     * 用户名
     */
    private String username;

    public UserRegisterEvent(Object source, String username) {
        super(source);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}