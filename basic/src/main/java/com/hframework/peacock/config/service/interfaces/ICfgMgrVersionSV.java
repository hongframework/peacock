package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgMgrVersion;
import com.hframework.peacock.config.domain.model.CfgMgrVersion_Example;


public interface ICfgMgrVersionSV   {

  
    /**
    * 创建版本
    * @param cfgMgrVersion
    * @return
    * @throws Exception
    */
    public int create(CfgMgrVersion cfgMgrVersion) throws  Exception;

    /**
    * 批量维护版本
    * @param cfgMgrVersions
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgMgrVersion[] cfgMgrVersions) throws  Exception;
    /**
    * 更新版本
    * @param cfgMgrVersion
    * @return
    * @throws Exception
    */
    public int update(CfgMgrVersion cfgMgrVersion) throws  Exception;

    /**
    * 通过查询对象更新版本
    * @param cfgMgrVersion
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgMgrVersion cfgMgrVersion, CfgMgrVersion_Example example) throws  Exception;

    /**
    * 删除版本
    * @param cfgMgrVersion
    * @return
    * @throws Exception
    */
    public int delete(CfgMgrVersion cfgMgrVersion) throws  Exception;


    /**
    * 删除版本
    * @param cfgMgrVersionId
    * @return
    * @throws Exception
    */
    public int delete(long cfgMgrVersionId) throws  Exception;


    /**
    * 查找所有版本
    * @return
    */
    public List<CfgMgrVersion> getCfgMgrVersionAll()  throws  Exception;

    /**
    * 通过版本ID查询版本
    * @param cfgMgrVersionId
    * @return
    * @throws Exception
    */
    public CfgMgrVersion getCfgMgrVersionByPK(long cfgMgrVersionId)  throws  Exception;

    /**
    * 通过MAP参数查询版本
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgMgrVersion> getCfgMgrVersionListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询版本
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgMgrVersion> getCfgMgrVersionListByExample(CfgMgrVersion_Example example) throws  Exception;


    /**
    * 通过MAP参数查询版本数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgMgrVersionCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询版本数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgMgrVersionCountByExample(CfgMgrVersion_Example example) throws  Exception;


 }
