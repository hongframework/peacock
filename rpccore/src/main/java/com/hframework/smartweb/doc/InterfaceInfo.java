package com.hframework.smartweb.doc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangquanhong on 2017/6/14.
 */
public class InterfaceInfo {
    private String url;
    private String name;
    private String description;
    private String version;

    private List<ParameterInfo> publicInfos;
    private List<ParameterInfo> parameterInfos;
    private List<ParameterInfo> staticInfos;


    public void addPublicParameter(ParameterInfo parameterInfo) {
        if(publicInfos ==null)
        publicInfos = new ArrayList<>();
        publicInfos.add(parameterInfo);
    }

    public void addBusinessParameter(ParameterInfo parameterInfo) {
        if(parameterInfos ==null)
        parameterInfos = new ArrayList<>();
        parameterInfos.add(parameterInfo);
    }

    public void addStaticParameter(ParameterInfo parameterInfo) {
        if(staticInfos ==null)
        staticInfos = new ArrayList<>();
        staticInfos.add(parameterInfo);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<ParameterInfo> getPublicInfos() {
        return publicInfos;
    }

    public void setPublicInfos(List<ParameterInfo> publicInfos) {
        this.publicInfos = publicInfos;
    }

    public List<ParameterInfo> getParameterInfos() {
        return parameterInfos;
    }

    public void setParameterInfos(List<ParameterInfo> parameterInfos) {
        this.parameterInfos = parameterInfos;
    }

    public List<ParameterInfo> getStaticInfos() {
        return staticInfos;
    }

    public void setStaticInfos(List<ParameterInfo> staticInfos) {
        this.staticInfos = staticInfos;
    }
}
