package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class ThirdApi {
    private Long id;

    private Long domainId;

    private Byte apiType;

    private String name;

    private String path;

    private String method;

    private String tags;

    private String requestType;

    private String responseType;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    private String content;

    public ThirdApi(Long id, Long domainId, Byte apiType, String name, String path, String method, String tags, String requestType, String responseType, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime, String content) {
        this.id = id;
        this.domainId = domainId;
        this.apiType = apiType;
        this.name = name;
        this.path = path;
        this.method = method;
        this.tags = tags;
        this.requestType = requestType;
        this.responseType = responseType;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public Long getDomainId() {
        return domainId;
    }

    public Byte getApiType() {
        return apiType;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public String getTags() {
        return tags;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getResponseType() {
        return responseType;
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

    public String getContent() {
        return content;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public void setDomainId(Long domainId) {
        this.domainId=domainId;
    }

    public void setApiType(Byte apiType) {
        this.apiType=apiType;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setPath(String path) {
        this.path=path;
    }

    public void setMethod(String method) {
        this.method=method;
    }

    public void setTags(String tags) {
        this.tags=tags;
    }

    public void setRequestType(String requestType) {
        this.requestType=requestType;
    }

    public void setResponseType(String responseType) {
        this.responseType=responseType;
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

    public void setContent(String content) {
        this.content=content;
    }

    public ThirdApi() {
        super();
    }
}