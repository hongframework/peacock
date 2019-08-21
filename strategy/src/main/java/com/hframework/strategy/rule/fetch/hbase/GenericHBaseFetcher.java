package com.hframework.strategy.rule.fetch.hbase;

import com.hframework.strategy.index.repository.converter.RowKeyConverter;

/**
 * Created by zhangquanhong on 2017/7/17.
 */
public class GenericHBaseFetcher extends HBaseFetcher {

    private String zkList;
    private String zkPort;
    private String table;
    private String family;
    private RowKeyConverter rowKeyConverter;
    private String keyQualifier;

    public GenericHBaseFetcher(String zkList, String zkPort, String table, String family, RowKeyConverter rowKeyConverter, String keyQualifier){
        this.zkList = zkList;
        this.zkPort = zkPort;
        this.table = table;
        this.family = family;
        this.rowKeyConverter = rowKeyConverter;
        this.keyQualifier = keyQualifier;
    }

    @Override
    protected String zkList() {
        return zkList;
    }

    @Override
    protected String zkPort() {
        return zkPort;
    }

    @Override
    protected String tableName() {
        return table;
    }

    @Override
    protected String familyName() {
        return family;
    }

    @Override
    protected RowKeyConverter rowKeyConverter() {
        return rowKeyConverter;
    }

    @Override
    protected String keyQualifier() {
        return keyQualifier;
    }
}
