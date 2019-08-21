package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("properties")
public class Properties   {

	@XStreamAlias("javaPackage")
	private String javaPackage;
	@XStreamAlias("clientName")
	private String clientName;
	@XStreamAlias("resourceFolder")
	private String resourceFolder;
	@XStreamAlias("beanOrganizeInterfaceBase")
	private String beanOrganizeInterfaceBase;

    public Properties() {
    	}
   
 
 	
	public String getJavaPackage(){
		return javaPackage;
	}

	public void setJavaPackage(String javaPackage){
    	this.javaPackage = javaPackage;
    }

 	
	public String getClientName(){
		return clientName;
	}

	public void setClientName(String clientName){
    	this.clientName = clientName;
    }

 	
	public String getResourceFolder(){
		return resourceFolder;
	}

	public void setResourceFolder(String resourceFolder){
    	this.resourceFolder = resourceFolder;
    }

 	
	public String getBeanOrganizeInterfaceBase(){
		return beanOrganizeInterfaceBase;
	}

	public void setBeanOrganizeInterfaceBase(String beanOrganizeInterfaceBase){
    	this.beanOrganizeInterfaceBase = beanOrganizeInterfaceBase;
    }
}
