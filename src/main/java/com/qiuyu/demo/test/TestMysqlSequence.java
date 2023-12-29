package com.qiuyu.demo.test;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class TestMysqlSequence {


    // 测试下自定义seq并发
    private static final JdbcTemplate jdbcTemplate1;
    private static final JdbcTemplate jdbcTemplate2;
    private static final JdbcTemplate jdbcTemplate3;
    private static final JdbcTemplate jdbcTemplate4;

    static {
        String driver = "com.mysql.cj.jdbc.Driver";//mysql驱动
        String url = "jdbc:mysql://127.0.0.1:3306/demo";//连接地址
        String user = "root";//用户
        String password = "User123$";//密码

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        jdbcTemplate1 = new JdbcTemplate(dataSource);
        jdbcTemplate2 = new JdbcTemplate(dataSource);
        jdbcTemplate3 = new JdbcTemplate(dataSource);
        jdbcTemplate4 = new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        String sql = "SELECT NEXTVAL('DOC_CHANNEL_SEQUENCE')";
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                jdbcTemplate1.queryForObject(sql, Long.TYPE);
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                jdbcTemplate2.queryForObject(sql, Long.TYPE);
            }
        });
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                jdbcTemplate3.queryForObject(sql, Long.TYPE);
            }
        });
//        Thread thread4 = new Thread(() -> {
//            for (int i = 0; i < 10000; i++) {
//                jdbcTemplate4.queryForObject(sql, Long.TYPE);
//            }        });
        thread1.start();
        thread2.start();
        thread3.start();
//        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
//        thread4.join();


        System.out.println("end....耗时：" + (System.currentTimeMillis() - start) + "ms");
    }


}