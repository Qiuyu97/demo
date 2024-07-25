package com.qiuyu.demo.test;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qiuyu.demo.domain.Article;
import com.qiuyu.demo.utils.JDBCUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * 调用AI能力对内容进行内容分析
 */
public class PeopleDailyArticleProcessing {
    private static final String API_URL = "https://api.coze.cn/open_api/v2/chat";

    private static final String API_BOT_ID = "7395427520118210611";
    private static final String API_TOKEN = "Bearer pat_dRujNtjkBHFcfZ5cickeFpMFI8nEioljCJfsEphrvzRoYws8vJKfxdJhivnUa59T";
    private static final int NUM_THREADS = 2;
    private static final int BATCH_QUERY_COUNT = 10;
    private static final ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

    public static void main(String[] args) {
        try {
            while (true) {
                // 每次查10个 防止OOM
                List<Article> articles = JDBCUtil.queryList("SELECT id, title, content, pub_year, pub_time, word_count, ai_check_status " +
                                "FROM people_daily_article WHERE ai_check_status = ? order by id LIMIT ?",
                        Article.class, AiCheckStatus.AICHECKSTATUS_NOT, BATCH_QUERY_COUNT);
                if (!CollectionUtils.isEmpty(articles)) {
                    CountDownLatch countDownLatch = new CountDownLatch(articles.size());
                    for (Article article : articles) {
                        executor.execute(() -> {
                            Long id = article.getId();
                            try {
                                System.out.println(">>>>>> Start AI check ID :" + id);
                                startChecking(id);
                                AiCheckResult aiCheckResult = processContent(id, article.getContent());
                                if (aiCheckResult.getSuccess()) {
                                    endChecking(id, aiCheckResult);
                                } else {
                                    failChecking(id);
                                }
                                System.out.println(">>>>>> End AI check ID :" + id);
                            } catch (Exception e) {
                                System.out.println("AI check error ID :" + id);
                            } finally {
                                countDownLatch.countDown();
                            }
                        });
                    }
                    countDownLatch.await();
                } else {
                    break;
                }
            }
            System.out.println("End All........................................................");
            // Iterate through results
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startChecking(Long id) {
        try {
            String updateSql = "UPDATE people_daily_article SET ai_check_status = ? WHERE id = ?";
            JDBCUtil.update(updateSql, AiCheckStatus.AICHECKSTATUS_START, id);
        } catch (Exception e) {
            System.out.println("startChecking error " + id);
            e.printStackTrace();
        }
    }

    private static void endChecking(Long id, AiCheckResult aiCheckResult) {
        try {
            String updateSql = "UPDATE people_daily_article SET ai_check_status = ?, `match` = ?, sex = ?, image = ?, sub_image = ?, theme = ?, area = ? WHERE id = ?";
            JDBCUtil.update(updateSql,
                    AiCheckStatus.AICHECKSTATUS_SUCCESS,
                    aiCheckResult.getMatch() ? 1 : 0,
                    aiCheckResult.getSex(),
                    aiCheckResult.getImage(),
                    aiCheckResult.getSubImage(),
                    aiCheckResult.getTheme(),
                    aiCheckResult.getArea(),
                    id);
        } catch (Exception e) {
            System.out.println("endChecking error " + id);
            e.printStackTrace();
        }
    }

    private static void failChecking(Long id) {
        try {
            String updateSql = "UPDATE people_daily_article SET ai_check_status = ? WHERE id = ?";
            JDBCUtil.update(updateSql, AiCheckStatus.AICHECKSTATUS_FAIL, id);
        } catch (Exception e) {
            System.out.println("failChecking error " + id);
            e.printStackTrace();
        }
    }

    private static AiCheckResult processContent(Long id, String content) {
        System.out.println("processContent id: " + id + " content " + content);
        AiCheckResult aiCheckResult = new AiCheckResult();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("conversation_id", "123456");
            jsonObject.put("bot_id", API_BOT_ID);
            jsonObject.put("user", "133333333333");
            jsonObject.put("query", content.trim().replace("\n", ""));
            jsonObject.put("stream", false);
            String resp = sendPostRequest(API_URL, API_TOKEN, jsonObject.toJSONString());
            if (StringUtils.isNotBlank(resp) && JSONUtil.isJson(resp)) {
                CoziResult coziResult = JSONUtil.toBean(resp, CoziResult.class);
                if (coziResult.isSuccess()) {
                    List<Message> messages = coziResult.getMessages();
                    if (!CollectionUtils.isEmpty(messages)) {
                        Message answer = messages.stream().filter(message -> "answer".equals(message.getType())).findFirst().orElse(new Message());
                        if (StringUtils.isNotBlank(answer.getContent()) && JSONUtil.isJson(answer.getContent())) {
                            System.out.println("match id: " + id + " result: " + answer.getContent());
                            JSONObject answerContent = JSONObject.parseObject(answer.getContent());
                            Boolean match = false;
                            if (answerContent.containsKey("match")) {
                                match = answerContent.getObject("match", Boolean.class);
                            }
                            if (match) {
                                aiCheckResult.setMatch(true);
                                Integer sex = answerContent.getInteger("sex");
                                if (sex != null) {
                                    aiCheckResult.setSex(sex);
                                }
                                Integer image = answerContent.getInteger("image");
                                if (image != null) {
                                    aiCheckResult.setImage(image);
                                }
                                Integer subImage = answerContent.getInteger("subImage");
                                if (subImage != null) {
                                    aiCheckResult.setSubImage(subImage);
                                }
                                Integer theme = answerContent.getInteger("theme");
                                if (theme != null) {
                                    aiCheckResult.setTheme(theme);
                                }
                                Integer area = answerContent.getInteger("area");
                                if (area != null) {
                                    aiCheckResult.setArea(area);
                                }
                            }
                            // 处理成功，获取到有效的json
                            aiCheckResult.setSuccess(true);
                        }
                    }
                } else {
                    System.out.println("request AI error id: " + id + " result: " + coziResult);
                    System.exit(Math.toIntExact(id));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aiCheckResult;
    }

    public static String sendPostRequest(String apiUrl, String accessToken, String jsonData) throws IOException {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(60000) // Timeout in milliseconds for establishing connection
                .setSocketTimeout(60000) // Timeout in milliseconds for waiting for data
                .build();

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        HttpPost httpPost = new HttpPost(apiUrl);

        // Set headers
        httpPost.setHeader("Authorization", accessToken);
        httpPost.setHeader("Content-Type", "application/json");

        // Set request body
        StringEntity entity = new StringEntity(jsonData, ContentType.APPLICATION_JSON.withCharset("UTF-8"));
        httpPost.setEntity(entity);

        // Execute HTTP post request
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();

        // Process response
        String responseBody = null;
        if (responseEntity != null) {
            responseBody = EntityUtils.toString(responseEntity);
        }

        return responseBody;
    }

    private static class AiCheckStatus {
        private static final int AICHECKSTATUS_NOT = 0;
        private static final int AICHECKSTATUS_START = 1;
        private static final int AICHECKSTATUS_SUCCESS = 2;
        private static final int AICHECKSTATUS_FAIL = 3;

    }

    @Data
    public static class CoziResult {
        @JsonProperty("messages")
        private List<Message> messages;

        @JsonProperty("conversation_id")
        private String conversationId;

        @JsonProperty("code")
        private int code;

        @JsonProperty("msg")
        private String msg;

        public boolean isSuccess() {
            return this.code == 0 || "success".equals(this.msg);
        }
    }

    @Data
    public static class Message {
        private String role;
        private String type;
        private String content;
        @JsonProperty("content_type")
        private String contentType;
    }

    @Data
    public static class AiCheckResult {
        private Boolean success = false;
        private Boolean match = false;
        private Integer sex = 3;
        private Integer image = 3;
        private Integer subImage = 0;
        private Integer theme = 0;
        private Integer area = 4;
    }


}
