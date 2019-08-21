package com.hframework.peacock.config.service.interfaces;

import java.util.*;
import com.hframework.peacock.config.domain.model.CfgRuntimeResponse;
import com.hframework.peacock.config.domain.model.CfgRuntimeResponse_Example;


public interface ICfgRuntimeResponseSV   {

  
    /**
    * 创建响应参数
    * @param cfgRuntimeResponse
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeResponse cfgRuntimeResponse) throws  Exception;

    /**
    * 批量维护响应参数
    * @param cfgRuntimeResponses
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeResponse[] cfgRuntimeResponses) throws  Exception;
    /**
    * 更新响应参数
    * @param cfgRuntimeResponse
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeResponse cfgRuntimeResponse) throws  Exception;

    /**
    * 通过查询对象更新响应参数
    * @param cfgRuntimeResponse
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeResponse cfgRuntimeResponse, CfgRuntimeResponse_Example example) throws  Exception;

    /**
    * 删除响应参数
    * @param cfgRuntimeResponse
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeResponse cfgRuntimeResponse) throws  Exception;


    /**
    * 删除响应参数
    * @param cfgRuntimeResponseId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeResponseId) throws  Exception;


    /**
    * 查找所有响应参数
    * @return
    */
    public List<CfgRuntimeResponse> getCfgRuntimeResponseAll()  throws  Exception;

    /**
    * 通过响应参数ID查询响应参数
    * @param cfgRuntimeResponseId
    * @return
    * @throws Exception
    */
    public CfgRuntimeResponse getCfgRuntimeResponseByPK(long cfgRuntimeResponseId)  throws  Exception;

    /**
    * 通过MAP参数查询响应参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeResponse> getCfgRuntimeResponseListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询响应参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeResponse> getCfgRuntimeResponseListByExample(CfgRuntimeResponse_Example example) throws  Exception;


    /**
    * 通过MAP参数查询响应参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeResponseCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询响应参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeResponseCountByExample(CfgRuntimeResponse_Example example) throws  Exception;


 }
