package com.qiuyu.demo.utils;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;

/**
 * 生成数据库文档
 * @author qy
 * @since 2022/10/25 10:10
 */
public class GenerateDBWord {


    public static void main(String[] args) {
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/fruit?serverTimezone=Asia/Shanghai");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("User123$");
        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);

        // 1、生成文件配置
        EngineConfig engineConfig = EngineConfig.builder()
                //生成文件路径(改成自己的生成路径)
                .fileOutputDir("C:\\Users\\11631\\Desktop")
                //生成后是否立即打开目录
                .openOutputDir(true)
                //文件类型  有HTML、WORD、MD三种枚举选择
                .fileType(EngineFileType.WORD)
                //生成模板实现
                .produceType(EngineTemplateType.freemarker)
                //自定义文件名称
                .fileName("水果管理系统数据库文档").build();

        //忽略表名（可选）
        ArrayList<String> ignoreTableName = new ArrayList<>();
        //ignoreTableName.add("cj_gs");
        // 忽略表前缀（可选）
        ArrayList<String> ignorePrefix = new ArrayList<>();
        //ignorePrefix.add("xxl_");
        // 忽略表后缀（可选）
        ArrayList<String> ignoreSuffix = new ArrayList<>();
        //ignoreSuffix.add("_user");

        // 2、配置想要忽略的表（可选）
        ProcessConfig processConfig = ProcessConfig.builder()
                .ignoreTableName(ignoreTableName)
                .ignoreTablePrefix(ignorePrefix)
                .ignoreTableSuffix(ignoreSuffix)
                .build();
        //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
        //根据名称指定表生成
        //.designatedTableName(new ArrayList<>())
        //根据表前缀生成
        //.designatedTablePrefix(new ArrayList<>())
        //根据表后缀生成
        //.designatedTableSuffix(new ArrayList<>())
        //忽略表名
        //.ignoreTableName(ignoreTableName)
        //.build();

        // 3、生成文档配置（包含以下自定义版本号、标题、描述（数据库名 + 描述 = 文件名）等配置连接）
        Configuration config = Configuration.builder()
                //版本
                .version("1.0.0")
                //描述
                .description("水果管理系统数据库文档")
                //数据源
                .dataSource(dataSource)
                //生成配置
                .engineConfig(engineConfig)
                //生成配置
                .produceConfig(processConfig)
                .build();

        // 4、执行生成
        new DocumentationExecute(config).execute();
    }


}