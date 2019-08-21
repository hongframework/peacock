package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgRuntimeApi;
import com.hframework.peacock.config.domain.model.CfgRuntimeApi_Example;


public interface ICfgRuntimeApiSV   {

  
    /**
    * 创建动态API
    * @param cfgRuntimeApi
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeApi cfgRuntimeApi) throws  Exception;

    /**
    * 批量维护动态API
    * @param cfgRuntimeApis
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeApi[] cfgRuntimeApis) throws  Exception;
    /**
    * 更新动态API
    * @param cfgRuntimeApi
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeApi cfgRuntimeApi) throws  Exception;

    /**
    * 通过查询对象更新动态API
    * @param cfgRuntimeApi
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeApi cfgRuntimeApi, CfgRuntimeApi_Example example) throws  Exception;

    /**
    * 删除动态API
    * @param cfgRuntimeApi
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeApi cfgRuntimeApi) throws  Exception;


    /**
    * 删除动态API
    * @param cfgRuntimeApiId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeApiId) throws  Exception;


    /**
    * 查找所有动态API
    * @return
    */
    public List<CfgRuntimeApi> getCfgRuntimeApiAll()  throws  Exception;

    /**
    * 通过动态APIID查询动态API
    * @param cfgRuntimeApiId
    * @return
    * @throws Exception
    */
    public CfgRuntimeApi getCfgRuntimeApiByPK(long cfgRuntimeApiId)  throws  Exception;

    /**
    * 通过MAP参数查询动态API
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeApi> getCfgRuntimeApiListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询动态API
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeApi> getCfgRuntimeApiListByExample(CfgRuntimeApi_Example example) throws  Exception;


    /**
    * 通过MAP参数查询动态API数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeApiCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询动态API数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeApiCountByExample(CfgRuntimeApi_Example example) throws  Exception;


 }
