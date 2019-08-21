package com.hframework.generator.thirdplatform.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("interfaceExample")
public class InterfaceExample {

	@XStreamAlias("name")
	private String name;
	@XStreamAlias("url")
	private String url;
	@XStreamAlias("requestMessage")
	private String requestMessage;
	@XStreamAlias("responseMessage")
	private String responseMessage;

    public InterfaceExample() {
    	}
   
 
 	
	public String getName(){
		return name;
	}

	public void setName(String name){
    	this.name = name;
    }

 	
	public String getUrl(){
		return url;
	}

	public void setUrl(String url){
    	this.url = url;
    }

 	
	public String getRequestMessage(){
		return requestMessage;
	}

	public void setRequestMessage(String requestMessage){
    	this.requestMessage = requestMessage;
    }

 	
	public String getResponseMessage(){
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage){
    	this.responseMessage = responseMessage;
    }
}
