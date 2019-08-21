package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class CfgTestCase {
    private Long id;

    private String name;

    private String path;

    private String parameterStr;

    private String requestBody;

    private String responseBody;

    private String method;

    private Byte isStore;

    private Byte isPub;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    private String description;

    public CfgTestCase(Long id, String name, String path, String parameterStr, String requestBody, String responseBody, String method, Byte isStore, Byte isPub, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime, String description) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.parameterStr = parameterStr;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.method = method;
        this.isStore = isStore;
        this.isPub = isPub;
        this.status = status;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getParameterStr() {
        return parameterStr;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getMethod() {
        return method;
    }

    public Byte getIsStore() {
        return isStore;
    }

    public Byte getIsPub() {
        return isPub;
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

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setPath(String path) {
        this.path=path;
    }

    public void setParameterStr(String parameterStr) {
        this.parameterStr=parameterStr;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody=requestBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody=responseBody;
    }

    public void setMethod(String method) {
        this.method=method;
    }

    public void setIsStore(Byte isStore) {
        this.isStore=isStore;
    }

    public void setIsPub(Byte isPub) {
        this.isPub=isPub;
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

    public void setDescription(String description) {
        this.description=description;
    }

    public CfgTestCase() {
        super();
    }
}