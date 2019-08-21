package com.hframework.peacock.config.domain.model;

public class CfgIndexMysql {
    private Integer id;

    private Integer indexId;

    private Integer mysqlId;

    private String table;

    private String primaryKey;

    private String column;

    private Integer ctime;

    private Integer mtime;

    private String sql;

    public CfgIndexMysql(Integer id, Integer indexId, Integer mysqlId, String table, String primaryKey, String column, Integer ctime, Integer mtime, String sql) {
        this.id = id;
        this.indexId = indexId;
        this.mysqlId = mysqlId;
        this.table = table;
        this.primaryKey = primaryKey;
        this.column = column;
        this.ctime = ctime;
        this.mtime = mtime;
        this.sql = sql;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public Integer getMysqlId() {
        return mysqlId;
    }

    public String getTable() {
        return table;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public String getColumn() {
        return column;
    }

    public Integer getCtime() {
        return ctime;
    }

    public Integer getMtime() {
        return mtime;
    }

    public String getSql() {
        return sql;
    }

    public void setId(Integer id) {
        this.id=id;
    }

    public void setIndexId(Integer indexId) {
        this.indexId=indexId;
    }

    public void setMysqlId(Integer mysqlId) {
        this.mysqlId=mysqlId;
    }

    public void setTable(String table) {
        this.table=table;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey=primaryKey;
    }

    public void setColumn(String column) {
        this.column=column;
    }

    public void setCtime(Integer ctime) {
        this.ctime=ctime;
    }

    public void setMtime(Integer mtime) {
        this.mtime=mtime;
    }

    public void setSql(String sql) {
        this.sql=sql;
    }

    public CfgIndexMysql() {
        super();
    }
}