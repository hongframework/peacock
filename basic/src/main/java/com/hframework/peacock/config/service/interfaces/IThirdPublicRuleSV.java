package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.ThirdPublicRule;
import com.hframework.peacock.config.domain.model.ThirdPublicRule_Example;


public interface IThirdPublicRuleSV   {

  
    /**
    * 创建公共规则
    * @param thirdPublicRule
    * @return
    * @throws Exception
    */
    public int create(ThirdPublicRule thirdPublicRule) throws  Exception;

    /**
    * 批量维护公共规则
    * @param thirdPublicRules
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdPublicRule[] thirdPublicRules) throws  Exception;
    /**
    * 更新公共规则
    * @param thirdPublicRule
    * @return
    * @throws Exception
    */
    public int update(ThirdPublicRule thirdPublicRule) throws  Exception;

    /**
    * 通过查询对象更新公共规则
    * @param thirdPublicRule
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdPublicRule thirdPublicRule, ThirdPublicRule_Example example) throws  Exception;

    /**
    * 删除公共规则
    * @param thirdPublicRule
    * @return
    * @throws Exception
    */
    public int delete(ThirdPublicRule thirdPublicRule) throws  Exception;


    /**
    * 删除公共规则
    * @param thirdPublicRuleId
    * @return
    * @throws Exception
    */
    public int delete(long thirdPublicRuleId) throws  Exception;


    /**
    * 查找所有公共规则
    * @return
    */
    public List<ThirdPublicRule> getThirdPublicRuleAll()  throws  Exception;

    /**
    * 通过公共规则ID查询公共规则
    * @param thirdPublicRuleId
    * @return
    * @throws Exception
    */
    public ThirdPublicRule getThirdPublicRuleByPK(long thirdPublicRuleId)  throws  Exception;

    /**
    * 通过MAP参数查询公共规则
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdPublicRule> getThirdPublicRuleListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询公共规则
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdPublicRule> getThirdPublicRuleListByExample(ThirdPublicRule_Example example) throws  Exception;


    /**
    * 通过MAP参数查询公共规则数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdPublicRuleCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询公共规则数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdPublicRuleCountByExample(ThirdPublicRule_Example example) throws  Exception;


 }
