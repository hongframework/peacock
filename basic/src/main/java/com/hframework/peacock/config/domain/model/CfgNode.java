package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class CfgNode {
    private Long id;

    private String code;

    private String name;

    private Byte type;

    private Byte status;

    private String programId;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    private String domain;

    public CfgNode(Long id, String code, String name, Byte type, Byte status, String programId, Long creatorId, Date createTime, Long modifierId, Date modifyTime, String domain) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.type = type;
        this.status = status;
        this.programId = programId;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
        this.domain = domain;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Byte getType() {
        return type;
    }

    public Byte getStatus() {
        return status;
    }

    public String getProgramId() {
        return programId;
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

    public String getDomain() {
        return domain;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public void setCode(String code) {
        this.code=code;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setType(Byte type) {
        this.type=type;
    }

    public void setStatus(Byte status) {
        this.status=status;
    }

    public void setProgramId(String programId) {
        this.programId=programId;
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

    public void setDomain(String domain) {
        this.domain=domain;
    }

    public CfgNode() {
        super();
    }
}