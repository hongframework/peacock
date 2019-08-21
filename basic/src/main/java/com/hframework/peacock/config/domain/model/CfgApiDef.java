package com.hframework.peacock.config.domain.model;

public class CfgApiDef {
    private Long id;

    private String path;

    private String name;

    private String version;

    private String propKey;

    private String propType;

    private String propOptions;

    private String propDescription;

    private String remark;

    private Integer ctime;

    private Integer mtime;

    public CfgApiDef(Long id, String path, String name, String version, String propKey, String propType, String propOptions, String propDescription, String remark, Integer ctime, Integer mtime) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.version = version;
        this.propKey = propKey;
        this.propType = propType;
        this.propOptions = propOptions;
        this.propDescription = propDescription;
        this.remark = remark;
        this.ctime = ctime;
        this.mtime = mtime;
    }

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getPropKey() {
        return propKey;
    }

    public String getPropType() {
        return propType;
    }

    public String getPropOptions() {
        return propOptions;
    }

    public String getPropDescription() {
        return propDescription;
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

    public void setId(Long id) {
        this.id=id;
    }

    public void setPath(String path) {
        this.path=path;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setVersion(String version) {
        this.version=version;
    }

    public void setPropKey(String propKey) {
        this.propKey=propKey;
    }

    public void setPropType(String propType) {
        this.propType=propType;
    }

    public void setPropOptions(String propOptions) {
        this.propOptions=propOptions;
    }

    public void setPropDescription(String propDescription) {
        this.propDescription=propDescription;
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

    public CfgApiDef() {
        super();
    }
}