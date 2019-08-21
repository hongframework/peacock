package com.hframework.peacock.handler.base.database;

import com.google.common.base.Joiner;

public class HandlerHelper {

    public static String concatQueryPaginationSql(String tableName, String[] queryField, String[] returnField,
                                                  Object[] parameterValues, Integer pageNo, Integer pageSize){
        StringBuffer sql = concatSql(tableName, queryField, returnField, parameterValues);
        if(pageNo >= 0 && pageSize >= 0) {
            sql.append(" limit ")
                    .append((pageNo -1) * pageSize)
                    .append(", ")
                    .append(pageSize);
        }
        String sqlString = sql.toString();
        System.out.println(sqlString);
        return sqlString;
    }

    public static String concatQueryOneSql(String tableName, String[] queryField, String[] returnField, Object[] parameterValues){
        StringBuffer sql = concatSql(tableName, queryField, returnField, parameterValues);
        sql.append(" limit 1");
        String sqlString = sql.toString();
        System.out.println(sqlString);
        return sqlString;
    }

    public static StringBuffer concatSql(String tableName, String[] queryField, String[] returnField, Object[] parameterValues){
        StringBuffer sql = new StringBuffer();
        sql.append("select ");
        if(parameterValues != null && parameterValues.length > 0) {
            sql.append(Joiner.on(", ").join(returnField)).append(" ");;
        }else {
            sql.append("*").append(" ");
        }
        sql.append("from ").append(tableName).append(" ");
        if(queryField != null && queryField.length > 0 && parameterValues != null && parameterValues.length >= queryField.length) {
            sql.append("where ");
            for (int i = 0; i < queryField.length; i++) {
                if(i > 0) sql.append(" and ");
                sql.append(queryField[i]).append(" = ?");
            }
        }
        return sql;
    }

    public static void main(String[] args) {
        System.out.println(concatQueryPaginationSql("user", new String[]{"id"},new String[]{"id", "name"},new String[]{"18"}, 2, 10));
    }
}
