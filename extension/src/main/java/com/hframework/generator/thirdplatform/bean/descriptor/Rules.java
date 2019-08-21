package com.hframework.generator.thirdplatform.bean.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;

@XStreamAlias("rules")
public class Rules   {

	@XStreamImplicit
    @XStreamAlias("rule")
	private List<Rule> ruleList;

    public Rules() {
    	}
   
 
 	
	public List<Rule> getRuleList(){
		return ruleList;
	}

	public void setRuleList(List<Rule> ruleList){
    	this.ruleList = ruleList;
    }
}
