package com.hframework.peacock.system.domain.model;

import java.util.Date;

public class Organize {
    private Long organizeId;

    private String organizeCode;

    private String organizeName;

    private Byte organizeType;

    private Byte organizeLevel;

    private Long parentOrganizeId;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    public Organize(Long organizeId, String organizeCode, String organizeName, Byte organizeType, Byte organizeLevel, Long parentOrganizeId, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime) {
        this.organizeId = organizeId;
        this.organizeCode = organizeCode;
        this.organizeName = organizeName;
        this.organizeType = organizeType;
        this.organizeLevel = organizeLevel;
        this.parentOrganizeId = parentOrganizeId;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
    }

    public Long getOrganizeId() {
        return organizeId;
    }

    public String getOrganizeCode() {
        return organizeCode;
    }

    public String getOrganizeName() {
        return organizeName;
    }

    public Byte getOrganizeType() {
        return organizeType;
    }

    public Byte getOrganizeLevel() {
        return organizeLevel;
    }

    public Long getParentOrganizeId() {
        return parentOrganizeId;
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

    public void setOrganizeId(Long organizeId) {
        this.organizeId=organizeId;
    }

    public void setOrganizeCode(String organizeCode) {
        this.organizeCode=organizeCode;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName=organizeName;
    }

    public void setOrganizeType(Byte organizeType) {
        this.organizeType=organizeType;
    }

    public void setOrganizeLevel(Byte organizeLevel) {
        this.organizeLevel=organizeLevel;
    }

    public void setParentOrganizeId(Long parentOrganizeId) {
        this.parentOrganizeId=parentOrganizeId;
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

    public Organize() {
        super();
    }
}