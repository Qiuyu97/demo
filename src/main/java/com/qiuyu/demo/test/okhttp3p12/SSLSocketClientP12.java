package com.qiuyu.demo.test.okhttp3p12;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class SSLSocketClientP12 {
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";//证书类型
    private static final String KEY_STORE_PASSWORD = "uX39!dd$#rr_XIyb%";//证书密码（应该是客户端证书密码，没有密码的直接改为空字符串）
    //private static final String KEY_STORE_PASSWORD = "123456";//证书密码（应该是客户端证书密码，没有密码的直接改为空字符串）

    public static OkHttpClient getClient1(OkHttpClient.Builder builder1) {
        OkHttpClient.Builder builder = builder1
                .hostnameVerifier(SSLSocketClientP12.getHostnameVerifier())//配置
                .sslSocketFactory(SSLSocketClientP12.getSSLSocketFactory(), SSLSocketClientP12.getX509TrustManager());
        return builder.build();
    }

    public static void main(String[] args) throws IOException {
        String url = "https://api.iimzt.com/app/list/posts?type=beautys&order=rand&page=5";
        OkHttpClient client = getClient1(new OkHttpClient.Builder());

        Request.Builder builder = new Request.Builder()
                .url(url)
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
        Request request = builder.build();

        Response response = client
                .newCall(request).execute();
        String result = response.body().string();
//打印结果返回数据
        System.out.println(result);
    }


    //获取这个SSLSocketFactory
    public static SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory factory = null;
        try {
            InputStream cerInputStream = new FileInputStream("E:\\other-work\\2024-04-11 okhttp3p12\\java.io.BufferedInputStream@5ad56b5.p12");
            SSLContext sslContext = SSLContext.getInstance("TLS");
            //InputStream cerInputStream = new FileInputStream("D:\\tools\\1.p12");
            //SSLContext sslContext = SSLContext.getInstance("TLS");

            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
            keyStore.load(cerInputStream, KEY_STORE_PASSWORD.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), getTrustManager(), new SecureRandom());
            factory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return factory;
    }

    //获取TrustManager
    private static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
        return trustAllCerts;
    }

    //获取HostnameVerifier
    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = (s, sslSession) -> true;
        return hostnameVerifier;
    }

    public static X509TrustManager getX509TrustManager() {
        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        return x509TrustManager;
    }

}
