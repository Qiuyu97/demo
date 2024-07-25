package com.qiuyu.demo.utils;

import com.qiuyu.demo.config.DataSourceConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC 工具类
 */
public class JDBCUtil {
    /**
     * 日志对象
     */
    private final static Log log = LogFactory.getLog(JDBCUtil.class);

    /**
     * 获取预编译对象
     *
     * @param sql    sql
     * @param params sql的可变参数
     * @return 编译后预编译对象
     * @throws Exception 异常
     */
    public static PreparedStatement getPreparedStatement(String sql, Object... params) throws Exception {
        // 获取预编译对象
        Connection connection = DataSourceConfig.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        // sql 中比如：select * from t_employee where id = ? 要把问号给替换掉
        // 获取sql中的?对象
        ParameterMetaData parameterMetaData = ps.getParameterMetaData();
        // 获取sql中?的个数
        int parameterCount = parameterMetaData.getParameterCount();
        // SQL params参数个数和SQL中的?个数不匹配
        if (parameterCount != params.length) {
            throw new RuntimeException("SQL参数不匹配");
        }
        // 如果有参数 我们就行赋值
        for (int i = 1; i <= parameterCount; i++) {
            ps.setObject(i, params[i - 1]);
        }
        // 日志：查看sql
//        log.info("JDBCUtil => " + ps);
        return ps;
    }

    /**
     * DDL DML 语句操作方法
     *
     * @param sql    sql
     * @param params 参数
     * @return 影响记录数
     * @throws Exception 异常
     */
    public static int update(String sql, Object... params) throws Exception {
        // 获取编译后的预编对象
        PreparedStatement ps = getPreparedStatement(sql, params);
        // 执行sql、
        int i = ps.executeUpdate();
        // 关闭资源
        close(ps, ps.getConnection());
        return i;
    }

    /**
     * DQL 语句 查询后返回对应的实体类
     *
     * @param sql    sql
     * @param c      类型
     * @param params sql的参数
     * @param <T>    泛型
     * @return 实体类对象
     * @throws Exception 异常
     */
    public static <T> T query(String sql, Class<T> c, Object... params) throws Exception {
        // 获取预编译对象
        PreparedStatement ps = getPreparedStatement(sql, params);
        // 执行SQL
        ResultSet rs = ps.executeQuery();
        // 获取返回对象的元数据 如比如员工表中的：所有字段 id, name, age, gender, work_id, salary
        ResultSetMetaData metaData = rs.getMetaData();
        // 获取元数据的个数
        int columnCount = metaData.getColumnCount();
        // 创建实体类
        T t = null;
        while (rs.next()) {
            t = c.getConstructor().newInstance();
            for (int i = 1; i <= columnCount; i++) {
                // 通过数据库的字段 推导 实体类的字段
                String filedName = convertDBFiledNameToEntityFiledName(metaData.getColumnName(i));
                // 通过我们的实体类的字段来获取set方法
                Method method = getSetMethodByFiledName(filedName, c);
                // 执行set方法
                method.invoke(t, rs.getObject(metaData.getColumnName(i)));
            }
        }
        // 关闭资源
        close(rs, ps, ps.getConnection());
        return t;
    }

    public static <T> List<T> queryList(String sql, Class<T> c, Object... params) throws Exception {
        // 获取预编译对象
        PreparedStatement ps = getPreparedStatement(sql, params);
        // 执行SQL
        ResultSet rs = ps.executeQuery();
        // 获取返回对象的元数据 如比如员工表中的：所有字段 id, name, age, gender, work_id, salary
        ResultSetMetaData metaData = rs.getMetaData();
        // 获取元数据的个数
        int columnCount = metaData.getColumnCount();
        // 集合
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            // 创建实体类
            T t = c.getConstructor().newInstance();
            for (int i = 1; i <= columnCount; i++) {
                // 通过数据库的字段 推导 实体类的字段
                String filedName = convertDBFiledNameToEntityFiledName(metaData.getColumnName(i));
                // 通过我们的实体类的字段来获取set方法
                Method method = getSetMethodByFiledName(filedName, c);
                // 执行set方法
                method.invoke(t, rs.getObject(metaData.getColumnName(i)));
            }
            list.add(t);
        }
        // 关闭资源
        close(rs, ps, ps.getConnection());
        return list;
    }

    private static <T> Method getSetMethodByFiledName(String filedName, Class<T> c) {
        // 获取类中所有的方法
        Method[] methods = c.getMethods();
        for (Method method : methods) {
            String name = (char) (filedName.charAt(0) - 32) + filedName.substring(1);
            if (method.getName().equals("set" + name)) {
                return method;
            }
        }
        return null;
    }

    /**
     * 将数据库的字段 转换成实体类的字段 比如 work_id => workId
     *
     * @param name 数据字段的名字
     * @return 实体类的字段名
     */
    private static String convertDBFiledNameToEntityFiledName(String name) {
        // 不包含下划线 直接返回
        if (!name.contains("_")) {
            return name;
        }
        StringBuilder sb = new StringBuilder();
        // 通过下划线分割比如work_id 变成 [work,id]
        String[] split = name.split("_");
        sb.append(split[0]);
        for (int i = 1; i < split.length; i++) {
            // 首字母边大写
            sb.append((char) (split[i].charAt(0) - 32));
            sb.append(split[i].substring(1));
        }
        return sb.toString();
    }

    /**
     * 关闭资源
     *
     * @param es  可变参数对象
     * @param <E> 泛型
     * @throws Exception 异常
     */
    @SafeVarargs
    public static <E extends AutoCloseable> void close(E... es) throws Exception {
        for (E e : es) {
            if (e != null) {
                e.close();
            }
        }
    }
}
