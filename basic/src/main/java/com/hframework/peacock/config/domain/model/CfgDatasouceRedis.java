package com.hframework.peacock.config.domain.model;

public class CfgDatasouceRedis {
    private Integer id;

    private String host;

    private Integer port;

    private String auth;

    private String db;

    private Byte state;

    private String remark;

    private Integer ctime;

    private Integer mtime;

    public CfgDatasouceRedis(Integer id, String host, Integer port, String auth, String db, Byte state, String remark, Integer ctime, Integer mtime) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.auth = auth;
        this.db = db;
        this.state = state;
        this.remark = remark;
        this.ctime = ctime;
        this.mtime = mtime;
    }

    public Integer getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getAuth() {
        return auth;
    }

    public String getDb() {
        return db;
    }

    public Byte getState() {
        return state;
    }

    public String getRemark() {
        return remark;
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

    public void setHost(String host) {
        this.host=host;
    }

    public void setPort(Integer port) {
        this.port=port;
    }

    public void setAuth(String auth) {
        this.auth=auth;
    }

    public void setDb(String db) {
        this.db=db;
    }

    public void setState(Byte state) {
        this.state=state;
    }

    public void setRemark(String remark) {
        this.remark=remark;
    }

    public void setCtime(Integer ctime) {
        this.ctime=ctime;
    }

    public void setMtime(Integer mtime) {
        this.mtime=mtime;
    }

    public CfgDatasouceRedis() {
        super();
    }
}