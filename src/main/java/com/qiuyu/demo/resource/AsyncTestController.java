package com.qiuyu.demo.resource;

import com.qiuyu.demo.utils.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

@Slf4j
@RestController
@RequestMapping("/async")
public class AsyncTestController {

    @Resource
    private Executor demoTaskExecutor;

    @GetMapping("/testRequest")
    public void createSseConnect() throws InterruptedException {
        ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String username = ra.getRequest().getHeader("X-username");
        log.info("main thread 登录用户：{}", username);
        RequestContextHolder.setRequestAttributes(ra, true);
        demoTaskExecutor.execute(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("async thread 登录用户：{}", RequestContext.getUsername());
        });
        log.info("main thread end...");
    }

}