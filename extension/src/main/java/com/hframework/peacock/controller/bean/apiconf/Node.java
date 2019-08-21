package com.hframework.peacock.controller.bean.apiconf;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("node")
public class Node {

	@XStreamImplicit
    @XStreamAlias("node")
	private List<Node> nodeList;
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
	@XStreamAsAttribute
    @XStreamAlias("path")
	private String path;
	@XStreamAsAttribute
    @XStreamAlias("isFloatNode")
	private String isFloatNode;
	@XStreamAsAttribute
    @XStreamAlias("merge")
	private String merge;
	@XStreamAsAttribute
	@XStreamAlias("is-public")
	private boolean isPublic;
	@XStreamAsAttribute
	@XStreamAlias("batch-helper")
	private String batchHelper;

    @XStreamOmitField
    private String oper;
	@XStreamOmitField
	private boolean isCommon;

	@XStreamOmitField
	private String tempPath;


	public Node() {
    	}
   
 
 	
	public List<Node> getNodeList(){
		return nodeList == null ? new ArrayList<Node>() : nodeList;
	}

	public void setNodeList(List<Node> nodeList){
    	this.nodeList = nodeList;
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

 	
	public String getPath(){
		return path;
	}

	public void setPath(String path){
    	this.path = path;
    }

 	
	public String getIsFloatNode(){
		return isFloatNode;
	}

	public void setIsFloatNode(String isFloatNode){
    	this.isFloatNode = isFloatNode;
    }

 	
	public String getMerge(){
		return merge;
	}

	public void setMerge(String merge){
    	this.merge = merge;
    }

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean aPublic) {
		isPublic = aPublic;
	}

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public void mergeHist(Node histNode) {
        boolean change = false;
        if(StringUtils.isBlank(this.name)){
            this.name = histNode.getName();
        }
        if(!histNode.type.equals(this.type)){
        	this.type = histNode.getType();
		}

		if(StringUtils.isBlank(this.batchHelper) && StringUtils.isNotBlank(histNode.batchHelper)){
			this.batchHelper = histNode.batchHelper;
		}
        this.oper = "2";
    }

	public boolean isCommon() {
		return isCommon;
	}

	public void setCommon(boolean common) {
		isCommon = common;
	}

	public String getTempPath() {
		return tempPath;
	}

	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

	public void setBatchHelper(String batchHelper) {
		this.batchHelper = batchHelper;
	}

    public String getBatchHelper() {
        return batchHelper;
    }
}
