package com.hframework.peacock.config.domain.model;

public class CfgConsumer {
    private Long id;

    private String clientId;

    private String secretKey;

    private String remark;

    private Integer ctime;

    private Integer mtime;

    private Byte state;

    public CfgConsumer(Long id, String clientId, String secretKey, String remark, Integer ctime, Integer mtime, Byte state) {
        this.id = id;
        this.clientId = clientId;
        this.secretKey = secretKey;
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

    public String getSecretKey() {
        return secretKey;
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

    public void setSecretKey(String secretKey) {
        this.secretKey=secretKey;
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

    public CfgConsumer() {
        super();
    }
}