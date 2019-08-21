package com.hframework.peacock.system.domain.model;

import java.math.BigDecimal;
import java.util.Date;

public class DictionaryItem {
    private Long dictionaryItemId;

    private String value;

    private String text;

    private String desc;

    private Integer isDefault;

    private BigDecimal pri;

    private String ext1;

    private String ext2;

    private Long dictionaryId;

    private String dictionaryCode;

    private Long opId;

    private Date createTime;

    private Long modifyOpId;

    private Date modifyTime;

    private Integer delFlag;

    public DictionaryItem(Long dictionaryItemId, String value, String text, String desc, Integer isDefault, BigDecimal pri, String ext1, String ext2, Long dictionaryId, String dictionaryCode, Long opId, Date createTime, Long modifyOpId, Date modifyTime, Integer delFlag) {
        this.dictionaryItemId = dictionaryItemId;
        this.value = value;
        this.text = text;
        this.desc = desc;
        this.isDefault = isDefault;
        this.pri = pri;
        this.ext1 = ext1;
        this.ext2 = ext2;
        this.dictionaryId = dictionaryId;
        this.dictionaryCode = dictionaryCode;
        this.opId = opId;
        this.createTime = createTime;
        this.modifyOpId = modifyOpId;
        this.modifyTime = modifyTime;
        this.delFlag = delFlag;
    }

    public Long getDictionaryItemId() {
        return dictionaryItemId;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public BigDecimal getPri() {
        return pri;
    }

    public String getExt1() {
        return ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public Long getDictionaryId() {
        return dictionaryId;
    }

    public String getDictionaryCode() {
        return dictionaryCode;
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

    public void setDictionaryItemId(Long dictionaryItemId) {
        this.dictionaryItemId=dictionaryItemId;
    }

    public void setValue(String value) {
        this.value=value;
    }

    public void setText(String text) {
        this.text=text;
    }

    public void setDesc(String desc) {
        this.desc=desc;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault=isDefault;
    }

    public void setPri(BigDecimal pri) {
        this.pri=pri;
    }

    public void setExt1(String ext1) {
        this.ext1=ext1;
    }

    public void setExt2(String ext2) {
        this.ext2=ext2;
    }

    public void setDictionaryId(Long dictionaryId) {
        this.dictionaryId=dictionaryId;
    }

    public void setDictionaryCode(String dictionaryCode) {
        this.dictionaryCode=dictionaryCode;
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

    public DictionaryItem() {
        super();
    }
}