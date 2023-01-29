package com.qiuyu.demo.study.designpatters.observe.springImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Async
    @TransactionalEventListener
    public void onApplicationEvent(UserRegisterEvent userRegisterEvent) {
        String sql = String.format("select * from  sys_user where username = '%s';", userRegisterEvent.getUsername());
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        log.info("用户注册成功:{}。发送邮箱消息，username：{}", map, userRegisterEvent.getUsername());
    }

}