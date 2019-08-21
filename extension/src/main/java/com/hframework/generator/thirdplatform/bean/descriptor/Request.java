package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("request")
public class Request   {

	@XStreamAlias("parameters")
	private Parameters parameters;
	@XStreamAlias("nodes")
	private Nodes nodes;
	@XStreamAsAttribute
    @XStreamAlias("load")
	private String load;
	@XStreamAsAttribute
    @XStreamAlias("message")
	private String message;
	@XStreamAsAttribute
	@XStreamAlias("beanName")
	private String beanName;


    public Request() {
    	}
   
 
 	
	public Parameters getParameters(){
		return parameters == null ? new Parameters() : parameters;
	}

	public void setParameters(Parameters parameters){
    	this.parameters = parameters;
    }

 	
	public Nodes getNodes(){
		return nodes == null ? new Nodes() : nodes;
	}

	public void setNodes(Nodes nodes){
    	this.nodes = nodes;
    }

 	
	public String getLoad(){
		return load;
	}

	public void setLoad(String load){
    	this.load = load;
    }

 	
	public String getMessage(){
		return message;
	}

	public void setMessage(String message){
    	this.message = message;
    }

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
}
