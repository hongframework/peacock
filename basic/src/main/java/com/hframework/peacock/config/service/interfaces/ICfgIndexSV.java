package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgIndex;
import com.hframework.peacock.config.domain.model.CfgIndex_Example;


public interface ICfgIndexSV   {

  
    /**
    * 创建鎸囨爣
    * @param cfgIndex
    * @return
    * @throws Exception
    */
    public int create(CfgIndex cfgIndex) throws  Exception;

    /**
    * 批量维护鎸囨爣
    * @param cfgIndexs
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgIndex[] cfgIndexs) throws  Exception;
    /**
    * 更新鎸囨爣
    * @param cfgIndex
    * @return
    * @throws Exception
    */
    public int update(CfgIndex cfgIndex) throws  Exception;

    /**
    * 通过查询对象更新鎸囨爣
    * @param cfgIndex
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgIndex cfgIndex, CfgIndex_Example example) throws  Exception;

    /**
    * 删除鎸囨爣
    * @param cfgIndex
    * @return
    * @throws Exception
    */
    public int delete(CfgIndex cfgIndex) throws  Exception;


    /**
    * 删除鎸囨爣
    * @param cfgIndexId
    * @return
    * @throws Exception
    */
    public int delete(long cfgIndexId) throws  Exception;


    /**
    * 查找所有鎸囨爣
    * @return
    */
    public List<CfgIndex> getCfgIndexAll()  throws  Exception;

    /**
    * 通过鎸囨爣ID查询鎸囨爣
    * @param cfgIndexId
    * @return
    * @throws Exception
    */
    public CfgIndex getCfgIndexByPK(long cfgIndexId)  throws  Exception;

    /**
    * 通过MAP参数查询鎸囨爣
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgIndex> getCfgIndexListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询鎸囨爣
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgIndex> getCfgIndexListByExample(CfgIndex_Example example) throws  Exception;


    /**
    * 通过MAP参数查询鎸囨爣数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgIndexCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询鎸囨爣数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgIndexCountByExample(CfgIndex_Example example) throws  Exception;


 }
