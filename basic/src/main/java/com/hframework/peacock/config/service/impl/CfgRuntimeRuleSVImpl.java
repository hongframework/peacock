package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgRuntimeRuleMapper;
import com.hframework.peacock.config.domain.model.CfgRuntimeRule;
import com.hframework.peacock.config.domain.model.CfgRuntimeRule_Example;
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeRuleSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgRuntimeRuleSV")
public class CfgRuntimeRuleSVImpl  implements ICfgRuntimeRuleSV {

	@Resource
	private CfgRuntimeRuleMapper cfgRuntimeRuleMapper;
  


    /**
    * 创建鍔ㄦ�佽鍒�
    * @param cfgRuntimeRule
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeRule cfgRuntimeRule) throws Exception {
        return cfgRuntimeRuleMapper.insertSelective(cfgRuntimeRule);
    }

    /**
    * 批量维护鍔ㄦ�佽鍒�
    * @param cfgRuntimeRules
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeRule[] cfgRuntimeRules) throws  Exception{
        int result = 0;
        if(cfgRuntimeRules != null) {
            for (CfgRuntimeRule cfgRuntimeRule : cfgRuntimeRules) {
                if(cfgRuntimeRule.getId() == null) {
                    result += this.create(cfgRuntimeRule);
                }else {
                    result += this.update(cfgRuntimeRule);
                }
            }
        }
        return result;
    }

    /**
    * 更新鍔ㄦ�佽鍒�
    * @param cfgRuntimeRule
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeRule cfgRuntimeRule) throws  Exception {
        return cfgRuntimeRuleMapper.updateByPrimaryKeySelective(cfgRuntimeRule);
    }

    /**
    * 通过查询对象更新鍔ㄦ�佽鍒�
    * @param cfgRuntimeRule
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeRule cfgRuntimeRule, CfgRuntimeRule_Example example) throws  Exception {
        return cfgRuntimeRuleMapper.updateByExampleSelective(cfgRuntimeRule, example);
    }

    /**
    * 删除鍔ㄦ�佽鍒�
    * @param cfgRuntimeRule
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeRule cfgRuntimeRule) throws  Exception {
        return cfgRuntimeRuleMapper.deleteByPrimaryKey(cfgRuntimeRule.getId());
    }

    /**
    * 删除鍔ㄦ�佽鍒�
    * @param cfgRuntimeRuleId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgRuntimeRuleId) throws  Exception {
        return cfgRuntimeRuleMapper.deleteByPrimaryKey(cfgRuntimeRuleId);
    }

    /**
    * 查找所有鍔ㄦ�佽鍒�
    * @return
    */
    public List<CfgRuntimeRule> getCfgRuntimeRuleAll()  throws  Exception {
        return cfgRuntimeRuleMapper.selectByExample(new CfgRuntimeRule_Example());
    }

    /**
    * 通过鍔ㄦ�佽鍒�ID查询鍔ㄦ�佽鍒�
    * @param cfgRuntimeRuleId
    * @return
    * @throws Exception
    */
    public CfgRuntimeRule getCfgRuntimeRuleByPK(Integer cfgRuntimeRuleId)  throws  Exception {
        return cfgRuntimeRuleMapper.selectByPrimaryKey(cfgRuntimeRuleId);
    }


    /**
    * 通过MAP参数查询鍔ㄦ�佽鍒�
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeRule> getCfgRuntimeRuleListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询鍔ㄦ�佽鍒�
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeRule> getCfgRuntimeRuleListByExample(CfgRuntimeRule_Example example) throws  Exception {
        return cfgRuntimeRuleMapper.selectByExampleWithBLOBs(example);
    }

    /**
    * 通过MAP参数查询鍔ㄦ�佽鍒�数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeRuleCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询鍔ㄦ�佽鍒�数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeRuleCountByExample(CfgRuntimeRule_Example example) throws  Exception {
        return cfgRuntimeRuleMapper.countByExample(example);
    }


  	//getter
 	
	public CfgRuntimeRuleMapper getCfgRuntimeRuleMapper(){
		return cfgRuntimeRuleMapper;
	}
	//setter
	public void setCfgRuntimeRuleMapper(CfgRuntimeRuleMapper cfgRuntimeRuleMapper){
    	this.cfgRuntimeRuleMapper = cfgRuntimeRuleMapper;
    }
}
