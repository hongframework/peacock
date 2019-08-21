package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgApiConf;
import com.hframework.peacock.config.domain.model.CfgApiConf_Example;


public interface ICfgApiConfSV   {

  
    /**
    * 创建API配置表
    * @param cfgApiConf
    * @return
    * @throws Exception
    */
    public int create(CfgApiConf cfgApiConf) throws  Exception;

    /**
    * 批量维护API配置表
    * @param cfgApiConfs
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgApiConf[] cfgApiConfs) throws  Exception;
    /**
    * 更新API配置表
    * @param cfgApiConf
    * @return
    * @throws Exception
    */
    public int update(CfgApiConf cfgApiConf) throws  Exception;

    /**
    * 通过查询对象更新API配置表
    * @param cfgApiConf
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgApiConf cfgApiConf, CfgApiConf_Example example) throws  Exception;

    /**
    * 删除API配置表
    * @param cfgApiConf
    * @return
    * @throws Exception
    */
    public int delete(CfgApiConf cfgApiConf) throws  Exception;


    /**
    * 删除API配置表
    * @param cfgApiConfId
    * @return
    * @throws Exception
    */
    public int delete(long cfgApiConfId) throws  Exception;


    /**
    * 查找所有API配置表
    * @return
    */
    public List<CfgApiConf> getCfgApiConfAll()  throws  Exception;

    /**
    * 通过API配置表ID查询API配置表
    * @param cfgApiConfId
    * @return
    * @throws Exception
    */
    public CfgApiConf getCfgApiConfByPK(long cfgApiConfId)  throws  Exception;

    /**
    * 通过MAP参数查询API配置表
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgApiConf> getCfgApiConfListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询API配置表
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgApiConf> getCfgApiConfListByExample(CfgApiConf_Example example) throws  Exception;


    /**
    * 通过MAP参数查询API配置表数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgApiConfCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询API配置表数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgApiConfCountByExample(CfgApiConf_Example example) throws  Exception;


 }
