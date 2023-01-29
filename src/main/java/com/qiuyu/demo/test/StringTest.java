package com.qiuyu.demo.test;

/**
 * @Description: StringaTest
 * @Author: qiuyu
 * @Date: 2022/5/24
 **/
public class StringTest {

    public static String dosomething1() {
        String str = "";
        for (int i = 0; i < 1000; i++) {
            str += i;
        }
        return str;
    }

    public static String dosomething2() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append(i);
        }
        return sb.toString();
    }

    public static String dosomething3(){
        StringBuffer sb =new StringBuffer();
        for(int i=0; i< 1000;i++){
            sb.append(i);
        }
        return sb.toString();
    }

//    public static void main(String[] args) {
////        System.out.println(dosomething1());
////        System.out.println(dosomething2());
////        System.out.println(dosomething3());
//       final List<String> strings = Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");
//        strings.parallelStream().forEach(node -> {
//            System.out.println(node);
////            strings.parallelStream().forEach(node2 -> {
////                System.out.println(node+node2);
////            });
//        });
//    }



}
