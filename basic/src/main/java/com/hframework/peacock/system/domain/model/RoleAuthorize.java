package com.hframework.peacock.system.domain.model;

import java.util.Date;

public class RoleAuthorize {
    private Long roleAuthorizeId;

    private Byte roleAuthorizeType;

    private Long roleId;

    private Long menuId;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    public RoleAuthorize(Long roleAuthorizeId, Byte roleAuthorizeType, Long roleId, Long menuId, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime) {
        this.roleAuthorizeId = roleAuthorizeId;
        this.roleAuthorizeType = roleAuthorizeType;
        this.roleId = roleId;
        this.menuId = menuId;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
    }

    public Long getRoleAuthorizeId() {
        return roleAuthorizeId;
    }

    public Byte getRoleAuthorizeType() {
        return roleAuthorizeType;
    }

    public Long getRoleId() {
        return roleId;
    }

    public Long getMenuId() {
        return menuId;
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

    public void setRoleAuthorizeId(Long roleAuthorizeId) {
        this.roleAuthorizeId=roleAuthorizeId;
    }

    public void setRoleAuthorizeType(Byte roleAuthorizeType) {
        this.roleAuthorizeType=roleAuthorizeType;
    }

    public void setRoleId(Long roleId) {
        this.roleId=roleId;
    }

    public void setMenuId(Long menuId) {
        this.menuId=menuId;
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

    public RoleAuthorize() {
        super();
    }
}