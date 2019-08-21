package com.hframework.smartsql.client2;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.util.message.JsonUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/8/10.
 */
public class DBClient {

    private static final Logger logger = LoggerFactory.getLogger(DBClient.class);

    private static final Boolean REGISTER_DATABASE_IGNORE_KEY_REPEAT = true;
    private static ThreadLocal<String> currentDatabaseKey = new ThreadLocal<>();

    private static String defaultDatabaseKey = null;

    private static Map<String, DBInfo> dbInfoCache = new HashMap<>();
    private static Map<String, DataSource> cache = new HashMap<>();

    public static void registerDatabase(String key, String url, String username, String password) {
        logger.info("register database :{},{},{}", key, url, username);
        if(dbInfoCache.containsKey(key)) {
            if(!REGISTER_DATABASE_IGNORE_KEY_REPEAT) {
                throw new RuntimeException("register database key [" + key + "] exists !");
            }
        }else {
            dbInfoCache.put(key, new DBInfo(url, username, password));
        }
        logger.info("register database success :{},{},{}", key, url, username);
    }

    public static void setCurrentDatabaseKey(String key) {
        if(defaultDatabaseKey == null) {
            defaultDatabaseKey = key;
        }
        currentDatabaseKey.set(key);
    }

    public static String getCurrentDatabaseKey() {
        String key = currentDatabaseKey.get();
        if(StringUtils.isBlank(key) && defaultDatabaseKey != null) {
            key = defaultDatabaseKey;
        }
        if(StringUtils.isBlank(key)) {
            throw new RuntimeException("get current database key [" + key + "] failed , not exists !");
        }
        logger.info("get current database success :{}", key);
        return key;
    }

    public static boolean containDataSource(String key) {
        return dbInfoCache.containsKey(key);
    }

    public static DataSource getDataSource(String key) {
        if(!dbInfoCache.containsKey(key)) {
            throw new RuntimeException("get datasource failed, [" + key + "] 's not register !");
        }
        DBInfo dbInfo = dbInfoCache.get(key);
        return getDataSource(dbInfo.getUrl(), dbInfo.getUsername(), dbInfo.getPassword());
    }

    private static DataSource getDataSource(String url, String username, String password) {
        String cacheKey = Joiner.on("|").join(new String[]{url, username, password});
        if(!cache.containsKey(cacheKey)) {
            synchronized (DBClient.class) {
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

    /**
     * 建立数据库连接
     * @return 数据库连接
     * @throws Exception
     */
    private static Connection getConnection(String key) throws SQLException {
        Connection connection = null;
        try {
            // 获取连接
            connection = getDataSource(key).getConnection();
        } catch (SQLException e) {
           throw e;

        }
        return connection;
    }

    /**
     * 关闭所有资源
     */
    private static void closeAll(ResultSet resultSet, PreparedStatement preparedStatement, CallableStatement callableStatement, Connection connection) {
        // 关闭结果集对象
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // 关闭PreparedStatement对象
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // 关闭CallableStatement 对象
        if (callableStatement != null) {
            try {
                callableStatement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        // 关闭Connection 对象
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
    * insert update delete SQL语句的执行的统一方法
    * @param sql SQL语句
    * @param params 参数数组，若没有参数则为null
    * @return 受影响的行数
    * @throws Exception
    */
    public static int executeUpdate(String sql, Object[] params)  {
        return executeUpdate(getCurrentDatabaseKey(), sql, params);
    }

    /**
     * insert update delete SQL语句的执行的统一方法
     * @param sql SQL语句
     * @param params 参数数组，若没有参数则为null
     * @return 受影响的行数
     * @throws Exception
     */
    public static int executeUpdate(String dbKey, String sql, Object[] params)  {
        // 受影响的行数
        int affectedLine = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // 获得连接
            connection = getConnection(dbKey);
            // 调用SQL
            preparedStatement = connection.prepareStatement(sql);

            // 参数赋值
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }

            // 执行
            affectedLine = preparedStatement.executeUpdate();

        } catch (Exception e) {
            logger.error("db query error => {}", ExceptionUtils.getFullStackTrace(e));
            throw new BusinessException("100000","数据查询失败！");
        } finally {
            // 释放资源
            closeAll(null, preparedStatement, null, connection);
        }
        return affectedLine;
    }

    /**
     * 获取结果集，并将结果放在List中
     * @param sql SQL语句
     * @return List 结果集
     * @throws Exception
     */
    public static Map<String, Object> executeQueryMap( String sql, Object[] params)  {
        return executeQueryMap(getCurrentDatabaseKey(), sql, params);
    }

    /**
     * 获取结果集，并将结果放在List中
     * @param sql SQL语句
     * @return List 结果集
     * @throws Exception
     */
    public static Map<String, Object> executeQueryMap(String dbKey, String sql, Object[] params)  {
        List<Map<String, Object>> maps = executeQueryMaps(dbKey, sql, params);

        if(maps != null && maps.size() > 1) {
            throw new RuntimeException("execute query result not only one !");
        }
        if(maps == null || maps.size() == 0) {
            return null;
        }
        return maps.get(0);
    }

    /**
     * 获取结果集，并将结果放在List中
     * @param sql SQL语句
     * @return List 结果集
     * @throws Exception
     */
    public static List<Map<String, Object>> executeQueryMaps(String sql, Object[] params)  {
        return executeQueryMaps(getCurrentDatabaseKey(), sql, params);
    }

    /**
     * 获取返回数据结构
     * @param sql SQL语句
     * @return List 结果集
     * @throws Exception
     */
    public static Map<String, String> executeQueryStruts(String dbKey, String sql, Object[] params)  {
        logger.debug("db query => {}|{}|{}",dbKey, sql, Arrays.toString(params));

        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        // 执行SQL获得结果集
        // 创建ResultSetMetaData对象
        ResultSetMetaData rsmd = null;

        Map<String, String> map = new LinkedHashMap<>();
        try {
            // 获得连接
            connection = getConnection(dbKey);
            // 调用SQL
            preparedStatement = connection.prepareStatement(sql);

            // 参数赋值
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }

            // 执行
            resultSet = preparedStatement.executeQuery();

            rsmd = resultSet.getMetaData();
            // 结果集列数
            int columnCount  = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                map.put(rsmd.getColumnLabel(i), rsmd.getColumnTypeName(i));
            }
        } catch (Exception e) {
            logger.error("db query error => {}", ExceptionUtils.getFullStackTrace(e));
            throw new BusinessException("100000","数据查询失败！");
        } finally {
            // 释放资源
            closeAll(resultSet, preparedStatement, null, connection);
        }

        try {
            logger.debug("db query result => {}", JsonUtils.writeValueAsString(map));
        } catch (IOException e) {
            logger.debug("db query result => {}", map);
        }
        return map;
    }

    /**
     * 获取结果集，并将结果放在List中
     * @param sql SQL语句
     * @return List 结果集
     * @throws Exception
     */
    public static List<Map<String, Object>> executeQueryMaps(String dbKey, String sql, Object[] params)  {
        logger.debug("db query => {}|{}|{}",dbKey, sql, Arrays.toString(params));


        List paramList = params != null ? Lists.newArrayList(params) : null;
        sql = replaceSqlInCondition(sql, paramList);

        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        // 执行SQL获得结果集
        // 创建ResultSetMetaData对象
        ResultSetMetaData rsmd = null;
        // 结果集列数
        int columnCount = 0;

        // 创建List
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            // 获得连接
            connection = getConnection(dbKey);
            // 调用SQL
            preparedStatement = connection.prepareStatement(sql);

            // 参数赋值
            if (paramList != null) {
                for (int i = 0; i < paramList.size(); i++) {
//                    if(params[i] instanceof Collection) {
//                        Vector v = new Vector(Lists.newArrayList(((Iterable)params[i])));
////                        Array array = connection.createArrayOf("VARCHAR", Lists.newArrayList(((Iterable)params[i])).toArray(new Object[0]));
//                        preparedStatement.setObject(i + 1, v);
//                    }else {
                        preparedStatement.setObject(i + 1, paramList.get(i));
//                    }

                }
            }

            // 执行
            resultSet = preparedStatement.executeQuery();

            rsmd = resultSet.getMetaData();
            // 获得结果集列数
            columnCount = rsmd.getColumnCount();
            // 将ResultSet的结果保存到List中
            while (resultSet.next()) {
                Map<String, Object> map = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    //BUG tinyint(1) 被判定为Boolean ，但存储的是还款月1-12与还款日1-31
                    if(value != null && value instanceof Boolean) {
                        map.put(rsmd.getColumnLabel(i), resultSet.getInt(i));
                    }else {
                        map.put(rsmd.getColumnLabel(i), value);
                    }

                }
                list.add(map);
            }
        } catch (Exception e) {
            logger.error("db query error => {}", ExceptionUtils.getFullStackTrace(e));
            throw new BusinessException("100000","数据查询失败！");
        } finally {
            // 释放资源
            closeAll(resultSet, preparedStatement, null, connection);
        }

        try {
            logger.debug("db query result => {}", JsonUtils.writeValueAsString(list));
        } catch (IOException e) {
            logger.debug("db query result => {}", list);
        }
        return list;
    }

    private static String replaceSqlInCondition(String sql, List paramList) {
        if(paramList == null) return sql;

        int fromIndex = 0;
        List<Integer> removeElementIndexes = new ArrayList<>();
        for (int i = 0; i < paramList.size(); i++) {
            fromIndex = sql.indexOf("?", fromIndex);
            Object object = paramList.get(i);
            if(object != null && object.getClass().isArray()) {
                Set<Object> objects = Sets.newHashSet((Object[]) object);
                if(objects.size() > 1) {
                    if(objects.contains(null)) {//批量用户标签，走批量get_cust_tip_labels时，场景过于复杂，暂时忽略
//                        System.out.println("666 => " + objects);
                        objects.remove(null);
                    }
                    String inString = "'" + Joiner.on("', '").join(objects) + "'";
                    String startPart = sql.substring(0, fromIndex);
                    String endPart = sql.substring(fromIndex + 1);
                    if(startPart.trim().endsWith("(")){// in ( ? )
                        sql = startPart + inString  + endPart;
                    }else { //其他
                        sql = StringUtils.reverse(StringUtils.reverse(startPart).replaceFirst("[<>!=]+", " ni "))
                                + "(" + inString + ")" + endPart;
                    }

//                    paramList.remove(i);//不能直接remove，否则如果paramList.set(i, ((Object[])paramList.get(i))[0]);i的位置将会错误
                    paramList.set(i, null);
                    removeElementIndexes.add(i);
                } else  if(objects.size() == 1) {
                    fromIndex ++;
                    paramList.set(i, ((Object[])paramList.get(i))[0]);
                }else {
//                    fromIndex ++;
                    if(((Object[]) object).length == 0) {
                        if(sql.substring(0, fromIndex).matches(".*[ ]+not[ ]+in[ ]+\\([ ]*")) { //not in (?) 但是in内没有条件
                            paramList.set(i, "-1");
                        }else {
                            paramList.set(i, null);
                        }
//                        throw new BusinessException("in condition must exists one parameter !");

                    }else {
                        paramList.set(i, ((Object[]) object)[0]);
                    }
                }
            }else {
                fromIndex ++;
            }
        }
        for (int i = removeElementIndexes.size() - 1; i >= 0; i--) {
            paramList.remove((int)removeElementIndexes.get(i));
        }
//        Iterator iterator = paramList.iterator();
//        while (iterator.hasNext()) {
//            Object next = iterator.next();
//            if(next == null) {
//                iterator.remove();
//            }
//        }
        return sql;
    }

    /**
     * 获取结果集，并将结果放在List中
     * @param sql SQL语句
     * @return List 结果集
     * @throws Exception
     */
    public static List<List<Object>> executeQueryList(String sql, Object[] params)  {
        return executeQueryList(getCurrentDatabaseKey(), sql, params, false);
    }

    /**
     * 获取结果集，并将结果放在List中
     * @param sql SQL语句
     * @return List 结果集
     * @throws Exception
     */
    public static List<List<Object>> executeQueryList(String sql, Object[] params, boolean addHead)  {
        return executeQueryList(getCurrentDatabaseKey(), sql, params, addHead);
    }

    /**
     * 获取结果集，并将结果放在List中
     * @param sql SQL语句
     * @return List 结果集
     * @throws Exception
     */
    public static List<List<Object>> executeQueryList(String dbKey, String sql, Object[] params, boolean addHead)  {
        logger.debug("db query => {}|{}|{}|{}",dbKey, sql, Arrays.toString(params), addHead);
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        // 执行SQL获得结果集
        // 创建ResultSetMetaData对象
        ResultSetMetaData rsmd = null;
        // 结果集列数
        int columnCount = 0;

        // 创建List
        List<List<Object>> data = new ArrayList<>();
        try {
            // 获得连接
            connection = getConnection(dbKey);
            // 调用SQL
            preparedStatement = connection.prepareStatement(sql);

            // 参数赋值
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }

            // 执行
            resultSet = preparedStatement.executeQuery();

            rsmd = resultSet.getMetaData();
            // 获得结果集列数
            columnCount = rsmd.getColumnCount();
            if(addHead) {
                List<Object> columns = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    columns.add(rsmd.getColumnLabel(i));
                }
                data.add(columns);
            }


            // 将ResultSet的结果保存到List中
            while (resultSet.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getObject(i));
                }
                data.add(row);
            }
        } catch (Exception e) {
            logger.error("db query error => {}", ExceptionUtils.getFullStackTrace(e));
            throw new BusinessException("100000","数据查询失败！");
        } finally {
            // 释放资源
            closeAll(resultSet, preparedStatement, null, connection);
        }
        try {
            logger.debug("db query result => {}", JsonUtils.writeValueAsString(data));
        } catch (IOException e) {
            logger.debug("db query result => {}", data);
        }
        return data;
    }

//    /**
//     * SQL 查询将查询结果直接放入ResultSet中
//     * @param sql SQL语句
//     * @param params 参数数组，若没有参数则为null
//     * @return 结果集
//     * @throws Exception
//     */
//    private static ResultSet executeQueryRS(String sql, Object[] params){
//        ResultSet resultSet = null;
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//        try {
//            // 获得连接
//            connection = getConnection("as");
//            // 调用SQL
//            preparedStatement = connection.prepareStatement(sql);
//
//            // 参数赋值
//            if (params != null) {
//                for (int i = 0; i < params.length; i++) {
//                    preparedStatement.setObject(i + 1, params[i]);
//                }
//            }
//
//            // 执行
//            resultSet = preparedStatement.executeQuery();
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }finally {
//            // 释放资源
//            closeAll(resultSet, preparedStatement, null, connection);
//        }
//
//        return resultSet;
//    }

    public static class DBInfo{
        private String url;
        private String username;
        private String password;

        public DBInfo(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DBInfo dbInfo = (DBInfo) o;
            return Objects.equal(url, dbInfo.url) &&
                    Objects.equal(username, dbInfo.username) &&
                    Objects.equal(password, dbInfo.password);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(url, username, password);
        }
    }
}
