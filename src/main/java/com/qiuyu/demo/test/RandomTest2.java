package com.qiuyu.demo.test;

import java.util.Random;

/**
 * @description
 * @author qy
 * @since 2022/8/2 12:43
 */
public class RandomTest2 {

    public static void main(String[] args) {
        qiu();yu();
    }


    private static void qiu(){
        Random ran;
        int k;
        char c;
        for (int i = Integer.MIN_VALUE; true; i++) {
            ran = new Random(i);
            k = ran.nextInt(27);
            c = (char) ('`' + k);
            if (c == 'q') {
                k = ran.nextInt(27);
                c = (char) ('`' + k);
                if (c == 'i') {
                    k = ran.nextInt(27);
                    c = (char) ('`' + k);
                    if (c == 'u') {
                        k = ran.nextInt(27);
                        if (k == 0){
                            System.out.println(i);
                            break;
                        }
                    }
                }
            }
        }
    }


    private static void yu(){
        Random ran;
        int k;
        char c;
        for (int i = Integer.MIN_VALUE; true; i++) {
            ran = new Random(i);
            k = ran.nextInt(27);
            c = (char) ('`' + k);
            if (c == 'y') {
                k = ran.nextInt(27);
                c = (char) ('`' + k);
                if (c == 'u') {
                    k = ran.nextInt(27);
                    if (k == 0) {
                        System.out.println(i);
                        break;
                    }
                }
            }
        }
    }

}