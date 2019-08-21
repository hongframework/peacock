package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class CfgMgrVersion {
    private Long id;

    private String code;

    private String description;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    private Long programId;

    public CfgMgrVersion(Long id, String code, String description, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime, Long programId) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
        this.programId = programId;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
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

    public Long getProgramId() {
        return programId;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public void setCode(String code) {
        this.code=code;
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

    public void setProgramId(Long programId) {
        this.programId=programId;
    }

    public CfgMgrVersion() {
        super();
    }
}