package com.qiuyu.demo.test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: User
 * @Author: qiuyu
 * @Date: 2022/5/24
 **/

public class UserTest {


    static class User{
        private long userId;
        private String username;

        public String getUsername(){
            return username;
        }
        public long getUserId(){
            return userId;
        }

        public User(long userId,String username){
            this.userId = userId;
            this.username = username;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof User)) return false;
            User user = (User) o;
            return userId == user.userId;
        }

        @Override
        public int hashCode() {
            return Long.hashCode(userId);
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        HashMap<User,String> map = new HashMap<>();
//        User user1 = new User(1001L,"张三");
//        User user2 = new User(1002L,"李四");
//        User user3 = new User(1003L,"王五");
//        User user4 = new User(1002L,"44");
//        map.put(user1,"张三");map.put(user2,"李四");map.put(user3,"王五");map.put(user4,"44");
//        Set<Map.Entry<User, String>> entries = map.entrySet();
//        for (Map.Entry<User, String> entry : entries) {
//            System.out.println(entry.getKey().getUserId() + ":" + entry.getValue());
//        }

//        Map<String, Integer> map = new HashMap<String,Integer>();
//        map.put("张三",2);
//        map.put("张a",4);
//        map.put("李四",1);
//        map.put("赵五",3);
       //使用多线程实现一个批处理过程，将以下数组，按10个数据一组，每组1个线程打印数据，并在10个线程都处理完成后输出总打印次数
        int[] data =new int[100];
        for (int i =0; i <100; i++) {
            data[i]= i;
        }
        singlePrint(data);
        concurrentPrint(data,10);
    }

    public static void singlePrint(int[] data) throws InterruptedException {
        //使用多线程实现一个批处理过程，将以下数组，按10个数据一组，每组1个线程打印数据，并在10个线程都处理完成后输出总打印次数
        long time = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < data.length; i++) {
//            System.out.println(data[i]);
            count++;
        }
        System.out.println("耗时:"+(System.currentTimeMillis()-time)+"ms。总打印次数："+ count);
    }

    public static void concurrentPrint(int[] data,int threadCount) throws InterruptedException {
     //使用多线程实现一个批处理过程，将以下数组，按10个数据一组，每组1个线程打印数据，并在10个线程都处理完成后输出总打印次数
        int batch =  data.length/threadCount;
        long time = System.currentTimeMillis();
        AtomicInteger atomicInteger = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            int index = i;
            Thread thread = new Thread(() -> {
                for (int j = batch * index; j < batch * index + batch; j++) {
                    if (j == data.length) break;
//                    System.out.println(data[j]);
                    atomicInteger.getAndIncrement();
                }
            });
            thread.start();
            thread.join();
        }
        System.out.println("耗时:"+(System.currentTimeMillis()-time)+"ms。总打印次数："+ atomicInteger.get());
    }

}
