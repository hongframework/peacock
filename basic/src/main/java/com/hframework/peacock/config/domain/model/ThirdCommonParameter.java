package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class ThirdCommonParameter {
    private Long id;

    private Long domainId;

    private String name;

    private String path;

    private Byte type;

    private Byte required;

    private String checkRule;

    private String value;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    public ThirdCommonParameter(Long id, Long domainId, String name, String path, Byte type, Byte required, String checkRule, String value, Long creatorId, Date createTime, Long modifierId, Date modifyTime) {
        this.id = id;
        this.domainId = domainId;
        this.name = name;
        this.path = path;
        this.type = type;
        this.required = required;
        this.checkRule = checkRule;
        this.value = value;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
    }

    public Long getId() {
        return id;
    }

    public Long getDomainId() {
        return domainId;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Byte getType() {
        return type;
    }

    public Byte getRequired() {
        return required;
    }

    public String getCheckRule() {
        return checkRule;
    }

    public String getValue() {
        return value;
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

    public void setDomainId(Long domainId) {
        this.domainId=domainId;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setPath(String path) {
        this.path=path;
    }

    public void setType(Byte type) {
        this.type=type;
    }

    public void setRequired(Byte required) {
        this.required=required;
    }

    public void setCheckRule(String checkRule) {
        this.checkRule=checkRule;
    }

    public void setValue(String value) {
        this.value=value;
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

    public ThirdCommonParameter() {
        super();
    }
}