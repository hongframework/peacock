package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgIndexMysql;
import com.hframework.peacock.config.domain.model.CfgIndexMysql_Example;


public interface ICfgIndexMysqlSV   {

  
    /**
    * 创建MYSQL鎸囨爣
    * @param cfgIndexMysql
    * @return
    * @throws Exception
    */
    public int create(CfgIndexMysql cfgIndexMysql) throws  Exception;

    /**
    * 批量维护MYSQL鎸囨爣
    * @param cfgIndexMysqls
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgIndexMysql[] cfgIndexMysqls) throws  Exception;
    /**
    * 更新MYSQL鎸囨爣
    * @param cfgIndexMysql
    * @return
    * @throws Exception
    */
    public int update(CfgIndexMysql cfgIndexMysql) throws  Exception;

    /**
    * 通过查询对象更新MYSQL鎸囨爣
    * @param cfgIndexMysql
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgIndexMysql cfgIndexMysql, CfgIndexMysql_Example example) throws  Exception;

    /**
    * 删除MYSQL鎸囨爣
    * @param cfgIndexMysql
    * @return
    * @throws Exception
    */
    public int delete(CfgIndexMysql cfgIndexMysql) throws  Exception;


    /**
    * 删除MYSQL鎸囨爣
    * @param cfgIndexMysqlId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgIndexMysqlId) throws  Exception;


    /**
    * 查找所有MYSQL鎸囨爣
    * @return
    */
    public List<CfgIndexMysql> getCfgIndexMysqlAll()  throws  Exception;

    /**
    * 通过MYSQL鎸囨爣ID查询MYSQL鎸囨爣
    * @param cfgIndexMysqlId
    * @return
    * @throws Exception
    */
    public CfgIndexMysql getCfgIndexMysqlByPK(Integer cfgIndexMysqlId)  throws  Exception;

    /**
    * 通过MAP参数查询MYSQL鎸囨爣
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgIndexMysql> getCfgIndexMysqlListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询MYSQL鎸囨爣
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgIndexMysql> getCfgIndexMysqlListByExample(CfgIndexMysql_Example example) throws  Exception;


    /**
    * 通过MAP参数查询MYSQL鎸囨爣数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgIndexMysqlCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询MYSQL鎸囨爣数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgIndexMysqlCountByExample(CfgIndexMysql_Example example) throws  Exception;


 }
