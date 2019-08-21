package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgIndexRedis;
import com.hframework.peacock.config.domain.model.CfgIndexRedis_Example;


public interface ICfgIndexRedisSV   {

  
    /**
    * 创建REDIS鎸囨爣
    * @param cfgIndexRedis
    * @return
    * @throws Exception
    */
    public int create(CfgIndexRedis cfgIndexRedis) throws  Exception;

    /**
    * 批量维护REDIS鎸囨爣
    * @param cfgIndexRediss
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgIndexRedis[] cfgIndexRediss) throws  Exception;
    /**
    * 更新REDIS鎸囨爣
    * @param cfgIndexRedis
    * @return
    * @throws Exception
    */
    public int update(CfgIndexRedis cfgIndexRedis) throws  Exception;

    /**
    * 通过查询对象更新REDIS鎸囨爣
    * @param cfgIndexRedis
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgIndexRedis cfgIndexRedis, CfgIndexRedis_Example example) throws  Exception;

    /**
    * 删除REDIS鎸囨爣
    * @param cfgIndexRedis
    * @return
    * @throws Exception
    */
    public int delete(CfgIndexRedis cfgIndexRedis) throws  Exception;


    /**
    * 删除REDIS鎸囨爣
    * @param cfgIndexRedisId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgIndexRedisId) throws  Exception;


    /**
    * 查找所有REDIS鎸囨爣
    * @return
    */
    public List<CfgIndexRedis> getCfgIndexRedisAll()  throws  Exception;

    /**
    * 通过REDIS鎸囨爣ID查询REDIS鎸囨爣
    * @param cfgIndexRedisId
    * @return
    * @throws Exception
    */
    public CfgIndexRedis getCfgIndexRedisByPK(Integer cfgIndexRedisId)  throws  Exception;

    /**
    * 通过MAP参数查询REDIS鎸囨爣
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgIndexRedis> getCfgIndexRedisListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询REDIS鎸囨爣
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgIndexRedis> getCfgIndexRedisListByExample(CfgIndexRedis_Example example) throws  Exception;


    /**
    * 通过MAP参数查询REDIS鎸囨爣数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgIndexRedisCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询REDIS鎸囨爣数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgIndexRedisCountByExample(CfgIndexRedis_Example example) throws  Exception;


 }
