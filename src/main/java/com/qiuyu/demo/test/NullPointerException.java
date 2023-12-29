package com.qiuyu.demo.test;

import lombok.Data;

public class NullPointerException {

    public static void main(String[] args) {
        DocContentCheckDetail checkDetail = new DocContentCheckDetail();
        int idx = 1;
        String s = "";
        DocumentContent docContent = new DocumentContent();
        checkDetail.setDocWordsCount(idx == 1 ?
                        docContent.getDocWordsCount()
                        : s.length());
    }

    @Data
    static class DocContentCheckDetail {

        private Integer docWordsCount;
    }


    @Data
    static class DocumentContent {

        private Integer docWordsCount;
    }
}
