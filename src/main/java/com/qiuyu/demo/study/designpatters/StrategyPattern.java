package com.qiuyu.demo.study.designpatters;

/**
 * @Description: 策略模式
 **/
public class StrategyPattern {

    interface Strategy{
        String publishArticle();
    }

    static class WechatStrategyPattern implements Strategy{
        @Override
        public String publishArticle() {
            return "发布微信";
        }
    }

    static class ToutiaoStrategyPattern implements Strategy{
        @Override
        public String publishArticle() {
            return "发布头条";
        }
    }

    static class DouyinStrategyPattern implements Strategy{
        @Override
        public String publishArticle() {
            return "发布抖音";
        }
    }

    static class Context{
        private final Strategy strategyPattern;

        public Context(Strategy strategyPattern){
            this.strategyPattern = strategyPattern;
        }

        public String publishArticle(){
            return strategyPattern.publishArticle();
        }
    }

    public static void main(String[] args) {
        Context context = new Context(new WechatStrategyPattern());
        System.out.println(context.publishArticle());

        context = new Context(new ToutiaoStrategyPattern());
        System.out.println(context.publishArticle());

        context = new Context(new DouyinStrategyPattern());
        System.out.println(context.publishArticle());
    }

}
