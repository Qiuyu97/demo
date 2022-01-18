package com.qiuyu.demo.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: BaseConfiguration
 * @Author: qiuyu
 * @Date: 2022/1/18
 **/
@Configuration
@EnableConfigurationProperties(SnowFlakeProperties.class)
public class BaseConfiguration {
}
