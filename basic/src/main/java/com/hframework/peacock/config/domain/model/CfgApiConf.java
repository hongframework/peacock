package com.hframework.peacock.config.domain.model;

public class CfgApiConf {
    private Long id;

    private String path;

    private String version;

    private String propKey;

    private String remark;

    private Integer ctime;

    private Integer mtime;

    private Integer validTime;

    private Integer invalidTime;

    private Byte state;

    private Integer invalidLetime;

    private String propValue;

    public CfgApiConf(Long id, String path, String version, String propKey, String remark, Integer ctime, Integer mtime, Integer validTime, Integer invalidTime, Byte state, Integer invalidLetime, String propValue) {
        this.id = id;
        this.path = path;
        this.version = version;
        this.propKey = propKey;
        this.remark = remark;
        this.ctime = ctime;
        this.mtime = mtime;
        this.validTime = validTime;
        this.invalidTime = invalidTime;
        this.state = state;
        this.invalidLetime = invalidLetime;
        this.propValue = propValue;
    }

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getPropKey() {
        return propKey;
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

    public Integer getValidTime() {
        return validTime;
    }

    public Integer getInvalidTime() {
        return invalidTime;
    }

    public Byte getState() {
        return state;
    }

    public Integer getInvalidLetime() {
        return invalidLetime;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public void setPath(String path) {
        this.path=path;
    }

    public void setVersion(String version) {
        this.version=version;
    }

    public void setPropKey(String propKey) {
        this.propKey=propKey;
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

    public void setValidTime(Integer validTime) {
        this.validTime=validTime;
    }

    public void setInvalidTime(Integer invalidTime) {
        this.invalidTime=invalidTime;
    }

    public void setState(Byte state) {
        this.state=state;
    }

    public void setInvalidLetime(Integer invalidLetime) {
        this.invalidLetime=invalidLetime;
    }

    public void setPropValue(String propValue) {
        this.propValue=propValue;
    }

    public CfgApiConf() {
        super();
    }
}