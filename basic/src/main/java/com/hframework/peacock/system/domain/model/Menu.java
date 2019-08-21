package com.hframework.peacock.system.domain.model;

import java.math.BigDecimal;
import java.util.Date;

public class Menu {
    private Long menuId;

    private String menuCode;

    private String menuName;

    private String menuDesc;

    private Integer menuLevel;

    private String icon;

    private String url;

    private Long parentMenuId;

    private BigDecimal pri;

    private Long creatorId;

    private Date createTime;

    private Long modifierId;

    private Date modifyTime;

    private Integer delFlag;

    public Menu(Long menuId, String menuCode, String menuName, String menuDesc, Integer menuLevel, String icon, String url, Long parentMenuId, BigDecimal pri, Long creatorId, Date createTime, Long modifierId, Date modifyTime, Integer delFlag) {
        this.menuId = menuId;
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuDesc = menuDesc;
        this.menuLevel = menuLevel;
        this.icon = icon;
        this.url = url;
        this.parentMenuId = parentMenuId;
        this.pri = pri;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.modifierId = modifierId;
        this.modifyTime = modifyTime;
        this.delFlag = delFlag;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public Integer getMenuLevel() {
        return menuLevel;
    }

    public String getIcon() {
        return icon;
    }

    public String getUrl() {
        return url;
    }

    public Long getParentMenuId() {
        return parentMenuId;
    }

    public BigDecimal getPri() {
        return pri;
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

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setMenuId(Long menuId) {
        this.menuId=menuId;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode=menuCode;
    }

    public void setMenuName(String menuName) {
        this.menuName=menuName;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc=menuDesc;
    }

    public void setMenuLevel(Integer menuLevel) {
        this.menuLevel=menuLevel;
    }

    public void setIcon(String icon) {
        this.icon=icon;
    }

    public void setUrl(String url) {
        this.url=url;
    }

    public void setParentMenuId(Long parentMenuId) {
        this.parentMenuId=parentMenuId;
    }

    public void setPri(BigDecimal pri) {
        this.pri=pri;
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

    public void setDelFlag(Integer delFlag) {
        this.delFlag=delFlag;
    }

    public Menu() {
        super();
    }
}