package com.hframework.smartsql.client2;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.Joiner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/8/10.
 */
public class DataSourceFactory {

    private static Map<String, DataSource> cache = new HashMap<>();

    public static DataSource getDataSource(String url, String username, String password) {
        String cacheKey = Joiner.on("|").join(new String[]{url, username, password});
        if(!cache.containsKey(cacheKey)) {
            synchronized (DataSourceFactory.class) {
                if(!cache.containsKey(cacheKey)) {
                    try {
                        cache.put(cacheKey, getDataSourceInternal(url, username, password));
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException("datasource create error => " + e.getMessage());
                    }
                }
            }
        }
        return cache.get(cacheKey);
    }


    private static DataSource getDataSourceInternal(String url, String username, String password) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver"); //设置驱动
        dataSource.setUsername("root"); //用户名
        dataSource.setPassword("11111111");//密码
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/jspdemo");//URL
        dataSource.setInitialSize(5);//连接池初始化大小
        dataSource.setMinIdle(1);//连接池最小空闲数
        dataSource.setMaxActive(10); // 启用监控统计功能
        dataSource.setFilters("stat");// for mysql
        dataSource.setPoolPreparedStatements(false);

        return dataSource;
    }
}
