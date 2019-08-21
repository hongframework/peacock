package com.hframework.peacock.controller.bean.apiconf;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.hframework.peacock.controller.xstream.ext.XStreamCDATA;

public class Example {
    @XStreamCDATA
    @XStreamAlias("parameters")
    private String parameters;
    @XStreamCDATA
    @XStreamAlias("request")
    private String request;
    @XStreamCDATA
    @XStreamAlias("response")
    private String response;

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
