package com.qiuyu.demo.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.Security;

public class DesUtil {

    public static final String DEFAULT_PASSWORD_CRYPT_KEY = "zrtg";

    private static final String DES = "DES/ECB/PKCS7Padding";
    //private static Cipher cipher = null;
    private static Boolean isInit = false;

    static {
        // Cipher对象实际完成加密操作
        try {
            if(!isInit){
                Security.addProvider(new BouncyCastleProvider());
                isInit = true;
            }/*
            cipher = Cipher.getInstance(DES,"BC");
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DesUtil() {

    }

    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        // 一个SecretKey对象
        //SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey =
        new SecretKeySpec(dks.getKey(),"DES");//keyFactory.generateSecret(dks);
        // 用密匙初始化Cipher对象

        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        // 正式执行加密操作
        return cipher.doFinal(src);
    }

    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {

        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
        // 一个SecretKey对象
        //SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey =
        new SecretKeySpec(dks.getKey(),"DES");//keyFactory.generateSecret(dks);

        // 用密匙初始化Cipher对象
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        // 正式执行解密操作
        return cipher.doFinal(src);
    }

    public static String decrypt(String data) {
        try {
            return new String(decrypt(hex2byte(data.getBytes()),DEFAULT_PASSWORD_CRYPT_KEY.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
        return null;
    }

    public static String decrypt(String data, String key) throws Exception {
        return new String(
                decrypt(hex2byte(data.getBytes()), key.getBytes()));
    }

    public static String encrypt(String data) {
        try {
            return byte2hex(encrypt(data.getBytes(),
                    DEFAULT_PASSWORD_CRYPT_KEY.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String data, String key) throws Exception {
        return byte2hex(encrypt(data.getBytes(), key.getBytes()));
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";

        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }

        return hs.toUpperCase();
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }

        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }

        return b2;
    }

//	public static void main(String[] args) {
//		String url = "http://hunqijiangzhi.com";
//		url = DesUtil.encrypt(url);
//		System.out.println(url);
//		System.out.println(DesUtil.decrypt(url));
//	}

}