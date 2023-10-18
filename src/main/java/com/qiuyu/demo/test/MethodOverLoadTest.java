package com.qiuyu.demo.test;

/**
 * 方法重载的测试
 * @author qy
 * @since 2023/2/20 13:47
 */
public class MethodOverLoadTest {

    public String getAge(Son son) {
        return "11";
    }

    public String getAge(Father father) {
        return "32";
    }

    public String getAge(GrandPa grandPa) {
        return "60";
    }

    public static abstract class Man {

    }

    public static class GrandPa extends Man {

    }

    public static class Father extends GrandPa {

    }

    public static class Son extends Father {

    }

    public static void main(String[] args) {
        MethodOverLoadTest test = new MethodOverLoadTest();
        Man son = new Son();
        Man father = new Father();
        Man grandPa = new GrandPa();
//        test.getAge(son);
//        test.getAge(father);
//        test.getAge(grandPa);
    }
}