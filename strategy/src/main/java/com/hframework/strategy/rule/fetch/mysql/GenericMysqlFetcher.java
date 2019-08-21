package com.hframework.strategy.rule.fetch.mysql;

import com.hframework.common.util.RegexUtils;

/**
 * Created by zhangquanhong on 2017/7/17.
 */
public  class GenericMysqlFetcher extends MysqlFetcher {

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    private String table;
    private String primaryKey;
    private String column;
    private String sql;
    private String[] parameters;

    public GenericMysqlFetcher(String host, String port, String database, String username, String password, String table, String primaryKey, String column, String sql) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.table = table;
        this.primaryKey = primaryKey;
        this.column = column;
        this.sql = sql;
        if(sql != null) {
            parameters = RegexUtils.find(sql(), "\\$[{]?[a-zA-Z]+[a-zA-Z0-9._]*[}]?");
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = parameters[i].replace("${","").replace("$","").replace("}","");
            }
            this.sql = sql.replaceAll("\\$[{]?[a-zA-Z]+[a-zA-Z0-9._]*[}]?", "?");
        }
        register();
    }

    @Override
    protected String host() {
        return host;
    }

    @Override
    protected String port() {
        return port;
    }

    @Override
    protected String database() {
        return database;
    }

    @Override
    protected String username() {
        return username;
    }

    @Override
    protected String password() {
        return password;
    }

    @Override
    protected String table() {
        return table;
    }

    @Override
    protected String primaryKey() {
        return primaryKey;
    }

    @Override
    protected String column() {
        return column;
    }

    @Override
    public String sql() {
        return sql;
    }

    public String[] sqlParameters() {
        return parameters;
    }
}
