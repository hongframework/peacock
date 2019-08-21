package com.hframework.peacock.system.domain.model;

import java.util.Date;

public class UserAuthorize {
    private Long userAuthorizeId;

    private Long userId;

    private Long organizeId;

    private Long roleId;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    public UserAuthorize(Long userAuthorizeId, Long userId, Long organizeId, Long roleId, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime) {
        this.userAuthorizeId = userAuthorizeId;
        this.userId = userId;
        this.organizeId = organizeId;
        this.roleId = roleId;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
    }

    public Long getUserAuthorizeId() {
        return userAuthorizeId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOrganizeId() {
        return organizeId;
    }

    public Long getRoleId() {
        return roleId;
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

    public void setUserAuthorizeId(Long userAuthorizeId) {
        this.userAuthorizeId=userAuthorizeId;
    }

    public void setUserId(Long userId) {
        this.userId=userId;
    }

    public void setOrganizeId(Long organizeId) {
        this.organizeId=organizeId;
    }

    public void setRoleId(Long roleId) {
        this.roleId=roleId;
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

    public UserAuthorize() {
        super();
    }
}