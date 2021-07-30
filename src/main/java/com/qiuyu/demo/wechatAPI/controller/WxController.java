package com.qiuyu.demo.wechatAPI.controller;

import com.qiuyu.demo.wechatAPI.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * 消息回复测试controller
 */
@RestController
@RequestMapping(value = "/api")
public class WxController {

    private final Logger log = LoggerFactory.getLogger(WxController.class);

    @Autowired
    private MessageService messageService;


    @PostMapping(value ="/wx/check")
    public void post(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        }
        response.setContentType("text/html;charset=UTF-8");

        // 调用核心业务类接收消息、处理消息
        String respMessage = messageService.newMessageRequest(request);

        // 响应消息
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(respMessage);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        } finally {
            out.close();
            out = null;
        }
    }

}
