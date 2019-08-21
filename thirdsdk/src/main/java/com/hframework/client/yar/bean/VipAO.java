package com.hframework.client.yar.bean;

/**
 * Created by weizhenkun on 2017/2/28.
 */
public class VipAO {

    private String imgUrl;
    private String gradeName;
    private String firstDesc;
    private String secondDesc;
    private int  grade;
    private int  point;

    public String getFirstDesc() {
        return firstDesc;
    }

    public void setFirstDesc(String firstDesc) {
        this.firstDesc = firstDesc;
    }

    public String getSecondDesc() {
        return secondDesc;
    }

    public void setSecondDesc(String secondDesc) {
        this.secondDesc = secondDesc;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}


