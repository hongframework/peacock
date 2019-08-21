package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class CfgRuntimeDictionary {
    private Long id;

    private Long programId;

    private String code;

    private String name;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    public CfgRuntimeDictionary(Long id, Long programId, String code, String name, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime) {
        this.id = id;
        this.programId = programId;
        this.code = code;
        this.name = name;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
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

    public void setProgramId(Long programId) {
        this.programId=programId;
    }

    public void setCode(String code) {
        this.code=code;
    }

    public void setName(String name) {
        this.name=name;
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

    public CfgRuntimeDictionary() {
        super();
    }
}