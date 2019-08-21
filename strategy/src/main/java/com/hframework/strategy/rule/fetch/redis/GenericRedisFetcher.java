package com.hframework.strategy.rule.fetch.redis;

import com.hframework.strategy.index.repository.converter.RowKeyConverter;

/**
 * Created by zhangquanhong on 2017/7/17.
 */
public  class GenericRedisFetcher extends RedisFetcher{

    private String host;
    private int port;
    private String auth;
    private int database;
    private RowKeyConverter rowKeyConverter;
    private String method;
    private String dataType;

    public GenericRedisFetcher(String host, int port, String auth, int database, RowKeyConverter rowKeyConverter, String method, String dataType) {
        this.host = host;
        this.port = port;
        this.auth = auth;
        this.database = database;
        this.rowKeyConverter = rowKeyConverter;
        this.method = method;
        this.dataType = dataType;
        register();
    }

    @Override
    protected String host() {
        return host;
    }

    @Override
    protected int port() {
        return port;
    }

    @Override
    protected String auth() {
        return auth;
    }

    @Override
    protected int database() {
        return database;
    }

    @Override
    protected RowKeyConverter rowKeyConverter() {
        return rowKeyConverter;
    }

    @Override
    protected String method() {
        return method;
    }

    @Override
    protected String dataType() {
        return dataType;
    }
}
