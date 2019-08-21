package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("responseConfig")
public class ResponseConfig   {

	@XStreamAlias("valueMappers")
	private ValueMappers valueMappers;
	@XStreamAlias("publicNodes")
	private PublicNodes publicNodes;

    public ResponseConfig() {
    	}
   
 
 	
	public ValueMappers getValueMappers(){
		return valueMappers;
	}

	public void setValueMappers(ValueMappers valueMappers){
    	this.valueMappers = valueMappers;
    }

 	
	public PublicNodes getPublicNodes(){
		return publicNodes == null ? new PublicNodes() : publicNodes;
	}

	public void setPublicNodes(PublicNodes publicNodes){
    	this.publicNodes = publicNodes;
    }
}
