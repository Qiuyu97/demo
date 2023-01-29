package com.qiuyu.demo.study.designpatters;

/**
* @Description: 单例模式
**/
public class SingletionPattern {

    private static volatile SingletionPattern instance;

    private SingletionPattern() {
    }

    // 双检锁
    public SingletionPattern getInstance() {
        if (instance == null) {
            synchronized (SingletionPattern.class) {
                if (instance == null) {
                    instance = new SingletionPattern();
                }
            }
        }
        return instance;
    }
}
