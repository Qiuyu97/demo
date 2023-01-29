package com.qiuyu.demo.study.designpatters.observe.springImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class SmsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @TransactionalEventListener
    public void onApplicationEvent(UserRegisterEvent userRegisterEvent) {
        String sql = String.format("select * from  sys_user where username = '%s';", userRegisterEvent.getUsername());
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        log.info("用户注册成功:{}。发送短信消息，username：{}", map, userRegisterEvent.getUsername());
    }

}