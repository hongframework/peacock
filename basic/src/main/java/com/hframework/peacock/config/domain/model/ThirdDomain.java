package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class ThirdDomain {
    private Long id;

    private String name;

    private String url;

    private String description;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    private String protocol;

    public ThirdDomain(Long id, String name, String url, String description, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime, String protocol) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.description = description;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
        this.protocol = protocol;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
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

    public String getProtocol() {
        return protocol;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setUrl(String url) {
        this.url=url;
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

    public void setProtocol(String protocol) {
        this.protocol=protocol;
    }

    public ThirdDomain() {
        super();
    }
}