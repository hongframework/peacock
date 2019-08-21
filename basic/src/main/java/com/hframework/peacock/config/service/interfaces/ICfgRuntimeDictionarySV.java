package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgRuntimeDictionary;
import com.hframework.peacock.config.domain.model.CfgRuntimeDictionary_Example;


public interface ICfgRuntimeDictionarySV   {

  
    /**
    * 创建API字典
    * @param cfgRuntimeDictionary
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeDictionary cfgRuntimeDictionary) throws  Exception;

    /**
    * 批量维护API字典
    * @param cfgRuntimeDictionarys
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeDictionary[] cfgRuntimeDictionarys) throws  Exception;
    /**
    * 更新API字典
    * @param cfgRuntimeDictionary
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeDictionary cfgRuntimeDictionary) throws  Exception;

    /**
    * 通过查询对象更新API字典
    * @param cfgRuntimeDictionary
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeDictionary cfgRuntimeDictionary, CfgRuntimeDictionary_Example example) throws  Exception;

    /**
    * 删除API字典
    * @param cfgRuntimeDictionary
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeDictionary cfgRuntimeDictionary) throws  Exception;


    /**
    * 删除API字典
    * @param cfgRuntimeDictionaryId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeDictionaryId) throws  Exception;


    /**
    * 查找所有API字典
    * @return
    */
    public List<CfgRuntimeDictionary> getCfgRuntimeDictionaryAll()  throws  Exception;

    /**
    * 通过API字典ID查询API字典
    * @param cfgRuntimeDictionaryId
    * @return
    * @throws Exception
    */
    public CfgRuntimeDictionary getCfgRuntimeDictionaryByPK(long cfgRuntimeDictionaryId)  throws  Exception;

    /**
    * 通过MAP参数查询API字典
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeDictionary> getCfgRuntimeDictionaryListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询API字典
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeDictionary> getCfgRuntimeDictionaryListByExample(CfgRuntimeDictionary_Example example) throws  Exception;


    /**
    * 通过MAP参数查询API字典数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeDictionaryCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询API字典数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeDictionaryCountByExample(CfgRuntimeDictionary_Example example) throws  Exception;


 }
