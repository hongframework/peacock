package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgMgrModule;
import com.hframework.peacock.config.domain.model.CfgMgrModule_Example;


public interface ICfgMgrModuleSV   {

  
    /**
    * 创建模块
    * @param cfgMgrModule
    * @return
    * @throws Exception
    */
    public int create(CfgMgrModule cfgMgrModule) throws  Exception;

    /**
    * 批量维护模块
    * @param cfgMgrModules
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgMgrModule[] cfgMgrModules) throws  Exception;
    /**
    * 更新模块
    * @param cfgMgrModule
    * @return
    * @throws Exception
    */
    public int update(CfgMgrModule cfgMgrModule) throws  Exception;

    /**
    * 通过查询对象更新模块
    * @param cfgMgrModule
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgMgrModule cfgMgrModule, CfgMgrModule_Example example) throws  Exception;

    /**
    * 删除模块
    * @param cfgMgrModule
    * @return
    * @throws Exception
    */
    public int delete(CfgMgrModule cfgMgrModule) throws  Exception;


    /**
    * 删除模块
    * @param cfgMgrModuleId
    * @return
    * @throws Exception
    */
    public int delete(long cfgMgrModuleId) throws  Exception;


    /**
    * 查找所有模块
    * @return
    */
    public List<CfgMgrModule> getCfgMgrModuleAll()  throws  Exception;

    /**
    * 通过模块ID查询模块
    * @param cfgMgrModuleId
    * @return
    * @throws Exception
    */
    public CfgMgrModule getCfgMgrModuleByPK(long cfgMgrModuleId)  throws  Exception;

    /**
    * 通过MAP参数查询模块
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgMgrModule> getCfgMgrModuleListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询模块
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgMgrModule> getCfgMgrModuleListByExample(CfgMgrModule_Example example) throws  Exception;


    /**
    * 通过MAP参数查询模块数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgMgrModuleCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询模块数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgMgrModuleCountByExample(CfgMgrModule_Example example) throws  Exception;


 }
