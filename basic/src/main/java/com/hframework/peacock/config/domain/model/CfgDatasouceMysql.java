package com.hframework.peacock.config.domain.model;

public class CfgDatasouceMysql {
    private Integer id;

    private String host;

    private Integer port;

    private String database;

    private String username;

    private String password;

    private String remark;

    private Byte state;

    private Integer ctime;

    private Integer mtime;

    public CfgDatasouceMysql(Integer id, String host, Integer port, String database, String username, String password, String remark, Byte state, Integer ctime, Integer mtime) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.remark = remark;
        this.state = state;
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

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRemark() {
        return remark;
    }

    public Byte getState() {
        return state;
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

    public void setDatabase(String database) {
        this.database=database;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public void setPassword(String password) {
        this.password=password;
    }

    public void setRemark(String remark) {
        this.remark=remark;
    }

    public void setState(Byte state) {
        this.state=state;
    }

    public void setCtime(Integer ctime) {
        this.ctime=ctime;
    }

    public void setMtime(Integer mtime) {
        this.mtime=mtime;
    }

    public CfgDatasouceMysql() {
        super();
    }
}