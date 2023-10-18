package com.qiuyu.demo.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiuyu.demo.utils.DesUtil;
import org.springframework.util.DigestUtils;

/**
 * @description
 * @author qy
 * @since 2023/4/18 13:47
 */
public class SignUtilService {

    //浙江日报
    private static String signAppSecret="a719da3d09c6dd26";

    /**
     *
     * @param millisTime 13位时间戳
     * @param body 稿件json body
     * @return
     */
    public static String generateSign(String millisTime, JSONObject body){
        try{
            return generateSign(millisTime,body.toJSONString());
        }catch (Exception e){
            return null;
        }
    }

    public static  String generateSign(String millisTime, String content){
        try{
            String encryptString = millisTime+"_"+content;
            String desEncryptString= DesUtil.encrypt(signAppSecret, encryptString);
            String sign = DigestUtils.md5DigestAsHex(desEncryptString.getBytes());

            return sign;
        }catch (Exception e){
            return null;
        }
    }

    /**
     *
     * @param millisTime 13位时间戳
     * @param body 稿件json body
     * @param sign 检查的加签
     * @return
     */
    public static boolean checkSign(String millisTime,JSONObject body,String sign){
        try{
            return generateSign(millisTime,body.toJSONString()).equals(sign);
        }catch (Exception e){
            return false;
        }
    }

    public static boolean checkSign(String millisTime,String content,String sign){
        try{
            return generateSign(millisTime,content).equals(sign);
        }catch (Exception e){
            return false;
        }
    }

    public static void main(String[] args) {
        String json = "{\n" +
                "    \"proposalSystem\": \"卫视网\",\n" +
                "    \"publishDate\": 1680138412380,\n" +
                "    \"shortTitle\": \"\",\n" +
                "    \"source\": \"\",\n" +
                "    \"title\": \"倪超的测试视频-test\",\n" +
                "    \"type\": \"1\",\n" +
                "    \"creatDate\": 1680138405000,\n" +
                "    \"content\": \"<p style=\\\"position: relative;font-family: 方正标雅宋;font-size: 17px;line-height: 1.5em;color: rgb(0, 0, 0);text-align: center !important;\\\"><video crossorigin=\\\"anonymous\\\" contentid=\\\"5fa96eecf89f44b7ac6b00ec4fc15b48\\\" title=\\\"login\\\" src=\\\"http://zhejiangweishitv.zrtg.com:19207/gjyshive/wsw/bucket-z/u-r596073b46jea8f0/video/2021/11/19/5fa96eecf89f44b7ac6b00ec4fc15b48/login.mp4\\\" poster=\\\"http://zhejiangweishitv.zrtg.com:19207/gjyshive/wsw/bucket-k/u-h365hz3wjo80w8mo/2021/11/19/809bfd69f346456ca57a526f32f19cc7_videogroup_320_180_0.jpg\\\" duration=\\\"41666666\\\" clipsource=\\\"0\\\" screendata=\\\"\\\" data-detials=\\\"{&quot;title&quot;:&quot;login&quot;,&quot;format&quot;:&quot;MP4&quot;,&quot;time&quot;:&quot;00:00:04:05&quot;,&quot;size&quot;:&quot;0.46M&quot;,&quot;keyframe&quot;:&quot;http://zhejiangweishitv.zrtg.com:19207/gjyshive/wsw/bucket-k/u-h365hz3wjo80w8mo/2021/11/19/809bfd69f346456ca57a526f32f19cc7_videogroup_320_180_0.jpg&quot;}\\\" data-mediatext=\\\"\\\" data-videolist=\\\"\\\" data-name=\\\"login\\\" preload=\\\"no\\\" controls=\\\"\\\" controlslist=\\\"nodownload\\\" id=\\\"5fa96eecf89f44b7ac6b00ec4fc15b48\\\"></video></p>\",\n" +
                "    \"catalogId\": 485,\n" +
                "    \"subTitle\": \"\",\n" +
                "    \"videoUrl\": \"\",\n" +
                "    \"logo\": \"http://zhejiangweishitv.zrtg.com:19207/gjyshive/wsw/bucket-k/u-h365hz3wjo80w8mo/2021/11/19/809bfd69f346456ca57a526f32f19cc7_videogroup_320_180_0.jpg\",\n" +
                "    \"articleIdGuid\": \"wsw_00001\",\n" +
                "    \"keyword\": \"资料\",\n" +
                "    \"channelCode\": \"bluemedia\",\n" +
                "    \"summary\": \"\",\n" +
                "    \"images\": [],\n" +
                "    \"author\": \"章丽兰\",\n" +
                "    \"articleId\": 25815,\n" +
                "    \"handle\": \"add\",\n" +
                "    \"checkdatassize\": [],\n" +
                "    \"proposalSystemCode\": \"wsw\",\n" +
                "    \"isPublish\": \"1\",\n" +
                "    \"catalogName\": \"测试分组\",\n" +
                "    \"extend\": {\n" +
                "        \"proposalSystem\": \"卫视网\",\n" +
                "        \"lastPushPerson_itemcodes\": \"f075a2bdf36246f8b277f52d9bda3221\",\n" +
                "        \"targetId\": \"95c04bcd98f847fe9f822f69966c66db\",\n" +
                "        \"releaseSource\": \"\",\n" +
                "        \"realmName\": \"http://zhejiangweishitv.zrtg.com:19207\",\n" +
                "        \"filelogo_itemnames\": \"无台标\",\n" +
                "        \"proposalSystemCode\": \"wsw\",\n" +
                "        \"token\": \"93b8b4d0e5688dcec6547e8ec68fdaa063b3e058d472aab4b191ebf8442b3dc1\",\n" +
                "        \"word_count\": \"\",\n" +
                "        \"releaseSiteCode\": \"wsw\",\n" +
                "        \"filelogo\": \"0\",\n" +
                "        \"broadcastColumn_itemnames\": \"中国蓝新闻\",\n" +
                "        \"autoProposal\": \"0\",\n" +
                "        \"releaseReporter\": \"倪超\",\n" +
                "        \"isSubmitCensorship_itemnames\": \"否\",\n" +
                "        \"autoProposal_itemnames\": \"是\",\n" +
                "        \"isSubmitCensorship\": \"false\",\n" +
                "        \"centerContentId\": \"8d02df97cca1474c9435f1f026b09f8a\",\n" +
                "        \"lastPushPerson_jsonStr\": \"[{\\\"executor\\\":\\\"章丽兰\\\",\\\"executorCode\\\":\\\"f075a2bdf36246f8b277f52d9bda3221\\\"}]\",\n" +
                "        \"broadcastColumn\": \"ws_zglxw\",\n" +
                "        \"cnBlueAppAllianceSiteId\": 184,\n" +
                "        \"cnBlueAppAllianceReleaseChannelIds\": [\n" +
                "            3526,\n" +
                "            3527\n" +
                "        ]\n" +
                "    },\n" +
                "    \"isTop\": \"0\",\n" +
                "    \"orderFlag\": 1680138404774,\n" +
                "    \"files\": [\n" +
                "        {\n" +
                "            \"filename\": \"login.mp4\",\n" +
                "            \"type\": \"2\",\n" +
                "            \"fileid\": \"5fa96eecf89f44b7ac6b00ec4fc15b48\",\n" +
                "            \"fileframe\": \"16:9\",\n" +
                "            \"fileurl\": \"http://zhejiangweishitv.zrtg.com:19207/gjyshive/wsw/bucket-z/u-r596073b46jea8f0/video/2021/11/19/5fa96eecf89f44b7ac6b00ec4fc15b48/login.mp4\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"filename\": \"809bfd69f346456ca57a526f32f19cc7_videogroup_320_180_0.jpg\",\n" +
                "            \"type\": \"1\",\n" +
                "            \"fileid\": \"\",\n" +
                "            \"fileurl\": \"http://zhejiangweishitv.zrtg.com:19207/gjyshive/wsw/bucket-k/u-h365hz3wjo80w8mo/2021/11/19/809bfd69f346456ca57a526f32f19cc7_videogroup_320_180_0.jpg\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"filename\": \"809bfd69f346456ca57a526f32f19cc7_videogroup_320_180_0.jpg\",\n" +
                "            \"type\": \"1\",\n" +
                "            \"fileid\": \"5fa96eecf89f44b7ac6b00ec4fc15b48\",\n" +
                "            \"fileurl\": \"http://zhejiangweishitv.zrtg.com:19207/gjyshive/wsw/bucket-k/u-h365hz3wjo80w8mo/2021/11/19/809bfd69f346456ca57a526f32f19cc7_videogroup_320_180_0.jpg\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"channelName\": \"中国蓝新闻APP\",\n" +
                "    \"account\": \"\",\n" +
                "    \"username\": \"zll058\"\n" +
                "}";

        JSONObject jsonObject = JSON.parseObject(json);
        System.out.println(generateSign("1681438487291", jsonObject));
    }

}