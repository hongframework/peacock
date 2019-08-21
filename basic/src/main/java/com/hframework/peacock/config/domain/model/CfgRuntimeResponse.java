package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class CfgRuntimeResponse {
    private Long id;

    private Long programId;

    private String code;

    private String name;

    private String value;

    private Byte type;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    private String description;

    public CfgRuntimeResponse(Long id, Long programId, String code, String name, String value, Byte type, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime, String description) {
        this.id = id;
        this.programId = programId;
        this.code = code;
        this.name = name;
        this.value = value;
        this.type = type;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getProgramId() {
        return programId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Byte getType() {
        return type;
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

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public void setProgramId(Long programId) {
        this.programId=programId;
    }

    public void setCode(String code) {
        this.code=code;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setValue(String value) {
        this.value=value;
    }

    public void setType(Byte type) {
        this.type=type;
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

    public void setDescription(String description) {
        this.description=description;
    }

    public CfgRuntimeResponse() {
        super();
    }
}