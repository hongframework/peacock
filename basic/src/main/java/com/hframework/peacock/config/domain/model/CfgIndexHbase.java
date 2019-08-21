package com.hframework.peacock.config.domain.model;

public class CfgIndexHbase {
    private Integer id;

    private Integer indexId;

    private Integer hbaseId;

    private String table;

    private String family;

    private String qualifier;

    private Byte rowkeyConverter;

    private Integer ctime;

    private Integer mtime;

    public CfgIndexHbase(Integer id, Integer indexId, Integer hbaseId, String table, String family, String qualifier, Byte rowkeyConverter, Integer ctime, Integer mtime) {
        this.id = id;
        this.indexId = indexId;
        this.hbaseId = hbaseId;
        this.table = table;
        this.family = family;
        this.qualifier = qualifier;
        this.rowkeyConverter = rowkeyConverter;
        this.ctime = ctime;
        this.mtime = mtime;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public Integer getHbaseId() {
        return hbaseId;
    }

    public String getTable() {
        return table;
    }

    public String getFamily() {
        return family;
    }

    public String getQualifier() {
        return qualifier;
    }

    public Byte getRowkeyConverter() {
        return rowkeyConverter;
    }

    public Integer getCtime() {
        return ctime;
    }

    public Integer getMtime() {
        return mtime;
    }

    public void setId(Integer id) {
        this.id=id;
    }

    public void setIndexId(Integer indexId) {
        this.indexId=indexId;
    }

    public void setHbaseId(Integer hbaseId) {
        this.hbaseId=hbaseId;
    }

    public void setTable(String table) {
        this.table=table;
    }

    public void setFamily(String family) {
        this.family=family;
    }

    public void setQualifier(String qualifier) {
        this.qualifier=qualifier;
    }

    public void setRowkeyConverter(Byte rowkeyConverter) {
        this.rowkeyConverter=rowkeyConverter;
    }

    public void setCtime(Integer ctime) {
        this.ctime=ctime;
    }

    public void setMtime(Integer mtime) {
        this.mtime=mtime;
    }

    public CfgIndexHbase() {
        super();
    }
}