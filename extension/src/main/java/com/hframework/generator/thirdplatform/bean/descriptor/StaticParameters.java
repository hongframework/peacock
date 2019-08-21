package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("staticParameters")
public class StaticParameters   {

	@XStreamImplicit
    @XStreamAlias("staticParameter")
	private List<String> staticParameterList;

    public StaticParameters() {
    	}
   
 
 	
	public List<String> getStaticParameterList(){
		return staticParameterList;
	}

	public void setStaticParameterList(List<String> staticParameterList){
    	this.staticParameterList = staticParameterList;
    }
}
