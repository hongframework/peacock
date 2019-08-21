package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgDatasouceMysql;
import com.hframework.peacock.config.domain.model.CfgDatasouceMysql_Example;


public interface ICfgDatasouceMysqlSV   {

  
    /**
    * 创建MYSQL
    * @param cfgDatasouceMysql
    * @return
    * @throws Exception
    */
    public int create(CfgDatasouceMysql cfgDatasouceMysql) throws  Exception;

    /**
    * 批量维护MYSQL
    * @param cfgDatasouceMysqls
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgDatasouceMysql[] cfgDatasouceMysqls) throws  Exception;
    /**
    * 更新MYSQL
    * @param cfgDatasouceMysql
    * @return
    * @throws Exception
    */
    public int update(CfgDatasouceMysql cfgDatasouceMysql) throws  Exception;

    /**
    * 通过查询对象更新MYSQL
    * @param cfgDatasouceMysql
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgDatasouceMysql cfgDatasouceMysql, CfgDatasouceMysql_Example example) throws  Exception;

    /**
    * 删除MYSQL
    * @param cfgDatasouceMysql
    * @return
    * @throws Exception
    */
    public int delete(CfgDatasouceMysql cfgDatasouceMysql) throws  Exception;


    /**
    * 删除MYSQL
    * @param cfgDatasouceMysqlId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgDatasouceMysqlId) throws  Exception;


    /**
    * 查找所有MYSQL
    * @return
    */
    public List<CfgDatasouceMysql> getCfgDatasouceMysqlAll()  throws  Exception;

    /**
    * 通过MYSQLID查询MYSQL
    * @param cfgDatasouceMysqlId
    * @return
    * @throws Exception
    */
    public CfgDatasouceMysql getCfgDatasouceMysqlByPK(Integer cfgDatasouceMysqlId)  throws  Exception;

    /**
    * 通过MAP参数查询MYSQL
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgDatasouceMysql> getCfgDatasouceMysqlListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询MYSQL
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgDatasouceMysql> getCfgDatasouceMysqlListByExample(CfgDatasouceMysql_Example example) throws  Exception;


    /**
    * 通过MAP参数查询MYSQL数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgDatasouceMysqlCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询MYSQL数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgDatasouceMysqlCountByExample(CfgDatasouceMysql_Example example) throws  Exception;


 }
