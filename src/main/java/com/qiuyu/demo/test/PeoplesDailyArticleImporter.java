package com.qiuyu.demo.test;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PeoplesDailyArticleImporter {

    private static final List<String> keyword = Arrays.asList("医生");

    private static final Pattern pattern1 = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");

    private static final Pattern pattern2 = Pattern.compile("\\d{4}年\\d{2}月\\d{2}日");

    private static final Pattern pattern3 = Pattern.compile("\\d{4}年\\d{1}月\\d{1}日");

    private static final Pattern pattern4 = Pattern.compile("\\d{4}年\\d{1}月\\d{2}日");

    private static final Pattern pattern5 = Pattern.compile("\\d{4}年\\d{2}月\\d{1}日");

    private static final Pattern pattern6 = Pattern.compile("\\d{4}年\\d{2}月");

    private static final DateTimeFormatter ofPattern1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter ofPattern2 = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
    private static final DateTimeFormatter ofPattern3 = DateTimeFormatter.ofPattern("yyyy年M月d日");
    private static final DateTimeFormatter ofPattern4 = DateTimeFormatter.ofPattern("yyyy年M月dd日");
    private static final DateTimeFormatter ofPattern5 = DateTimeFormatter.ofPattern("yyyy年MM月d日");

    private static final DateTimeFormatter ofPattern6 = DateTimeFormatter.ofPattern("yyyy年MM月");

    private static int insertCount = 0;
    public static void main(String[] args) {
        String folderPath = "E:\\other-work\\data\\peopleDailyArticle\\2021";
        String databaseUrl = "jdbc:mysql://localhost:3306/demo"; // 替换为您的数据库连接信息
        String databaseUser = "root";
        String databasePassword = "User123$";

        try {
            // 加载数据库驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 建立数据库连接
            Connection connection = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
            System.out.println("数据库加载完成......");
            // 遍历文件夹中的所有文件
            File folder = new File(folderPath);
            File[] files = folder.listFiles();
            if (files == null || files.length ==0) {
                return;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println("读取文件夹：" + file.getName());
                    File[] files1 = file.listFiles();
                    if (files1 == null || files1.length ==0) {
                        return;
                    }
                    for (File file1 : files1) {
                        if (file1.isFile() && file1.getName().endsWith(".txt")) {
                            System.out.println("读取文件：" + file1.getName());
                            // 读取文件内容
                            List<Article> articles = readFileContent(file1);
                            if (!CollectionUtils.isEmpty(articles)) {
                                for (Article article : articles) {
                                    insertArticleToDatabase(connection, file1.getName(), article);
                                }
                            }
                        }
                    }
                } else if (file.isFile() && file.getName().endsWith(".txt")) {
                    System.out.println("读取文件：" + file.getName());
                    // 读取文件内容
                    List<Article> articles = readFileContent(file);
                    if (!CollectionUtils.isEmpty(articles)) {
                        for (Article article : articles) {
                            insertArticleToDatabase(connection, file.getName(), article);
                        }
                    }
                }
            }

            System.out.println("数据导入完成!");
        } catch (ClassNotFoundException e) {
            System.out.println("无法加载数据库驱动程序: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("数据库操作异常: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("发生未知错误: " + e.getMessage());
        }
    }


    private static List<Article> readFileContent(File file) {
        List<Article> articles = new ArrayList<>();
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), Charset.forName("GBK"))))
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            Article article = null;
            String line;
            StringBuilder content = null;
            String filename = file.getName().substring(0, file.getName().indexOf("."));
            while ((line = reader.readLine()) != null) {
                // 过滤空行
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                // 如果上一阶段没有获取到标题，说明 ### 后面标题为空，取后面的非空第一行当标题
                if (article != null && StringUtils.isBlank(article.getTitle())) {
                    article.setTitle(line.trim());
                }

                // 开始标志,同时也是上一篇的结束标志
                if (line.contains("###")) {
                    filterUpArticleByKeywords(articles, article, content, filename);
                    content = new StringBuilder();
                    article = new Article();
                    article.setTitle(getTitle(line));
                    continue;
                }
                // 发布时间
                if (pattern1.matcher(line.trim()).matches()
                        || pattern2.matcher(line.trim()).matches()
                        || pattern3.matcher(line.trim()).matches()
                        || pattern4.matcher(line.trim()).matches()
                        || pattern5.matcher(line.trim()).matches()
                ) {
                    if (article != null) {
                        article.setPubTime(getPubTime(line.trim(), filename));
                    }
                    continue;
                }

                // 正文
                if (content != null) {
                    content.append(line).append("\n");
                }

            }
            // 这里是最后一篇文章，因为后续没有 ### 的标识了
            filterUpArticleByKeywords(articles, article, content, filename);
        } catch (Exception e) {
            System.out.println("读取文件 " + file.getName() + " 时发生错误: " + e.getMessage());
        }
        return articles;
    }

    /**
     * 上一篇稿件过滤存储
     * @param articles
     * @param article
     * @param content
     * @param filename
     */
    private static void filterUpArticleByKeywords(List<Article> articles, Article article, StringBuilder content, String filename) {
        if (articles == null) {
            articles = new ArrayList<>();
        }
        if (article != null && article.getTitle() != null) {
            article.setContent(content.toString());
            article.setWordCount(article.getContent().replace(" ", "").length());
            if (article.getPubTime() == null) {
                article.setPubTime(getPubTime(null, filename));
            }
            for (String keyword : PeoplesDailyArticleImporter.keyword) {
                if (article.getTitle().contains(keyword)
                        || article.getContent().contains(keyword)) {
                    articles.add(article);
                    break;
                }
            }
        }
    }


    private static String getTitle(String line) {
        try {
            return line.replace("#", "").trim();
        }catch (Exception e) {
            System.out.println("getTitle error. line: " + line);
            return "";
        }
    }

    private static LocalDate getPubTime(String line, String filename) {
        LocalDate time = null;
        try {
            if (line == null) {
                line = "";
            }
            String trim = line.trim();
            if (pattern1.matcher(trim).matches()) {
                time = LocalDate.parse(trim, ofPattern1);
            } else if (pattern2.matcher(trim).matches()) {
                time = LocalDate.parse(trim, ofPattern2);
            } else if (pattern3.matcher(trim).matches()) {
                time = LocalDate.parse(trim, ofPattern3);
            } else if (pattern4.matcher(trim).matches()) {
                time = LocalDate.parse(trim, ofPattern4);
            } else if (pattern5.matcher(trim).matches()) {
                time = LocalDate.parse(trim, ofPattern5);
            } else if (pattern6.matcher(filename).matches()){
                // 上述没解析到pub_time，取文件名+15日
                time = LocalDate.parse(filename+"15日", ofPattern2);
            }
        } catch (Exception e) {
            System.out.println("getPubTime error. line: " + line);
            e.printStackTrace();
        }
        return time;
    }


    private static void insertArticleToDatabase(Connection connection, String fileName, Article article) {
        String sql = "INSERT INTO `people_daily_article` ( `file_name`, `title`, `content`, `pub_time`, `word_count`, `created_time`, `pub_year`) VALUES (?, ?, ?, ?, ?, NOW(), ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (article != null) {
                statement.setString(1, fileName);
                statement.setString(2, article.getTitle());
                statement.setString(3, article.getContent());
                statement.setDate(4, Date.valueOf(article.getPubTime()));
                statement.setInt(5, article.getWordCount());
                String year = "";
                if (article.getPubTime() != null) {
                    year = String.valueOf(article.getPubTime().getYear());
                }
                statement.setString(6, year);
                statement.executeUpdate();
                insertCount++;
                if (insertCount % 10 == 0) {
                    System.out.println(">>> insert count: " + insertCount + "条");
                }
            }
        } catch (SQLException e) {
            System.out.println("插入文章 " + fileName + " 时发生错误: " + e.getMessage());
        }
    }


    @Data
    private static class Article {

        private String title;
        private String content;
        private LocalDate pubTime;
        private Integer wordCount;

    }


}