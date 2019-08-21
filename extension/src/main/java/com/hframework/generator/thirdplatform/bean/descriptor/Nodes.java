package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("nodes")
public class Nodes   {

	@XStreamImplicit
    @XStreamAlias("node")
	private List<Node> nodeList;

    public Nodes() {
    	}
   
 
 	
	public List<Node> getNodeList(){
		return nodeList == null ? new ArrayList<Node>() : nodeList;
	}

	public void setNodeList(List<Node> nodeList){
    	this.nodeList = nodeList;
    }
}
