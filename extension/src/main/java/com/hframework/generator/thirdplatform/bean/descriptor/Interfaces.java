package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("interfaces")
public class Interfaces   {

	@XStreamImplicit
    @XStreamAlias("interface")
	private List<Interface> interface1List;

    public Interfaces() {
    	}
   
 
 	
	public List<Interface> getInterface1List(){
		return interface1List;
	}

	public void setInterface1List(List<Interface> interface1List){
    	this.interface1List = interface1List;
    }
}
