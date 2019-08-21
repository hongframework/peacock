package com.hframework.peacock.config.service.interfaces;

import java.util.*;
import com.hframework.peacock.config.domain.model.CfgRuntimeParameter;
import com.hframework.peacock.config.domain.model.CfgRuntimeParameter_Example;


public interface ICfgRuntimeParameterSV   {

  
    /**
    * 创建参数
    * @param cfgRuntimeParameter
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeParameter cfgRuntimeParameter) throws  Exception;

    /**
    * 批量维护参数
    * @param cfgRuntimeParameters
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeParameter[] cfgRuntimeParameters) throws  Exception;
    /**
    * 更新参数
    * @param cfgRuntimeParameter
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeParameter cfgRuntimeParameter) throws  Exception;

    /**
    * 通过查询对象更新参数
    * @param cfgRuntimeParameter
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeParameter cfgRuntimeParameter, CfgRuntimeParameter_Example example) throws  Exception;

    /**
    * 删除参数
    * @param cfgRuntimeParameter
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeParameter cfgRuntimeParameter) throws  Exception;


    /**
    * 删除参数
    * @param cfgRuntimeParameterId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeParameterId) throws  Exception;


    /**
    * 查找所有参数
    * @return
    */
    public List<CfgRuntimeParameter> getCfgRuntimeParameterAll()  throws  Exception;

    /**
    * 通过参数ID查询参数
    * @param cfgRuntimeParameterId
    * @return
    * @throws Exception
    */
    public CfgRuntimeParameter getCfgRuntimeParameterByPK(long cfgRuntimeParameterId)  throws  Exception;

    /**
    * 通过MAP参数查询参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeParameter> getCfgRuntimeParameterListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeParameter> getCfgRuntimeParameterListByExample(CfgRuntimeParameter_Example example) throws  Exception;


    /**
    * 通过MAP参数查询参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeParameterCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeParameterCountByExample(CfgRuntimeParameter_Example example) throws  Exception;


 }
