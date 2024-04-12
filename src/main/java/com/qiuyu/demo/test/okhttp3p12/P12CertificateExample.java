package com.qiuyu.demo.test.okhttp3p12;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class P12CertificateExample {
    public static void main(String[] args) throws Exception {
        // 加载P12证书文件
        String p12File = "E:\\other-work\\2024-04-11 okhttp3p12\\java.io.BufferedInputStream@5ad56b5.p12";
        String password = "uX39!dd$#rr_XIyb%";
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        InputStream inputStream = Files.newInputStream(Paths.get(p12File));
        keyStore.load(inputStream, password.toCharArray());

        // 创建KeyManagerFactory，并初始化
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password.toCharArray());

        // 创建信任管理器
        TrustManager[] trustManagers = { new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        } };

        // 创建SSL上下文并初始化
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, new SecureRandom());

        // 设置默认的SSL上下文
        SSLContext.setDefault(sslContext);

        // 创建URL对象
        String url = "https://api.iimzt.com/app/list/posts?type=beautys&order=rand&page=5";
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        // 发送请求
        connection.setRequestMethod("GET");
        // 必须设置userAgent才能访问
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
        connection.connect();

        // 获取响应
        int responseCode = connection.getResponseCode();
        InputStream responseStream;
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            responseStream = connection.getInputStream();
        } else {
            responseStream = connection.getErrorStream();
        }

        // 处理响应数据
        // ...
        // 读取响应数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        // 打印响应结果
        System.out.println("Response: " + response.toString());
        // 关闭连接和流
        responseStream.close();
        connection.disconnect();
    }
}