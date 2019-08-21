package com.hframework.peacock.config.domain.model;

public class CfgFeature {
    private Integer id;

    private String name;

    private Byte type;

    private String remark;

    private Integer ctime;

    private Integer mtime;

    private Byte state;

    public CfgFeature(Integer id, String name, Byte type, String remark, Integer ctime, Integer mtime, Byte state) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.remark = remark;
        this.ctime = ctime;
        this.mtime = mtime;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Byte getType() {
        return type;
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

    public void setId(Integer id) {
        this.id=id;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setType(Byte type) {
        this.type=type;
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

    public CfgFeature() {
        super();
    }
}