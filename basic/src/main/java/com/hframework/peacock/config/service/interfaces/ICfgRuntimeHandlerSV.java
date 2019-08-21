package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgRuntimeHandler;
import com.hframework.peacock.config.domain.model.CfgRuntimeHandler_Example;


public interface ICfgRuntimeHandlerSV   {

  
    /**
    * 创建动态Handler
    * @param cfgRuntimeHandler
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeHandler cfgRuntimeHandler) throws  Exception;

    /**
    * 批量维护动态Handler
    * @param cfgRuntimeHandlers
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeHandler[] cfgRuntimeHandlers) throws  Exception;
    /**
    * 更新动态Handler
    * @param cfgRuntimeHandler
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeHandler cfgRuntimeHandler) throws  Exception;

    /**
    * 通过查询对象更新动态Handler
    * @param cfgRuntimeHandler
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeHandler cfgRuntimeHandler, CfgRuntimeHandler_Example example) throws  Exception;

    /**
    * 删除动态Handler
    * @param cfgRuntimeHandler
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeHandler cfgRuntimeHandler) throws  Exception;


    /**
    * 删除动态Handler
    * @param cfgRuntimeHandlerId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeHandlerId) throws  Exception;


    /**
    * 查找所有动态Handler
    * @return
    */
    public List<CfgRuntimeHandler> getCfgRuntimeHandlerAll()  throws  Exception;

    /**
    * 通过动态HandlerID查询动态Handler
    * @param cfgRuntimeHandlerId
    * @return
    * @throws Exception
    */
    public CfgRuntimeHandler getCfgRuntimeHandlerByPK(long cfgRuntimeHandlerId)  throws  Exception;

    /**
    * 通过MAP参数查询动态Handler
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeHandler> getCfgRuntimeHandlerListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询动态Handler
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeHandler> getCfgRuntimeHandlerListByExample(CfgRuntimeHandler_Example example) throws  Exception;


    /**
    * 通过MAP参数查询动态Handler数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeHandlerCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询动态Handler数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeHandlerCountByExample(CfgRuntimeHandler_Example example) throws  Exception;


 }
