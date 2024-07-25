package com.qiuyu.demo.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * 数据库连接池配置类
 */
public class DataSourceConfig {

    private final static Log log = LogFactory.getLog(DataSourceConfig.class);

    private static volatile DataSource dataSource;

    /**
     * 这里优化为使用单例模式去构建dataSource
     * @author qiuyu
     **/
    private DataSourceConfig() {

    }

    /**
     * 获取数据库连池接对象
     *
     * @return 数据库连接池对象
     */
    public static DataSource getDataSource() throws Exception {
        // DCL
        if (dataSource == null) {
            synchronized (DataSourceConfig.class) {
                if (dataSource == null) {
                    Properties properties = new Properties();
                    properties.load(DataSourceConfig.class.getResourceAsStream("/druid.properties"));
                    log.info("DataSourceConfig jdbc properties: " + properties);
                    dataSource = DruidDataSourceFactory.createDataSource(properties);
                }
            }
        }
        return dataSource;
    }

    public static void initialize(){
        try {
            getDataSource().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接对象
     *
     * @return 数据库连接对象
     * @throws Exception 异常
     */
    public static Connection getConnection() throws Exception {
        return getDataSource().getConnection();
    }
}
