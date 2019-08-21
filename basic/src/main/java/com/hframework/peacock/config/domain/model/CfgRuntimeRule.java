package com.hframework.peacock.config.domain.model;

public class CfgRuntimeRule {
    private Integer id;

    private String code;

    private String name;

    private String version;

    private String description;

    private Byte returnType;

    private Integer features;

    private Integer ctime;

    private Integer mtime;

    private Byte state;

    private String expresssion;

    public CfgRuntimeRule(Integer id, String code, String name, String version, String description, Byte returnType, Integer features, Integer ctime, Integer mtime, Byte state, String expresssion) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.version = version;
        this.description = description;
        this.returnType = returnType;
        this.features = features;
        this.ctime = ctime;
        this.mtime = mtime;
        this.state = state;
        this.expresssion = expresssion;
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

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public Byte getReturnType() {
        return returnType;
    }

    public Integer getFeatures() {
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

    public String getExpresssion() {
        return expresssion;
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

    public void setVersion(String version) {
        this.version=version;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public void setReturnType(Byte returnType) {
        this.returnType=returnType;
    }

    public void setFeatures(Integer features) {
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

    public void setExpresssion(String expresssion) {
        this.expresssion=expresssion;
    }

    public CfgRuntimeRule() {
        super();
    }
}