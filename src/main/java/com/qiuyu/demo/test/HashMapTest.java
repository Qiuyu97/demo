package com.qiuyu.demo.test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description
 * @author qy
 * @since 2022/8/10 12:27
 */
public class HashMapTest {

    public static void main(String[] args) {

//        Map<String, String> map = new HashMap<>(32);
//        for (int i = 1; i < 25; i++) {
//            map.put(String.valueOf(i),"value:"+i);
//        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("111","111");
        map.put("222", 222);
        map.put("333", null);
        System.out.println(map);
        System.out.println(map.getOrDefault("333", "").toString());
    }
}