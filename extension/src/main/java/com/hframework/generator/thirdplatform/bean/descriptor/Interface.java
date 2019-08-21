package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("interface")
public class Interface   {

	@XStreamAlias("request")
	private Request request;
	@XStreamAlias("response")
	private Response response;
	@XStreamAsAttribute
    @XStreamAlias("path")
	private String path;
	@XStreamAsAttribute
    @XStreamAlias("name")
	private String name;
	@XStreamAsAttribute
    @XStreamAlias("method")
	private String method;
	@XStreamAsAttribute
	@XStreamAlias("template")
	private String template;

	@XStreamAsAttribute
    @XStreamAlias("usePublicParams")
	private String usePublicParams;

    public Interface() {
    	}
   
 
 	
	public Request getRequest(){
		return request == null ? new Request() : request;
	}

	public void setRequest(Request request){
    	this.request = request;
    }

 	
	public Response getResponse(){
		return response == null ? new Response() : response;
	}

	public void setResponse(Response response){
    	this.response = response;
    }

 	
	public String getPath(){
		return path;
	}

	public void setPath(String path){
    	this.path = path;
    }

 	
	public String getName(){
		return name;
	}

	public void setName(String name){
    	this.name = name;
    }

 	
	public String getMethod(){
		return method;
	}

	public void setMethod(String method){
    	this.method = method;
    }

 	
	public String getUsePublicParams(){
		return usePublicParams;
	}

	public void setUsePublicParams(String usePublicParams){
    	this.usePublicParams = usePublicParams;
    }

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
}
