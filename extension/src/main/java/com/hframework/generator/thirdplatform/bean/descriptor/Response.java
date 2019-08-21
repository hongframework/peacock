package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("response")
public class Response   {

	@XStreamImplicit
    @XStreamAlias("node")
	private List<Node> nodeList;
	@XStreamAsAttribute
    @XStreamAlias("load")
	private String load;
	@XStreamAsAttribute
    @XStreamAlias("extend")
	private String extend;
	@XStreamAsAttribute
    @XStreamAlias("message")
	private String message;

	@XStreamAsAttribute
	@XStreamAlias("beanName")
	private String beanName;

    public Response() {
    	}
   
 
 	
	public List<Node> getNodeList(){
		return nodeList;
	}

	public void setNodeList(List<Node> nodeList){
    	this.nodeList = nodeList;
    }

 	
	public String getLoad(){
		return load;
	}

	public void setLoad(String load){
    	this.load = load;
    }

 	
	public String getExtend(){
		return extend;
	}

	public void setExtend(String extend){
    	this.extend = extend;
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
