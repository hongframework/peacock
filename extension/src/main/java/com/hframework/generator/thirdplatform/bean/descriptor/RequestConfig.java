package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("requestConfig")
public class RequestConfig   {

	@XStreamAlias("valueMappers")
	private ValueMappers valueMappers;
	@XStreamAlias("publicParameters")
	private PublicParameters publicParameters;
	@XStreamAlias("publicNodes")
	private PublicNodes publicNodes;

    public RequestConfig() {
    	}
   
 
 	
	public ValueMappers getValueMappers(){
		return valueMappers;
	}

	public void setValueMappers(ValueMappers valueMappers){
    	this.valueMappers = valueMappers;
    }

 	
	public PublicParameters getPublicParameters(){
		return publicParameters == null ? new PublicParameters() : publicParameters;
	}

	public void setPublicParameters(PublicParameters publicParameters){
    	this.publicParameters = publicParameters;
    }

 	
	public PublicNodes getPublicNodes(){
		return publicNodes;
	}

	public void setPublicNodes(PublicNodes publicNodes){
    	this.publicNodes = publicNodes;
    }
}
