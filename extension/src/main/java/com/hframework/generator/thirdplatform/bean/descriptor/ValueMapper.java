package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("valueMapper")
public class ValueMapper   {

	@XStreamAsAttribute
    @XStreamAlias("name")
	private String name;
	@XStreamAsAttribute
    @XStreamAlias("ruleId")
	private String ruleId;
	@XStreamAsAttribute
    @XStreamAlias("value")
	private String value;
	@XStreamAsAttribute
    @XStreamAlias("path")
	private String path;

    public ValueMapper() {
    	}
   
 
 	
	public String getName(){
		return name;
	}

	public void setName(String name){
    	this.name = name;
    }

 	
	public String getRuleId(){
		return ruleId;
	}

	public void setRuleId(String ruleId){
    	this.ruleId = ruleId;
    }

 	
	public String getValue(){
		return value;
	}

	public void setValue(String value){
    	this.value = value;
    }

 	
	public String getPath(){
		return path;
	}

	public void setPath(String path){
    	this.path = path;
    }
}
