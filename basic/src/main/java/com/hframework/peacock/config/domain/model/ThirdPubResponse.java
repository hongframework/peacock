package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class ThirdPubResponse {
    private Long id;

    private Date createTime;

    private Long creatorId;

    private Long domainId;

    private Long modifierId;

    private Date modifyTime;

    private String name;

    private String path;

    private Byte type;

    private String value;

    public ThirdPubResponse(Long id, Date createTime, Long creatorId, Long domainId, Long modifierId, Date modifyTime, String name, String path, Byte type, String value) {
        this.id = id;
        this.createTime = createTime;
        this.creatorId = creatorId;
        this.domainId = domainId;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
        this.name = name;
        this.path = path;
        this.type = type;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public Long getDomainId() {
        return domainId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public Date getModifyTime() {
        return modifyTime;
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

    public String getValue() {
        return value;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public void setCreateTime(Date createTime) {
        this.createTime=createTime;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId=creatorId;
    }

    public void setDomainId(Long domainId) {
        this.domainId=domainId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId=modifierId;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime=modifyTime;
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

    public void setValue(String value) {
        this.value=value;
    }

    public ThirdPubResponse() {
        super();
    }
}