package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("parameter")
public class Parameter   {

	@XStreamAsAttribute
    @XStreamAlias("name")
	private String name;
	@XStreamAsAttribute
    @XStreamAlias("type")
	private String type;
	@XStreamAsAttribute
    @XStreamAlias("nullable")
	private String nullable;
	@XStreamAsAttribute
    @XStreamAlias("visiable")
	private String visiable;
	@XStreamAsAttribute
    @XStreamAlias("ruleId")
	private String ruleId;
	@XStreamAsAttribute
    @XStreamAlias("value")
	private String value;

    public Parameter() {
    	}
   
 
 	
	public String getName(){
		return name;
	}

	public void setName(String name){
    	this.name = name;
    }

 	
	public String getType(){
		return type;
	}

	public void setType(String type){
    	this.type = type;
    }

 	
	public String getNullable(){
		return nullable;
	}

	public void setNullable(String nullable){
    	this.nullable = nullable;
    }

 	
	public String getVisiable(){
		return visiable;
	}

	public void setVisiable(String visiable){
    	this.visiable = visiable;
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
}
