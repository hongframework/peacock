package com.hframework.peacock.config.domain.model;

public class CfgDatasouceHbase {
    private Integer id;

    private String zklist;

    private Integer zkport;

    private Byte state;

    private String remark;

    private Integer ctime;

    private Integer mtime;

    public CfgDatasouceHbase(Integer id, String zklist, Integer zkport, Byte state, String remark, Integer ctime, Integer mtime) {
        this.id = id;
        this.zklist = zklist;
        this.zkport = zkport;
        this.state = state;
        this.remark = remark;
        this.ctime = ctime;
        this.mtime = mtime;
    }

    public Integer getId() {
        return id;
    }

    public String getZklist() {
        return zklist;
    }

    public Integer getZkport() {
        return zkport;
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

    public void setZklist(String zklist) {
        this.zklist=zklist;
    }

    public void setZkport(Integer zkport) {
        this.zkport=zkport;
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

    public CfgDatasouceHbase() {
        super();
    }
}