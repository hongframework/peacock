package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("global")
public class Global   {

	@XStreamAlias("staticParameters")
	private StaticParameters staticParameters;
	@XStreamAlias("properties")
	private Properties properties;
	@XStreamAlias("rules")
	private Rules rules;
	@XStreamAlias("requestConfig")
	private RequestConfig requestConfig;
	@XStreamAlias("responseConfig")
	private ResponseConfig responseConfig;

    public Global() {
    	}
   
 
 	
	public StaticParameters getStaticParameters(){
		return staticParameters;
	}

	public void setStaticParameters(StaticParameters staticParameters){
    	this.staticParameters = staticParameters;
    }

 	
	public Properties getProperties(){
		return properties;
	}

	public void setProperties(Properties properties){
    	this.properties = properties;
    }

 	
	public Rules getRules(){
		return rules;
	}

	public void setRules(Rules rules){
    	this.rules = rules;
    }

 	
	public RequestConfig getRequestConfig(){
		return requestConfig == null ? new RequestConfig() : requestConfig;
	}

	public void setRequestConfig(RequestConfig requestConfig){
    	this.requestConfig = requestConfig;
    }

 	
	public ResponseConfig getResponseConfig(){
		return responseConfig;
	}

	public void setResponseConfig(ResponseConfig responseConfig){
    	this.responseConfig = responseConfig;
    }
}
