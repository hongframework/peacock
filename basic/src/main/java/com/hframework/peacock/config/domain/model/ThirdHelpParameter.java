package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class ThirdHelpParameter {
    private Long id;

    private String name;

    private String code;

    private String value;

    private Byte visiable;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    public ThirdHelpParameter(Long id, String name, String code, String value, Byte visiable, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.value = value;
        this.visiable = visiable;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public Byte getVisiable() {
        return visiable;
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

    public void setName(String name) {
        this.name=name;
    }

    public void setCode(String code) {
        this.code=code;
    }

    public void setValue(String value) {
        this.value=value;
    }

    public void setVisiable(Byte visiable) {
        this.visiable=visiable;
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

    public ThirdHelpParameter() {
        super();
    }
}