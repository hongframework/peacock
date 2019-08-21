package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgFeature;
import com.hframework.peacock.config.domain.model.CfgFeature_Example;


public interface ICfgFeatureSV   {

  
    /**
    * 创建鐗瑰緛
    * @param cfgFeature
    * @return
    * @throws Exception
    */
    public int create(CfgFeature cfgFeature) throws  Exception;

    /**
    * 批量维护鐗瑰緛
    * @param cfgFeatures
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgFeature[] cfgFeatures) throws  Exception;
    /**
    * 更新鐗瑰緛
    * @param cfgFeature
    * @return
    * @throws Exception
    */
    public int update(CfgFeature cfgFeature) throws  Exception;

    /**
    * 通过查询对象更新鐗瑰緛
    * @param cfgFeature
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgFeature cfgFeature, CfgFeature_Example example) throws  Exception;

    /**
    * 删除鐗瑰緛
    * @param cfgFeature
    * @return
    * @throws Exception
    */
    public int delete(CfgFeature cfgFeature) throws  Exception;


    /**
    * 删除鐗瑰緛
    * @param cfgFeatureId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgFeatureId) throws  Exception;


    /**
    * 查找所有鐗瑰緛
    * @return
    */
    public List<CfgFeature> getCfgFeatureAll()  throws  Exception;

    /**
    * 通过鐗瑰緛ID查询鐗瑰緛
    * @param cfgFeatureId
    * @return
    * @throws Exception
    */
    public CfgFeature getCfgFeatureByPK(Integer cfgFeatureId)  throws  Exception;

    /**
    * 通过MAP参数查询鐗瑰緛
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgFeature> getCfgFeatureListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询鐗瑰緛
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgFeature> getCfgFeatureListByExample(CfgFeature_Example example) throws  Exception;


    /**
    * 通过MAP参数查询鐗瑰緛数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgFeatureCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询鐗瑰緛数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgFeatureCountByExample(CfgFeature_Example example) throws  Exception;


 }
