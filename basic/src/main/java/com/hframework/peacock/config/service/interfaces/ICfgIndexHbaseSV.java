package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgIndexHbase;
import com.hframework.peacock.config.domain.model.CfgIndexHbase_Example;


public interface ICfgIndexHbaseSV   {

  
    /**
    * 创建HBASE鎸囨爣
    * @param cfgIndexHbase
    * @return
    * @throws Exception
    */
    public int create(CfgIndexHbase cfgIndexHbase) throws  Exception;

    /**
    * 批量维护HBASE鎸囨爣
    * @param cfgIndexHbases
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgIndexHbase[] cfgIndexHbases) throws  Exception;
    /**
    * 更新HBASE鎸囨爣
    * @param cfgIndexHbase
    * @return
    * @throws Exception
    */
    public int update(CfgIndexHbase cfgIndexHbase) throws  Exception;

    /**
    * 通过查询对象更新HBASE鎸囨爣
    * @param cfgIndexHbase
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgIndexHbase cfgIndexHbase, CfgIndexHbase_Example example) throws  Exception;

    /**
    * 删除HBASE鎸囨爣
    * @param cfgIndexHbase
    * @return
    * @throws Exception
    */
    public int delete(CfgIndexHbase cfgIndexHbase) throws  Exception;


    /**
    * 删除HBASE鎸囨爣
    * @param cfgIndexHbaseId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgIndexHbaseId) throws  Exception;


    /**
    * 查找所有HBASE鎸囨爣
    * @return
    */
    public List<CfgIndexHbase> getCfgIndexHbaseAll()  throws  Exception;

    /**
    * 通过HBASE鎸囨爣ID查询HBASE鎸囨爣
    * @param cfgIndexHbaseId
    * @return
    * @throws Exception
    */
    public CfgIndexHbase getCfgIndexHbaseByPK(Integer cfgIndexHbaseId)  throws  Exception;

    /**
    * 通过MAP参数查询HBASE鎸囨爣
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgIndexHbase> getCfgIndexHbaseListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询HBASE鎸囨爣
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgIndexHbase> getCfgIndexHbaseListByExample(CfgIndexHbase_Example example) throws  Exception;


    /**
    * 通过MAP参数查询HBASE鎸囨爣数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgIndexHbaseCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询HBASE鎸囨爣数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgIndexHbaseCountByExample(CfgIndexHbase_Example example) throws  Exception;


 }
