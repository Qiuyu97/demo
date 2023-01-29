package com.qiuyu.demo.study.designpatters;

import java.util.Observable;
import java.util.Observer;

/**
 *  观察者模式（基于java API）
 *  https://blog.csdn.net/sun_shaoping/article/details/84067446
 *
 * @author qy
 * @since 2022/7/20 9:44
 */
public class ObserverPattern {


    public static class SmsObserver implements Observer {

        private final Observable observable;

        public SmsObserver(Observable observable) {
            this.observable = observable;
            this.observable.addObserver(this);
        }

        @Override
        public void update(Observable o, Object arg) {
            System.out.println("短信提示: " + "===>" + arg.toString());
        }
    }

    public static class EmailObserver implements Observer {

        private final Observable observable;

        public EmailObserver(Observable observable) {
            this.observable = observable;
            this.observable.addObserver(this);
        }

        @Override
        public void update(Observable o, Object arg) {
            System.out.println("邮箱提示: " + "===>" + arg.toString());
        }
    }

    public static class MyObservable extends Observable {
        public MyObservable() {
            setChanged();
        }
    }

    public static void main(String[] args) {
        Observable observable = new MyObservable();
        new SmsObserver(observable);
        new EmailObserver(observable);
        observable.notifyObservers("欢迎订购XXX服务");
    }

}