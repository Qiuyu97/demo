package com.qiuyu.demo.test;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @description
 * @author qy
 * @since 2023/2/24 17:08
 */
public class BeanUtilTest {

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.setId(1000L);
        demo.setCurrentTime(LocalDateTime.now());
        Map<String, Object> map = BeanUtil.beanToMap(demo, true, false);
        System.out.println(JSONUtil.toJsonStr(map));
    }

    @Data
    public static class Demo {
        private Long id;
        private String firstName;
        private LocalDateTime currentTime;

    }
}