package com.qiuyu.demo.test;

import java.util.HashMap;
import java.util.Map;

/**
 * @description
 * @author qy
 * @since 2022/8/10 12:27
 */
public class HashMapTest {

    public static void main(String[] args) {

        Map<String, String> map = new HashMap<>(32);
        for (int i = 1; i < 25; i++) {
            map.put(String.valueOf(i),"value:"+i);
        }
    }
}