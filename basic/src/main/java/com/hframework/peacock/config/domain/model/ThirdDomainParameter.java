package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class ThirdDomainParameter {
    private Long id;

    private Long domainId;

    private String name;

    private String code;

    private String value;

    private Byte visiable;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    public ThirdDomainParameter(Long id, Long domainId, String name, String code, String value, Byte visiable, Long creatorId, Date createTime, Long modifierId, Date modifyTime) {
        this.id = id;
        this.domainId = domainId;
        this.name = name;
        this.code = code;
        this.value = value;
        this.visiable = visiable;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
    }

    public Long getId() {
        return id;
    }

    public Long getDomainId() {
        return domainId;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public Byte getVisiable() {
        return visiable;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public void setDomainId(Long domainId) {
        this.domainId=domainId;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setCode(String code) {
        this.code=code;
    }

    public void setValue(String value) {
        this.value=value;
    }

    public void setVisiable(Byte visiable) {
        this.visiable=visiable;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId=creatorId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime=createTime;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId=modifierId;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime=modifyTime;
    }

    public ThirdDomainParameter() {
        super();
    }
}