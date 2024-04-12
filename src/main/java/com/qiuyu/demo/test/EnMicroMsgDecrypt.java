package com.qiuyu.demo.test;

import java.security.MessageDigest;

public class EnMicroMsgDecrypt {

    public static void main(String[] args) {
        try {
//            String a = "F:\\code\\ccod111\\wechat-explore\\systemInfo.cfg";
//            String b = "F:\\code\\ccod111\\wechat-explore\\CompatibleInfo.cfg";
//            ObjectInputStream in = new ObjectInputStream(new FileInputStream(a));
//            Object DL = in.readObject();
//            HashMap hashWithOutFormat = (HashMap) DL;
//            ObjectInputStream in1 = new ObjectInputStream(new FileInputStream(b));
//            Object DJ = in1.readObject();
//            HashMap hashWithOutFormat1 = (HashMap) DJ;
//            String S = String.valueOf(hashWithOutFormat1.get(Integer.valueOf(258))); // 取手机的IMEI
//            if (S == null ){
//                S = String.valueOf("1234567890ABCDEF");
//            }
            String S = String.valueOf("1234567890ABCDEF");
            System.out.println("The IMEI is : " + S);
//            String uin = String.valueOf(hashWithOutFormat.get(Integer.valueOf(1)));
            String uin = "1389364202";
            System.out.println("The uin is : " + uin);

            String s = S + uin; // 合并到一个字符串
            s = encode(s); // hash
            System.out.println("The Key is : " + s.substring(0, 7));
//            in.close();
//            in1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encode(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getEncode32(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getEncode32(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();

    }

}
