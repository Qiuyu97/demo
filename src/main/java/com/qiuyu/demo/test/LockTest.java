package com.qiuyu.demo.test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: 赵超
 * @Copyright: Copyright (c) 浙江日报
 * @Date: 2022-08-02 15:58
 * @Version: 1.0
 */
public class LockTest extends Thread {

    public static MyReentrantLock lock = new MyReentrantLock();

    public static Integer size = 0;

    public static void incr() {

       lock.lock();
        size++;
        lock.unlock();

    }


    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                incr();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int j = 0; j < 100000; j++) {
                incr();
            }
        });

        thread1.start();
        ;
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(size);


    }


}
