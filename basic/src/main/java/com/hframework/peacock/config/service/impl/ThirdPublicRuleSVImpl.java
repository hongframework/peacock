package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.ThirdPublicRuleMapper;
import com.hframework.peacock.config.domain.model.ThirdPublicRule;
import com.hframework.peacock.config.domain.model.ThirdPublicRule_Example;
import com.hframework.peacock.config.service.interfaces.IThirdPublicRuleSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iThirdPublicRuleSV")
public class ThirdPublicRuleSVImpl  implements IThirdPublicRuleSV {

	@Resource
	private ThirdPublicRuleMapper thirdPublicRuleMapper;
  


    /**
    * 创建公共规则
    * @param thirdPublicRule
    * @return
    * @throws Exception
    */
    public int create(ThirdPublicRule thirdPublicRule) throws Exception {
        return thirdPublicRuleMapper.insertSelective(thirdPublicRule);
    }

    /**
    * 批量维护公共规则
    * @param thirdPublicRules
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdPublicRule[] thirdPublicRules) throws  Exception{
        int result = 0;
        if(thirdPublicRules != null) {
            for (ThirdPublicRule thirdPublicRule : thirdPublicRules) {
                if(thirdPublicRule.getId() == null) {
                    result += this.create(thirdPublicRule);
                }else {
                    result += this.update(thirdPublicRule);
                }
            }
        }
        return result;
    }

    /**
    * 更新公共规则
    * @param thirdPublicRule
    * @return
    * @throws Exception
    */
    public int update(ThirdPublicRule thirdPublicRule) throws  Exception {
        return thirdPublicRuleMapper.updateByPrimaryKeySelective(thirdPublicRule);
    }

    /**
    * 通过查询对象更新公共规则
    * @param thirdPublicRule
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdPublicRule thirdPublicRule, ThirdPublicRule_Example example) throws  Exception {
        return thirdPublicRuleMapper.updateByExampleSelective(thirdPublicRule, example);
    }

    /**
    * 删除公共规则
    * @param thirdPublicRule
    * @return
    * @throws Exception
    */
    public int delete(ThirdPublicRule thirdPublicRule) throws  Exception {
        return thirdPublicRuleMapper.deleteByPrimaryKey(thirdPublicRule.getId());
    }

    /**
    * 删除公共规则
    * @param thirdPublicRuleId
    * @return
    * @throws Exception
    */
    public int delete(long thirdPublicRuleId) throws  Exception {
        return thirdPublicRuleMapper.deleteByPrimaryKey(thirdPublicRuleId);
    }

    /**
    * 查找所有公共规则
    * @return
    */
    public List<ThirdPublicRule> getThirdPublicRuleAll()  throws  Exception {
        return thirdPublicRuleMapper.selectByExample(new ThirdPublicRule_Example());
    }

    /**
    * 通过公共规则ID查询公共规则
    * @param thirdPublicRuleId
    * @return
    * @throws Exception
    */
    public ThirdPublicRule getThirdPublicRuleByPK(long thirdPublicRuleId)  throws  Exception {
        return thirdPublicRuleMapper.selectByPrimaryKey(thirdPublicRuleId);
    }


    /**
    * 通过MAP参数查询公共规则
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdPublicRule> getThirdPublicRuleListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询公共规则
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdPublicRule> getThirdPublicRuleListByExample(ThirdPublicRule_Example example) throws  Exception {
        return thirdPublicRuleMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询公共规则数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdPublicRuleCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询公共规则数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdPublicRuleCountByExample(ThirdPublicRule_Example example) throws  Exception {
        return thirdPublicRuleMapper.countByExample(example);
    }


  	//getter
 	
	public ThirdPublicRuleMapper getThirdPublicRuleMapper(){
		return thirdPublicRuleMapper;
	}
	//setter
	public void setThirdPublicRuleMapper(ThirdPublicRuleMapper thirdPublicRuleMapper){
    	this.thirdPublicRuleMapper = thirdPublicRuleMapper;
    }
}
