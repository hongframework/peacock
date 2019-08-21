package com.hframework.peacock.config.domain.model;

public class CfgIndex {
    private Integer id;

    private String code;

    private String name;

    private String remark;

    private String features;

    private Integer ctime;

    private Integer mtime;

    private Byte state;

    private String editFeatures;

    public CfgIndex(Integer id, String code, String name, String remark, String features, Integer ctime, Integer mtime, Byte state, String editFeatures) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.remark = remark;
        this.features = features;
        this.ctime = ctime;
        this.mtime = mtime;
        this.state = state;
        this.editFeatures = editFeatures;
    }

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getRemark() {
        return remark;
    }

    public String getFeatures() {
        return features;
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

    public String getEditFeatures() {
        return editFeatures;
    }

    public void setId(Integer id) {
        this.id=id;
    }

    public void setCode(String code) {
        this.code=code;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setRemark(String remark) {
        this.remark=remark;
    }

    public void setFeatures(String features) {
        this.features=features;
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

    public void setEditFeatures(String editFeatures) {
        this.editFeatures=editFeatures;
    }

    public CfgIndex() {
        super();
    }
}