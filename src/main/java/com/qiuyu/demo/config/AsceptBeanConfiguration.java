package com.qiuyu.demo.config;

import com.qiuyu.demo.aspect.ServiceRuntimeLogsAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Description:
 * @Author: qiuyu
 * @Date: 2021/3/19
 **/
@Configuration
public class AsceptBeanConfiguration {


    @Bean
    public ServiceRuntimeLogsAspect serviceRuntimeLogsAspect(){
        return new ServiceRuntimeLogsAspect();
    }


}
