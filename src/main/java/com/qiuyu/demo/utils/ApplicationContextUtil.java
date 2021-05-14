package com.qiuyu.demo.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Date: Create in 11:45 2020/11/3
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBeanByName(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBeanByClass(Class clazz) {
        return (T) applicationContext.getBean(clazz);
    }
}
