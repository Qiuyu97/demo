package com.qiuyu.demo.test;


public class AsyncVariableVolatileTest {

    public static volatile boolean flag0 = false;
    public static volatile boolean flag = false;

//    public static boolean flag = false;



    public static void main(String[] args) throws InterruptedException {
        Runnable1 runnable1 = new Runnable1();
        Runnable2 runnable2 = new Runnable2();
        new Thread(runnable1).start();
        Thread.sleep(100L);
        new Thread(runnable2).start();
    }

     static class Runnable1 implements Runnable{
        @Override
        public void run() {
            try {
                System.out.println("Runnable1 start...");
                flag = true;
                System.out.println("Runnable1 flag = " + flag );
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            finally {
                flag = false;
                System.out.println("Runnable1 flag = " + flag );
            }
        }
    }

     static class Runnable2 implements Runnable{

        @Override
        public void run() {
            System.out.println("Runnable2 start...");
            while (flag){

            }
            System.out.println("Runnable2 监听到flag改变");
        }
    }
}