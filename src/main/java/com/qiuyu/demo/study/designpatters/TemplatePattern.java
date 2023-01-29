package com.qiuyu.demo.study.designpatters;

/**
 * @Description: 模板模式
 **/
public class TemplatePattern {

    abstract static class Template {

        abstract String getToken(String code);

        abstract Object getUserInfo(String token);

        void bindAccount(String code) {
            // 绑定账号
            saveAccount(getUserInfo(getToken(code)));
        }

        void saveAccount(Object userInfo) {
            System.out.println("绑定成功！"+userInfo.toString());
        }
    }

    static class WechatTemplatePattern extends Template {
        @Override
        String getToken(String code) {
            return "微信token";
        }

        @Override
        Object getUserInfo(String token) {
            return "微信用户";
        }
    }

    static class ToutiaoTemplatePattern extends Template {
        @Override
        String getToken(String code) {
            return "头条token";
        }

        @Override
        Object getUserInfo(String token) {
            return "头条用户";
        }
    }

    public static void main(String[] args) {
        Template templatePattern1 = new WechatTemplatePattern();
        templatePattern1.bindAccount("");

        Template templatePattern2 = new ToutiaoTemplatePattern();
        templatePattern2.bindAccount("");
    }

}
