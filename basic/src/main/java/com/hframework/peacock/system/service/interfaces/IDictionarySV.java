package com.hframework.peacock.system.service.interfaces;

import java.util.*;
import com.hframework.peacock.system.domain.model.Dictionary;
import com.hframework.peacock.system.domain.model.Dictionary_Example;


public interface IDictionarySV   {

  
    /**
    * 创建瀛楀吀
    * @param dictionary
    * @return
    * @throws Exception
    */
    public int create(Dictionary dictionary) throws  Exception;

    /**
    * 批量维护瀛楀吀
    * @param dictionarys
    * @return
    * @throws Exception
    */
    public int batchOperate(Dictionary[] dictionarys) throws  Exception;
    /**
    * 更新瀛楀吀
    * @param dictionary
    * @return
    * @throws Exception
    */
    public int update(Dictionary dictionary) throws  Exception;

    /**
    * 通过查询对象更新瀛楀吀
    * @param dictionary
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(Dictionary dictionary, Dictionary_Example example) throws  Exception;

    /**
    * 删除瀛楀吀
    * @param dictionary
    * @return
    * @throws Exception
    */
    public int delete(Dictionary dictionary) throws  Exception;


    /**
    * 删除瀛楀吀
    * @param dictionaryId
    * @return
    * @throws Exception
    */
    public int delete(long dictionaryId) throws  Exception;


    /**
    * 查找所有瀛楀吀
    * @return
    */
    public List<Dictionary> getDictionaryAll()  throws  Exception;

    /**
    * 通过瀛楀吀ID查询瀛楀吀
    * @param dictionaryId
    * @return
    * @throws Exception
    */
    public Dictionary getDictionaryByPK(long dictionaryId)  throws  Exception;

    /**
    * 通过MAP参数查询瀛楀吀
    * @param params
    * @return
    * @throws Exception
    */
    public List<Dictionary> getDictionaryListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询瀛楀吀
    * @param example
    * @return
    * @throws Exception
    */
    public List<Dictionary> getDictionaryListByExample(Dictionary_Example example) throws  Exception;


    /**
    * 通过MAP参数查询瀛楀吀数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getDictionaryCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询瀛楀吀数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getDictionaryCountByExample(Dictionary_Example example) throws  Exception;


 }
