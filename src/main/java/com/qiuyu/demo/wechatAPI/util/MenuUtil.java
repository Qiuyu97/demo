package com.qiuyu.demo.wechatAPI.util;

import com.qiuyu.demo.wechatAPI.vo.Button;
import com.qiuyu.demo.wechatAPI.vo.ClickButton;
import com.qiuyu.demo.wechatAPI.vo.Menu;
import com.qiuyu.demo.wechatAPI.vo.ViewButton;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 类名称: MemuUtil
 * 类描述: 菜单工具
 * @CreateUser: QIUYU
 * @CreateTime:2019-10-11
 */
public class MenuUtil {
    public static  final Logger log = LoggerFactory.getLogger(MenuUtil.class);
    /**
     * 创建菜单
     * @param accessToken
     * @param Menu 菜单json格式字符串
     * @return
     */
    public static int createMenu(String accessToken,String Menu){
        int result = Integer.MIN_VALUE;
        String url = WxConstants.CTRATE_MENU_URL.replaceAll("ACCESS_TOKEN", accessToken);
        //发起POST请求创建菜单
        JSONObject json = HttpRequestUtil.httpsRequest(url, "POST", Menu);
        if(json!=null){
            //从返回的数据包中取数据{"errcode":0,"errmsg":"ok"}
            result = json.getInt("errcode");
        }
        return result;
    }


    public static String initMenu(){
        String result = "";
        //创建点击一级菜单
        ClickButton button11 = new ClickButton();
        button11.setName("发送位置");
        button11.setKey("11");
        button11.setType("location_select");

        //创建跳转型一级菜单
        ViewButton button21 = new ViewButton();
        button21.setName("百度一下");
        button21.setType("view");
        button21.setUrl("https://www.baidu.com");

        //创建其他类型的菜单与click型用法一致
        ClickButton button31 = new ClickButton();
        button31.setName("拍照发图");
        button31.setType("pic_photo_or_album");
        button31.setKey("31");

        /*ClickButton button32 = new ClickButton();
        button32.setName("发送位置");
        button32.setKey("32");
        button32.setType("location_select");*/

        ViewButton button32 = new ViewButton();
        button32.setName("天目云采编-生产");
        button32.setType("view");
        button32.setUrl("https://i.tmuyun.com");

        ViewButton button33 = new ViewButton();
        button33.setName("天目云采编-预发布");
        button33.setType("view");
        button33.setUrl("https://csi.tmuyun.com");

        ViewButton button34 = new ViewButton();
        button34.setName("coding");
        button34.setType("view");
        button34.setUrl("https://tmyteam.coding.net/");


        //封装到一级菜单
        Button button = new Button();
        button.setName("tmy");
        button.setType("click");
        button.setSub_button(new Button[]{button31,button32,button33,button34});

        //封装菜单
        Menu menu = new Menu();
        menu.setButton(new Button[]{button11,button21,button});
        return JSONObject.fromObject(menu).toString();
    }

    /**
     *
     * @Description: 删除菜单
     * @Parameters: accessToken
     * @Return:  boolean
     * @Create  2019-10-11
     * @Version:
     * @author: QIUYU
     */
    public static boolean deleteMenu(String accessToken){
        boolean result = false;
        String requestUrl = WxConstants.DELETE_MENU_URL.replace("ACCESS_TOKEN", accessToken);
        //发起GET请求删除菜单
        JSONObject jsonObject = HttpRequestUtil.httpsRequest(requestUrl, "GET", null);

        if (null != jsonObject) {
            int errorCode = jsonObject.getInt("errcode");
            String errorMsg = jsonObject.getString("errmsg");
            if (0== errorCode) {
                result = true;
            } else {
                result = false;
                log.error("删除菜单失败 errcode：{} errmsg：{} ",errorCode,errorMsg);
            }
        }
        return result;
    }



}
