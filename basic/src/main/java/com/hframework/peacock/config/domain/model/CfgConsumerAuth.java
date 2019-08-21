package com.hframework.peacock.config.domain.model;

public class CfgConsumerAuth {
    private Long id;

    private String clientId;

    private String authPath;

    private String remark;

    private Integer ctime;

    private Integer mtime;

    private Byte state;

    public CfgConsumerAuth(Long id, String clientId, String authPath, String remark, Integer ctime, Integer mtime, Byte state) {
        this.id = id;
        this.clientId = clientId;
        this.authPath = authPath;
        this.remark = remark;
        this.ctime = ctime;
        this.mtime = mtime;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAuthPath() {
        return authPath;
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

    public Byte getState() {
        return state;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public void setClientId(String clientId) {
        this.clientId=clientId;
    }

    public void setAuthPath(String authPath) {
        this.authPath=authPath;
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

    public void setState(Byte state) {
        this.state=state;
    }

    public CfgConsumerAuth() {
        super();
    }
}