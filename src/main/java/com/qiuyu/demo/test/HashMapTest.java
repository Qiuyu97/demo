package com.qiuyu.demo.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author qy
 * @since 2022/8/10 12:27
 */
public class HashMapTest {

    public static void main(String[] args) {

        Map<String, String> map = new HashMap<>(20);
        for (int i = 0; i < 20; i++) {
            map.put(String.valueOf(i),"value:"+i);
        }
    }
}