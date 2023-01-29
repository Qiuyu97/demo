package com.qiuyu.demo.test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description
 * @author qy
 * @since 2022/9/9 10:17
 */
public class SafepointTest {

    public static final AtomicInteger num = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            for (int i = 0; i < 1000000000; i++) {
                num.getAndAdd(1);
            }
            System.out.println(Thread.currentThread().getName() + "执行结束!");
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        Thread.yield();
        Thread.sleep(900);
        System.out.println("num = " + num);
    }

}