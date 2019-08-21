package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class CfgStaticExpanderParameter {
    private Long id;

    private String name;

    private String value;

    private String description;

    private Long expanderId;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    public CfgStaticExpanderParameter(Long id, String name, String value, String description, Long expanderId, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.description = description;
        this.expanderId = expanderId;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public Long getExpanderId() {
        return expanderId;
    }

    public Byte getStatus() {
        return status;
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

    public void setName(String name) {
        this.name=name;
    }

    public void setValue(String value) {
        this.value=value;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public void setExpanderId(Long expanderId) {
        this.expanderId=expanderId;
    }

    public void setStatus(Byte status) {
        this.status=status;
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

    public CfgStaticExpanderParameter() {
        super();
    }
}