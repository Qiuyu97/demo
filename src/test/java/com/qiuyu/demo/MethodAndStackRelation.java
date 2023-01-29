package com.qiuyu.demo;


public class MethodAndStackRelation {

    public static void main(String[] args) {
        A();
    }

    public static int A(){
        int x = 2;
        int y = 3;
        int z = (x+y)*4;
        return z;
    }

}
