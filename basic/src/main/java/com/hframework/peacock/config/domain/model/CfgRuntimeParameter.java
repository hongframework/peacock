package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class CfgRuntimeParameter {
    private Long id;

    private Long programId;

    private String name;

    private String type;

    private String description;

    private String defaultVal;

    private Long minVal;

    private Long maxVal;

    private Byte required;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    private String expanders;

    public CfgRuntimeParameter(Long id, Long programId, String name, String type, String description, String defaultVal, Long minVal, Long maxVal, Byte required, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime, String expanders) {
        this.id = id;
        this.programId = programId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.defaultVal = defaultVal;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.required = required;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
        this.expanders = expanders;
    }

    public Long getId() {
        return id;
    }

    public Long getProgramId() {
        return programId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public Long getMinVal() {
        return minVal;
    }

    public Long getMaxVal() {
        return maxVal;
    }

    public Byte getRequired() {
        return required;
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

    public String getExpanders() {
        return expanders;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public void setProgramId(Long programId) {
        this.programId=programId;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setType(String type) {
        this.type=type;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal=defaultVal;
    }

    public void setMinVal(Long minVal) {
        this.minVal=minVal;
    }

    public void setMaxVal(Long maxVal) {
        this.maxVal=maxVal;
    }

    public void setRequired(Byte required) {
        this.required=required;
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

    public void setExpanders(String expanders) {
        this.expanders=expanders;
    }

    public CfgRuntimeParameter() {
        super();
    }
}