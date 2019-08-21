package com.hframework.peacock.system.domain.model;

import java.util.Date;

public class Dictionary {
    private Long dictionaryId;

    private String dictionaryName;

    private String dictionaryCode;

    private String dictionaryDesc;

    private String ext1;

    private String ext2;

    private Long opId;

    private Date createTime;

    private Long modifyOpId;

    private Date modifyTime;

    private Integer delFlag;

    public Dictionary(Long dictionaryId, String dictionaryName, String dictionaryCode, String dictionaryDesc, String ext1, String ext2, Long opId, Date createTime, Long modifyOpId, Date modifyTime, Integer delFlag) {
        this.dictionaryId = dictionaryId;
        this.dictionaryName = dictionaryName;
        this.dictionaryCode = dictionaryCode;
        this.dictionaryDesc = dictionaryDesc;
        this.ext1 = ext1;
        this.ext2 = ext2;
        this.opId = opId;
        this.createTime = createTime;
        this.modifyOpId = modifyOpId;
        this.modifyTime = modifyTime;
        this.delFlag = delFlag;
    }

    public Long getDictionaryId() {
        return dictionaryId;
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public String getDictionaryCode() {
        return dictionaryCode;
    }

    public String getDictionaryDesc() {
        return dictionaryDesc;
    }

    public String getExt1() {
        return ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public Long getOpId() {
        return opId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Long getModifyOpId() {
        return modifyOpId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDictionaryId(Long dictionaryId) {
        this.dictionaryId=dictionaryId;
    }

    public void setDictionaryName(String dictionaryName) {
        this.dictionaryName=dictionaryName;
    }

    public void setDictionaryCode(String dictionaryCode) {
        this.dictionaryCode=dictionaryCode;
    }

    public void setDictionaryDesc(String dictionaryDesc) {
        this.dictionaryDesc=dictionaryDesc;
    }

    public void setExt1(String ext1) {
        this.ext1=ext1;
    }

    public void setExt2(String ext2) {
        this.ext2=ext2;
    }

    public void setOpId(Long opId) {
        this.opId=opId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime=createTime;
    }

    public void setModifyOpId(Long modifyOpId) {
        this.modifyOpId=modifyOpId;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime=modifyTime;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag=delFlag;
    }

    public Dictionary() {
        super();
    }
}