package com.qiuyu.demo.service.impl;

import com.qiuyu.demo.annotations.ServiceRuntimeLogs;
import com.qiuyu.demo.service.OpenApiService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: qiuyu
 * @Date: 2021/3/19
 **/
@Service
public class OpenApiServiceImpl implements OpenApiService {

    @Override
    @ServiceRuntimeLogs
    public String testAop() {
        /*ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10000; i++) {
            final int index = i;
            executor.execute(()-> {
               System.out.println(Thread.currentThread().getName()+" Hello"+index);
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) { // Wait until all threads are finish,and also you can use "executor.awaitTermination();" to wait
        }*/

        return "say Helloword";
    }

    

}
