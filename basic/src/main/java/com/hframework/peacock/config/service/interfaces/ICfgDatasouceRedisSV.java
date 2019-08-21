package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgDatasouceRedis;
import com.hframework.peacock.config.domain.model.CfgDatasouceRedis_Example;


public interface ICfgDatasouceRedisSV   {

  
    /**
    * 创建REDIS
    * @param cfgDatasouceRedis
    * @return
    * @throws Exception
    */
    public int create(CfgDatasouceRedis cfgDatasouceRedis) throws  Exception;

    /**
    * 批量维护REDIS
    * @param cfgDatasouceRediss
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgDatasouceRedis[] cfgDatasouceRediss) throws  Exception;
    /**
    * 更新REDIS
    * @param cfgDatasouceRedis
    * @return
    * @throws Exception
    */
    public int update(CfgDatasouceRedis cfgDatasouceRedis) throws  Exception;

    /**
    * 通过查询对象更新REDIS
    * @param cfgDatasouceRedis
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgDatasouceRedis cfgDatasouceRedis, CfgDatasouceRedis_Example example) throws  Exception;

    /**
    * 删除REDIS
    * @param cfgDatasouceRedis
    * @return
    * @throws Exception
    */
    public int delete(CfgDatasouceRedis cfgDatasouceRedis) throws  Exception;


    /**
    * 删除REDIS
    * @param cfgDatasouceRedisId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgDatasouceRedisId) throws  Exception;


    /**
    * 查找所有REDIS
    * @return
    */
    public List<CfgDatasouceRedis> getCfgDatasouceRedisAll()  throws  Exception;

    /**
    * 通过REDISID查询REDIS
    * @param cfgDatasouceRedisId
    * @return
    * @throws Exception
    */
    public CfgDatasouceRedis getCfgDatasouceRedisByPK(Integer cfgDatasouceRedisId)  throws  Exception;

    /**
    * 通过MAP参数查询REDIS
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgDatasouceRedis> getCfgDatasouceRedisListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询REDIS
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgDatasouceRedis> getCfgDatasouceRedisListByExample(CfgDatasouceRedis_Example example) throws  Exception;


    /**
    * 通过MAP参数查询REDIS数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgDatasouceRedisCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询REDIS数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgDatasouceRedisCountByExample(CfgDatasouceRedis_Example example) throws  Exception;


 }
