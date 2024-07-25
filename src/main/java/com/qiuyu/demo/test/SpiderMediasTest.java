package com.qiuyu.demo.test;

import lombok.Data;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SpiderMediasTest {

    private static final String siteUrl = "https://mc-public-test.tmuyun.com/test/1.html";

    // 获取页面访问地址
    private static final String pageUrl = siteUrl.substring(0, siteUrl.lastIndexOf("/") + 1);

    public static void main(String[] args) {
        // 素材
        List<MediaDto> mediaDtos = new ArrayList<>();
        // 完成的站点页面
        List<String> completedSites = new ArrayList<>();
        getMedias(mediaDtos, completedSites, siteUrl);
        System.out.println(mediaDtos.stream().map(MediaDto::getUrl).collect(Collectors.toList()));
    }

    /**
     * 收集站点素材
     * @param collection 媒资素材列表
     * @param completedSites 完成素材爬取的站点集合
     * @param siteUrl 站点地址
     */
    private static void getMedias(Collection<MediaDto> collection, Collection<String> completedSites, String siteUrl) {
        Connection connect = Jsoup.connect(siteUrl);
        Document document;
        try {
            document = connect.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 图片
        Elements imgElements = document.getElementsByTag("img");
        for (Element img : imgElements) {
            MediaDto mediaDto = new MediaDto();
            mediaDto.setTagName("img");
            mediaDto.setUrl(img.attr("src"));
            collection.add(mediaDto);
        }

        // 音频
        Elements audioElements = document.getElementsByTag("audio");
        for (Element audio : audioElements) {
            MediaDto mediaDto = new MediaDto();
            mediaDto.setTagName("audio");
            mediaDto.setUrl(audio.attr("src"));
            collection.add(mediaDto);
        }

        // 视频
        Elements videoElements = document.getElementsByTag("video");
        for (Element video : videoElements) {
            MediaDto mediaDto = new MediaDto();
            mediaDto.setTagName("video");
            mediaDto.setUrl(video.attr("src"));
            collection.add(mediaDto);
        }

        // iframe
        Elements iframeElements = document.getElementsByTag("iframe");
        for (Element iframe : iframeElements) {
            MediaDto mediaDto = new MediaDto();
            mediaDto.setTagName("iframe");
            mediaDto.setUrl(iframe.attr("src"));
            collection.add(mediaDto);
        }
        // 完成站点素材爬取
        completedSites.add(siteUrl);

        // 获取页面引用的子页面
        Elements aElements = document.getElementsByTag("a");
        for (Element a : aElements) {
            String href = a.attr("href");
            String url = pageUrl + href;
            // 排除当前页面
            if (ObjectUtils.nullSafeEquals(siteUrl, url)) {
                continue;
            }
            if (!completedSites.contains(url))  {
                // 递归获取子页面的素材
                getMedias(collection, completedSites, url);
            }
        }

    }


    @Data
    public static class MediaDto {
        private String tagName;
        private String url;
    }


}
