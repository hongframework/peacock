package com.hframework.peacock.config.service.interfaces;

import java.util.*;
import com.hframework.peacock.config.domain.model.CfgTestCase;
import com.hframework.peacock.config.domain.model.CfgTestCase_Example;


public interface ICfgTestCaseSV   {

  
    /**
    * 创建测试CASE
    * @param cfgTestCase
    * @return
    * @throws Exception
    */
    public int create(CfgTestCase cfgTestCase) throws  Exception;

    /**
    * 批量维护测试CASE
    * @param cfgTestCases
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgTestCase[] cfgTestCases) throws  Exception;
    /**
    * 更新测试CASE
    * @param cfgTestCase
    * @return
    * @throws Exception
    */
    public int update(CfgTestCase cfgTestCase) throws  Exception;

    /**
    * 通过查询对象更新测试CASE
    * @param cfgTestCase
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgTestCase cfgTestCase, CfgTestCase_Example example) throws  Exception;

    /**
    * 删除测试CASE
    * @param cfgTestCase
    * @return
    * @throws Exception
    */
    public int delete(CfgTestCase cfgTestCase) throws  Exception;


    /**
    * 删除测试CASE
    * @param cfgTestCaseId
    * @return
    * @throws Exception
    */
    public int delete(long cfgTestCaseId) throws  Exception;


    /**
    * 查找所有测试CASE
    * @return
    */
    public List<CfgTestCase> getCfgTestCaseAll()  throws  Exception;

    /**
    * 通过测试CASEID查询测试CASE
    * @param cfgTestCaseId
    * @return
    * @throws Exception
    */
    public CfgTestCase getCfgTestCaseByPK(long cfgTestCaseId)  throws  Exception;

    /**
    * 通过MAP参数查询测试CASE
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgTestCase> getCfgTestCaseListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询测试CASE
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgTestCase> getCfgTestCaseListByExample(CfgTestCase_Example example) throws  Exception;


    /**
    * 通过MAP参数查询测试CASE数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgTestCaseCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询测试CASE数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgTestCaseCountByExample(CfgTestCase_Example example) throws  Exception;


 }
