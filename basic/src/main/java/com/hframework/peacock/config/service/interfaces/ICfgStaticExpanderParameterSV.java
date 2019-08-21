package com.hframework.peacock.config.service.interfaces;

import java.util.*;
import com.hframework.peacock.config.domain.model.CfgStaticExpanderParameter;
import com.hframework.peacock.config.domain.model.CfgStaticExpanderParameter_Example;


public interface ICfgStaticExpanderParameterSV   {

  
    /**
    * 创建静态扩展器参数
    * @param cfgStaticExpanderParameter
    * @return
    * @throws Exception
    */
    public int create(CfgStaticExpanderParameter cfgStaticExpanderParameter) throws  Exception;

    /**
    * 批量维护静态扩展器参数
    * @param cfgStaticExpanderParameters
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgStaticExpanderParameter[] cfgStaticExpanderParameters) throws  Exception;
    /**
    * 更新静态扩展器参数
    * @param cfgStaticExpanderParameter
    * @return
    * @throws Exception
    */
    public int update(CfgStaticExpanderParameter cfgStaticExpanderParameter) throws  Exception;

    /**
    * 通过查询对象更新静态扩展器参数
    * @param cfgStaticExpanderParameter
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgStaticExpanderParameter cfgStaticExpanderParameter, CfgStaticExpanderParameter_Example example) throws  Exception;

    /**
    * 删除静态扩展器参数
    * @param cfgStaticExpanderParameter
    * @return
    * @throws Exception
    */
    public int delete(CfgStaticExpanderParameter cfgStaticExpanderParameter) throws  Exception;


    /**
    * 删除静态扩展器参数
    * @param cfgStaticExpanderParameterId
    * @return
    * @throws Exception
    */
    public int delete(long cfgStaticExpanderParameterId) throws  Exception;


    /**
    * 查找所有静态扩展器参数
    * @return
    */
    public List<CfgStaticExpanderParameter> getCfgStaticExpanderParameterAll()  throws  Exception;

    /**
    * 通过静态扩展器参数ID查询静态扩展器参数
    * @param cfgStaticExpanderParameterId
    * @return
    * @throws Exception
    */
    public CfgStaticExpanderParameter getCfgStaticExpanderParameterByPK(long cfgStaticExpanderParameterId)  throws  Exception;

    /**
    * 通过MAP参数查询静态扩展器参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgStaticExpanderParameter> getCfgStaticExpanderParameterListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询静态扩展器参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgStaticExpanderParameter> getCfgStaticExpanderParameterListByExample(CfgStaticExpanderParameter_Example example) throws  Exception;


    /**
    * 通过MAP参数查询静态扩展器参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgStaticExpanderParameterCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询静态扩展器参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgStaticExpanderParameterCountByExample(CfgStaticExpanderParameter_Example example) throws  Exception;


 }
