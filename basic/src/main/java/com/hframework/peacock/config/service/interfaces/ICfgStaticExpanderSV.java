package com.hframework.peacock.config.service.interfaces;

import java.util.*;
import com.hframework.peacock.config.domain.model.CfgStaticExpander;
import com.hframework.peacock.config.domain.model.CfgStaticExpander_Example;


public interface ICfgStaticExpanderSV   {

  
    /**
    * 创建静态扩展器
    * @param cfgStaticExpander
    * @return
    * @throws Exception
    */
    public int create(CfgStaticExpander cfgStaticExpander) throws  Exception;

    /**
    * 批量维护静态扩展器
    * @param cfgStaticExpanders
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgStaticExpander[] cfgStaticExpanders) throws  Exception;
    /**
    * 更新静态扩展器
    * @param cfgStaticExpander
    * @return
    * @throws Exception
    */
    public int update(CfgStaticExpander cfgStaticExpander) throws  Exception;

    /**
    * 通过查询对象更新静态扩展器
    * @param cfgStaticExpander
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgStaticExpander cfgStaticExpander, CfgStaticExpander_Example example) throws  Exception;

    /**
    * 删除静态扩展器
    * @param cfgStaticExpander
    * @return
    * @throws Exception
    */
    public int delete(CfgStaticExpander cfgStaticExpander) throws  Exception;


    /**
    * 删除静态扩展器
    * @param cfgStaticExpanderId
    * @return
    * @throws Exception
    */
    public int delete(long cfgStaticExpanderId) throws  Exception;


    /**
    * 查找所有静态扩展器
    * @return
    */
    public List<CfgStaticExpander> getCfgStaticExpanderAll()  throws  Exception;

    /**
    * 通过静态扩展器ID查询静态扩展器
    * @param cfgStaticExpanderId
    * @return
    * @throws Exception
    */
    public CfgStaticExpander getCfgStaticExpanderByPK(long cfgStaticExpanderId)  throws  Exception;

    /**
    * 通过MAP参数查询静态扩展器
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgStaticExpander> getCfgStaticExpanderListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询静态扩展器
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgStaticExpander> getCfgStaticExpanderListByExample(CfgStaticExpander_Example example) throws  Exception;


    /**
    * 通过MAP参数查询静态扩展器数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgStaticExpanderCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询静态扩展器数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgStaticExpanderCountByExample(CfgStaticExpander_Example example) throws  Exception;


 }
