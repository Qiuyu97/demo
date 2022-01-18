package com.qiuyu.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;


@Slf4j
// @Configuration
@ConfigurationProperties(prefix = "snowflow")
public class SnowFlakeProperties {
    private Map<String, String> ids;

    @PostConstruct
    public void init() {
        log.info("snowflowIds: " + ids);
    }

    public Map<String, String> getIds() {
        return ids;
    }

    public void setIds(Map<String, String> ids) {
        this.ids = ids;
    }
}
