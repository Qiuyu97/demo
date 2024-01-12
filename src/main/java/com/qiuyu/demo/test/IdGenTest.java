package com.qiuyu.demo.test;

public class IdGenTest {

    public static void main(String[] args) {
        Long userId = 511L;
        Long doc_id = 25305764L;
        System.out.println((userId << 32));
        System.out.println(Math.abs((userId << 32) ^ (doc_id)));
    }
}
