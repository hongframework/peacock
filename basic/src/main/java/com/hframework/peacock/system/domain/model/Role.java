package com.hframework.peacock.system.domain.model;

import java.util.Date;

public class Role {
    private Long roleId;

    private String roleCode;

    private String roleName;

    private Byte roleType;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    public Role(Long roleId, String roleCode, String roleName, Byte roleType, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime) {
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.roleType = roleType;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public Byte getRoleType() {
        return roleType;
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

    public void setRoleId(Long roleId) {
        this.roleId=roleId;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode=roleCode;
    }

    public void setRoleName(String roleName) {
        this.roleName=roleName;
    }

    public void setRoleType(Byte roleType) {
        this.roleType=roleType;
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

    public Role() {
        super();
    }
}