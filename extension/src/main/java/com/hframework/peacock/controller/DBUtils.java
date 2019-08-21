package com.hframework.peacock.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.hframework.common.resource.ResourceWrapper;
import com.hframework.peacock.config.domain.model.CfgDatasouceMysql;
import org.apache.commons.beanutils.BeanUtils;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by zhangquanhong on 2016/10/20.
 */
public class DBUtils {

    private static DataSource createDataSource(String url, String username, String password) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver"); //设置驱动
        dataSource.setUsername(username); //用户名
        dataSource.setPassword(password);//密码
        dataSource.setUrl(url);//URL
        dataSource.setInitialSize(5);//连接池初始化大小
        dataSource.setMinIdle(1);//连接池最小空闲数
        dataSource.setMaxActive(10); // 启用监控统计功能
        dataSource.setFilters("stat");// for mysql
        dataSource.setPoolPreparedStatements(false);

        return dataSource;
    }


    public static void testConnection(String url, String user, String password) throws PropertyVetoException, SQLException {
        List query = query(url, user, password, "select '连接成功' from dual");
        System.out.println(query.get(0));
    }

    public static List<Map> query(CfgDatasouceMysql cfgDatasource, String sql) throws PropertyVetoException, SQLException {
        return query("jdbc:mysql://" + cfgDatasource.getHost() + ":" + cfgDatasource.getPort() + "/" + cfgDatasource.getDatabase() + "?useUnicode=true", cfgDatasource.getUsername(), cfgDatasource.getPassword(), sql);
    }

    public static List<Map> query(String url, String user, String password, String sql) throws PropertyVetoException, SQLException {
        List result = new ArrayList();
        DataSource dataSource = createDataSource(url, user, password);
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            int columnCount = resultSet.getMetaData().getColumnCount();
            Set<String> columns = new HashSet<>();
            for (int i = 0; i < columnCount; i++) {
                columns.add(resultSet.getMetaData().getColumnLabel(i + 1));
            }

            while (resultSet.next()) {
                Map<String, String> map = new HashMap<>();
                for (String column : columns) {
                    map.put(ResourceWrapper.JavaUtil.getJavaVarName(column), resultSet.getString(column));
                }
                result.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw  e;
        }finally {
            try {
                if(statement != null)statement.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        connection.close();
        return result;
    }


    public static void main(String[] args) throws PropertyVetoException, SQLException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List query = query("jdbc:mysql://127.0.0.1:3306/beetle", "canal", "NHY67ujm", "SHOW COLUMNS FROM browse_log;");

        Map<String, String> map = new HashMap<>();
        map.put("abc", "1");
        map.put("ad", "2");


        String abc = BeanUtils.getProperty(map, "ad");
        System.out.println(abc);
    }
}
