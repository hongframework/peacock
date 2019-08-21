package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgRuntimeDictionaryItems;
import com.hframework.peacock.config.domain.model.CfgRuntimeDictionaryItems_Example;


public interface ICfgRuntimeDictionaryItemsSV   {

  
    /**
    * 创建API字典项
    * @param cfgRuntimeDictionaryItems
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems) throws  Exception;

    /**
    * 批量维护API字典项
    * @param cfgRuntimeDictionaryItemss
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeDictionaryItems[] cfgRuntimeDictionaryItemss) throws  Exception;
    /**
    * 更新API字典项
    * @param cfgRuntimeDictionaryItems
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems) throws  Exception;

    /**
    * 通过查询对象更新API字典项
    * @param cfgRuntimeDictionaryItems
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems, CfgRuntimeDictionaryItems_Example example) throws  Exception;

    /**
    * 删除API字典项
    * @param cfgRuntimeDictionaryItems
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems) throws  Exception;


    /**
    * 删除API字典项
    * @param cfgRuntimeDictionaryItemsId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeDictionaryItemsId) throws  Exception;


    /**
    * 查找所有API字典项
    * @return
    */
    public List<CfgRuntimeDictionaryItems> getCfgRuntimeDictionaryItemsAll()  throws  Exception;

    /**
    * 通过API字典项ID查询API字典项
    * @param cfgRuntimeDictionaryItemsId
    * @return
    * @throws Exception
    */
    public CfgRuntimeDictionaryItems getCfgRuntimeDictionaryItemsByPK(long cfgRuntimeDictionaryItemsId)  throws  Exception;

    /**
    * 通过MAP参数查询API字典项
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeDictionaryItems> getCfgRuntimeDictionaryItemsListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询API字典项
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeDictionaryItems> getCfgRuntimeDictionaryItemsListByExample(CfgRuntimeDictionaryItems_Example example) throws  Exception;


    /**
    * 通过MAP参数查询API字典项数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeDictionaryItemsCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询API字典项数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeDictionaryItemsCountByExample(CfgRuntimeDictionaryItems_Example example) throws  Exception;


 }
