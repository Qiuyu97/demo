package com.qiuyu.demo.resource;

import com.qiuyu.demo.annotations.ServiceRuntimeLogs;
import com.qiuyu.demo.service.OpenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: qiuyu
 * @Date: 2021/3/8
 **/
@RestController
@RequestMapping("/open")
public class OpenApiResource {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private OpenApiService openApiService;


    @GetMapping("/test/demo/{str}")
    public String productQuote(@PathVariable String str) {

        return openApiService.testAop();
    }


}
