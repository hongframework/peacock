package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgDatasouceHbase;
import com.hframework.peacock.config.domain.model.CfgDatasouceHbase_Example;


public interface ICfgDatasouceHbaseSV   {

  
    /**
    * 创建HBASE
    * @param cfgDatasouceHbase
    * @return
    * @throws Exception
    */
    public int create(CfgDatasouceHbase cfgDatasouceHbase) throws  Exception;

    /**
    * 批量维护HBASE
    * @param cfgDatasouceHbases
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgDatasouceHbase[] cfgDatasouceHbases) throws  Exception;
    /**
    * 更新HBASE
    * @param cfgDatasouceHbase
    * @return
    * @throws Exception
    */
    public int update(CfgDatasouceHbase cfgDatasouceHbase) throws  Exception;

    /**
    * 通过查询对象更新HBASE
    * @param cfgDatasouceHbase
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgDatasouceHbase cfgDatasouceHbase, CfgDatasouceHbase_Example example) throws  Exception;

    /**
    * 删除HBASE
    * @param cfgDatasouceHbase
    * @return
    * @throws Exception
    */
    public int delete(CfgDatasouceHbase cfgDatasouceHbase) throws  Exception;


    /**
    * 删除HBASE
    * @param cfgDatasouceHbaseId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgDatasouceHbaseId) throws  Exception;


    /**
    * 查找所有HBASE
    * @return
    */
    public List<CfgDatasouceHbase> getCfgDatasouceHbaseAll()  throws  Exception;

    /**
    * 通过HBASEID查询HBASE
    * @param cfgDatasouceHbaseId
    * @return
    * @throws Exception
    */
    public CfgDatasouceHbase getCfgDatasouceHbaseByPK(Integer cfgDatasouceHbaseId)  throws  Exception;

    /**
    * 通过MAP参数查询HBASE
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgDatasouceHbase> getCfgDatasouceHbaseListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询HBASE
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgDatasouceHbase> getCfgDatasouceHbaseListByExample(CfgDatasouceHbase_Example example) throws  Exception;


    /**
    * 通过MAP参数查询HBASE数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgDatasouceHbaseCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询HBASE数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgDatasouceHbaseCountByExample(CfgDatasouceHbase_Example example) throws  Exception;


 }
