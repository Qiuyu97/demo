package com.qiuyu.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 获取yml的配置
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfiguration {
    private final Logger log = LoggerFactory.getLogger(RedisConfiguration.class);

    private static String host;

    private static int port;

    private static String password;

    public static String getHost() {
        return host;
    }

    public void setHost(String host) {
        RedisConfiguration.host = host;
    }

    public static int getPort() {
        return port;
    }

    public void setPort(int port) {
        RedisConfiguration.port = port;
    }

    public static String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        RedisConfiguration.password = password;
    }
}
