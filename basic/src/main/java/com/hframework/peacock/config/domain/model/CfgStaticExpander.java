package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class CfgStaticExpander {
    private Long id;

    private String type;

    private String name;

    private String triggerDataType;

    private String expanderClass;

    private String description;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    public CfgStaticExpander(Long id, String type, String name, String triggerDataType, String expanderClass, String description, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.triggerDataType = triggerDataType;
        this.expanderClass = expanderClass;
        this.description = description;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getTriggerDataType() {
        return triggerDataType;
    }

    public String getExpanderClass() {
        return expanderClass;
    }

    public String getDescription() {
        return description;
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

    public void setType(String type) {
        this.type=type;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setTriggerDataType(String triggerDataType) {
        this.triggerDataType=triggerDataType;
    }

    public void setExpanderClass(String expanderClass) {
        this.expanderClass=expanderClass;
    }

    public void setDescription(String description) {
        this.description=description;
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

    public CfgStaticExpander() {
        super();
    }
}