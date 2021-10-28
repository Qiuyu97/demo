package com.qiuyu.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * TODO DESCRIPTION
 *
 * @author zzl
 * @date 2021-08-25
 */
public class FileBinary {
    private static final Logger log = LoggerFactory.getLogger(FileBinary.class);


    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * 网络文件转换为byte二进制
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String url) throws IOException {
        URLConnection conn = new URL(url).openConnection();
        DataInputStream in = new DataInputStream(conn.getInputStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    public static byte[] toByteArray(URLConnection conn) throws IOException {
        DataInputStream in = new DataInputStream(conn.getInputStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4 * 1024];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * @throws IOException
     * @throws MalformedURLException
     *
     * @Title: toByteArray
     * @Description: 网络文件转换为本地文件
     * @param @param url
     * @param @return
     * @param @throws IOException    设定文件
     * @return byte[]    返回类型
     * @throws
     */
    public static void toBDFile(String urlStr, String bdUrl) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        DataInputStream in = new DataInputStream(conn.getInputStream());
        byte[] data = toByteArray(in);
        in.close();
        FileOutputStream out = new FileOutputStream(bdUrl);
        out.write(data);
        out.close();
    }


    /**
     * 获取网络文件的输入流
     * @param urlStr
     * @return
     */
    public static InputStream getInputStreamByUrl(String urlStr) {
        DataInputStream in = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = new DataInputStream(conn.getInputStream());
        } catch (IOException e) {
            log.error("url转换输入流失败,错误信息{}", e.getMessage());
        }
        return in;
    }

}