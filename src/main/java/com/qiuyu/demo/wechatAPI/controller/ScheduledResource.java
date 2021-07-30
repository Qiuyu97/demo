package com.qiuyu.demo.wechatAPI.controller;

import com.qiuyu.demo.wechatAPI.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: ScheduledResource
 * @Author: qiuyu
 * @Date: 2021/7/30
 **/
@RestController
@RequestMapping("/api")
@Async
@Component
@Slf4j
public class ScheduledResource {

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.appSecret}")
    private String appSecret;
    @Autowired
    private WeChatService weChatService;


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日-HH时mm分ss秒");


    /**
     * @Description:定时刷新redis中的access_token和jsapi_ticket。该定时任务涉及到刷新次数限制，请勿随便打开。
     * @CreateUser:QiuYu
     */
    @Scheduled(fixedRate= 1000*60*90, initialDelay = 2000)//项目启动2秒中之后执行一次，然后每90min执行一次，单位都为ms
    public void refreshRedisAccessToken(){
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        log.info("-------->开始刷新Redis,开始时间: " + formatter.format(date));
        boolean flag = weChatService.refreshAccessTokenAndJsapiTicket(appId, appSecret);
        if (flag) {
            log.info("--------->AccessToken和Ticket刷新成功！");
        } else {
            log.info("--------->AccessToken和Ticket刷新失败！");
        }

    }
}
