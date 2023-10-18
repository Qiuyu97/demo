package com.qiuyu.demo.test;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

import java.util.function.Function;

/**
 * @description
 * @author qy
 * @since 2023/4/2 11:01
 */
public class RangeMapTest {

    static RangeMap<Integer, Function<Boolean, String>> rangeMap = TreeRangeMap.create();

    static {
        rangeMap.put(Range.closed(100, 100), isChineseMode -> isChineseMode ? "非常优秀" : "very good");
        rangeMap.put(Range.closedOpen(90, 100), isChineseMode -> isChineseMode ? "优秀" : "good");
        rangeMap.put(Range.closedOpen(60, 90), isChineseMode -> isChineseMode ? "合格" : "pass");
        rangeMap.put(Range.closedOpen(0, 60), isChineseMode -> isChineseMode ? "不及格" : "no pass");
    }


    public static void main(String[] args) {
        System.out.println(getPower(100,true));
        System.out.println(getPower(90,false));
        System.out.println(getPower(97,true));
        System.out.println(getPower(68,false));
        System.out.println(getPower(32,true));
    }

    public static String getPower(int level, boolean isChineseMode) {
        return rangeMap.get(level).apply(isChineseMode);
    }
}