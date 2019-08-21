package com.hframework.peacock.config.domain.model;

import java.util.Date;

public class CfgRuntimeTrace {
    private Long id;

    private Long programId;

    private String nodeId;

    private String name;

    private Byte status;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    private String content;

    public CfgRuntimeTrace(Long id, Long programId, String nodeId, String name, Byte status, Long creatorId, Date createTime, Long modifierId, Date modifyTime, String content) {
        this.id = id;
        this.programId = programId;
        this.nodeId = nodeId;
        this.name = name;
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

    public Long getProgramId() {
        return programId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getName() {
        return name;
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

    public void setProgramId(Long programId) {
        this.programId=programId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId=nodeId;
    }

    public void setName(String name) {
        this.name=name;
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

    public CfgRuntimeTrace() {
        super();
    }
}