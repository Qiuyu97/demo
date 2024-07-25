package com.qiuyu.demo.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {

    private Long id;
    private String fileName;
    private String title;
    private String content;
    private String pubYear;
    private LocalDateTime pubTime;
    private Integer wordCount;
    private Integer aiCheckStatus;
    private LocalDateTime createdTime;
    private Integer match;
    private Integer sex;
    private Integer image;
    private Integer subImage;
    private Integer theme;
    private Integer area;

}
