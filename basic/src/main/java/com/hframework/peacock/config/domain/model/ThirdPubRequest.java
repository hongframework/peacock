package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class ThirdPubRequest {
    private Long id;

    private Long domainId;

    private String name;

    private String path;

    private Byte type;

    private Byte required;

    private String checkRule;

    private Date createTime;

    private Long creatorId;

    private Long modifierId;

    private Date modifyTime;

    private String value;

    public ThirdPubRequest(Long id, Long domainId, String name, String path, Byte type, Byte required, String checkRule, Date createTime, Long creatorId, Long modifierId, Date modifyTime, String value) {
        this.id = id;
        this.domainId = domainId;
        this.name = name;
        this.path = path;
        this.type = type;
        this.required = required;
        this.checkRule = checkRule;
        this.createTime = createTime;
        this.creatorId = creatorId;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
        this.value = value;
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

    public Date getCreateTime() {
        return createTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public String getValue() {
        return value;
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

    public void setCreateTime(Date createTime) {
        this.createTime=createTime;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId=creatorId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId=modifierId;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime=modifyTime;
    }

    public void setValue(String value) {
        this.value=value;
    }

    public ThirdPubRequest() {
        super();
    }
}