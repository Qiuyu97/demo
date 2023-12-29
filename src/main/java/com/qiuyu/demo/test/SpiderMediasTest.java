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
import java.util.List;
import java.util.stream.Collectors;

public class SpiderMediasTest {

    private static final String siteUrl = "https://mc-public-test.tmuyun.com/test/1.html";

    public static void main(String[] args) {
        List<MediaDto> mediaDtos = new ArrayList<>();
        List<String> completedSites = new ArrayList<>();
        getMedias(mediaDtos, completedSites, siteUrl);
        System.out.println(mediaDtos.stream().map(MediaDto::getUrl).collect(Collectors.toList()));
    }

    private static void getMedias(List<MediaDto> list, List<String> completedSites, String siteUrl) {
        // 获取页面访问地址
        String pageUrl = siteUrl.substring(0, siteUrl.lastIndexOf("/") + 1);


        Connection connect = Jsoup.connect(siteUrl);
        Document document;
        try {
            document = connect.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 子页面
        Elements aElements = document.getElementsByTag("a");
        for (Element a : aElements) {
            String href = a.attr("href");
            String url = pageUrl + href;
            if (ObjectUtils.nullSafeEquals(siteUrl, url)) {
                continue;
            }
            if (!completedSites.contains(url))  {
                getMedias(list, completedSites, url);
            }
        }

        // 图片
        Elements imgElements = document.getElementsByTag("img");
        for (Element img : imgElements) {
            MediaDto mediaDto = new MediaDto();
            mediaDto.setTagName("img");
            mediaDto.setUrl(img.attr("src"));
            list.add(mediaDto);
        }

        // 音频
        Elements audioElements = document.getElementsByTag("audio");
        for (Element audio : audioElements) {
            MediaDto mediaDto = new MediaDto();
            mediaDto.setTagName("audio");
            mediaDto.setUrl(audio.attr("src"));
            list.add(mediaDto);
        }

        // 视频
        Elements videoElements = document.getElementsByTag("video");
        for (Element video : videoElements) {
            MediaDto mediaDto = new MediaDto();
            mediaDto.setTagName("video");
            mediaDto.setUrl(video.attr("src"));
            list.add(mediaDto);
        }

        completedSites.add(siteUrl);
    }


    @Data
    public static class MediaDto {
        private String tagName;
        private String url;
        private String downloadPath;
    }


}
