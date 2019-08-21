package com.hframework.peacock.controller.bean.apiconf;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * generated by hframework on 2017.
 */@XStreamAlias("parameters")
public class Parameters   {

	@XStreamImplicit
    @XStreamAlias("parameter")
	private List<Parameter> parameterList;

    public Parameters() {
    }
   
 	 	 
     public List<Parameter> getParameterList(){
     	return parameterList;
     }

     public void setParameterList(List<Parameter> parameterList){
     	this.parameterList = parameterList;
     }
	 
}