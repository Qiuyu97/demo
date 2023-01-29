package com.qiuyu.demo.study.designpatters.observe.springImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements ApplicationEventPublisherAware {

    private final JdbcTemplate jdbcTemplate;

    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional(rollbackFor = Exception.class)
    public void register(String username) {
        // ... 执行注册逻辑
        log.info("[register][执行用户({}) 的注册逻辑]", username);
        String sql = String.format("insert into sys_user(username) values ('%s');", username);
        jdbcTemplate.execute(sql);
        // 注册成功通知
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this, username));
    }


}