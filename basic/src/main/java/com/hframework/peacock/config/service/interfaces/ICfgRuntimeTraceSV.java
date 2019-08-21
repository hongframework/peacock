package com.hframework.peacock.config.service.interfaces;

import java.util.*;
import com.hframework.peacock.config.domain.model.CfgRuntimeTrace;
import com.hframework.peacock.config.domain.model.CfgRuntimeTrace_Example;


public interface ICfgRuntimeTraceSV   {

  
    /**
    * 创建跟踪配置
    * @param cfgRuntimeTrace
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeTrace cfgRuntimeTrace) throws  Exception;

    /**
    * 批量维护跟踪配置
    * @param cfgRuntimeTraces
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeTrace[] cfgRuntimeTraces) throws  Exception;
    /**
    * 更新跟踪配置
    * @param cfgRuntimeTrace
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeTrace cfgRuntimeTrace) throws  Exception;

    /**
    * 通过查询对象更新跟踪配置
    * @param cfgRuntimeTrace
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeTrace cfgRuntimeTrace, CfgRuntimeTrace_Example example) throws  Exception;

    /**
    * 删除跟踪配置
    * @param cfgRuntimeTrace
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeTrace cfgRuntimeTrace) throws  Exception;


    /**
    * 删除跟踪配置
    * @param cfgRuntimeTraceId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeTraceId) throws  Exception;


    /**
    * 查找所有跟踪配置
    * @return
    */
    public List<CfgRuntimeTrace> getCfgRuntimeTraceAll()  throws  Exception;

    /**
    * 通过跟踪配置ID查询跟踪配置
    * @param cfgRuntimeTraceId
    * @return
    * @throws Exception
    */
    public CfgRuntimeTrace getCfgRuntimeTraceByPK(long cfgRuntimeTraceId)  throws  Exception;

    /**
    * 通过MAP参数查询跟踪配置
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeTrace> getCfgRuntimeTraceListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询跟踪配置
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeTrace> getCfgRuntimeTraceListByExample(CfgRuntimeTrace_Example example) throws  Exception;


    /**
    * 通过MAP参数查询跟踪配置数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeTraceCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询跟踪配置数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeTraceCountByExample(CfgRuntimeTrace_Example example) throws  Exception;


 }
