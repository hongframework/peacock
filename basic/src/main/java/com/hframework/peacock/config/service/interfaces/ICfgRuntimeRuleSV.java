package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgRuntimeRule;
import com.hframework.peacock.config.domain.model.CfgRuntimeRule_Example;


public interface ICfgRuntimeRuleSV   {

  
    /**
    * 创建鍔ㄦ�佽鍒�
    * @param cfgRuntimeRule
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeRule cfgRuntimeRule) throws  Exception;

    /**
    * 批量维护鍔ㄦ�佽鍒�
    * @param cfgRuntimeRules
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeRule[] cfgRuntimeRules) throws  Exception;
    /**
    * 更新鍔ㄦ�佽鍒�
    * @param cfgRuntimeRule
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeRule cfgRuntimeRule) throws  Exception;

    /**
    * 通过查询对象更新鍔ㄦ�佽鍒�
    * @param cfgRuntimeRule
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeRule cfgRuntimeRule, CfgRuntimeRule_Example example) throws  Exception;

    /**
    * 删除鍔ㄦ�佽鍒�
    * @param cfgRuntimeRule
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeRule cfgRuntimeRule) throws  Exception;


    /**
    * 删除鍔ㄦ�佽鍒�
    * @param cfgRuntimeRuleId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgRuntimeRuleId) throws  Exception;


    /**
    * 查找所有鍔ㄦ�佽鍒�
    * @return
    */
    public List<CfgRuntimeRule> getCfgRuntimeRuleAll()  throws  Exception;

    /**
    * 通过鍔ㄦ�佽鍒�ID查询鍔ㄦ�佽鍒�
    * @param cfgRuntimeRuleId
    * @return
    * @throws Exception
    */
    public CfgRuntimeRule getCfgRuntimeRuleByPK(Integer cfgRuntimeRuleId)  throws  Exception;

    /**
    * 通过MAP参数查询鍔ㄦ�佽鍒�
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeRule> getCfgRuntimeRuleListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询鍔ㄦ�佽鍒�
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeRule> getCfgRuntimeRuleListByExample(CfgRuntimeRule_Example example) throws  Exception;


    /**
    * 通过MAP参数查询鍔ㄦ�佽鍒�数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeRuleCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询鍔ㄦ�佽鍒�数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeRuleCountByExample(CfgRuntimeRule_Example example) throws  Exception;


 }
