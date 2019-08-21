package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamAlias("rule")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"text"})
public class Rule   {

	@XStreamAsAttribute
    @XStreamAlias("id")
	private String id;
	@XStreamAsAttribute
    @XStreamAlias("type")
	private String type;
	
	private String text;

    public Rule() {
    	}
   
 
 	
	public String getId(){
		return id;
	}

	public void setId(String id){
    	this.id = id;
    }

 	
	public String getType(){
		return type;
	}

	public void setType(String type){
    	this.type = type;
    }

 	
	public String getText(){
		return text;
	}

	public void setText(String text){
    	this.text = text;
    }
}
