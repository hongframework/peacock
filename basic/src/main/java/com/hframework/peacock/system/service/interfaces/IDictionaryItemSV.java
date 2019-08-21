package com.hframework.peacock.system.service.interfaces;

import java.util.*;
import com.hframework.peacock.system.domain.model.DictionaryItem;
import com.hframework.peacock.system.domain.model.DictionaryItem_Example;


public interface IDictionaryItemSV   {

  
    /**
    * 创建瀛楀吀椤�
    * @param dictionaryItem
    * @return
    * @throws Exception
    */
    public int create(DictionaryItem dictionaryItem) throws  Exception;

    /**
    * 批量维护瀛楀吀椤�
    * @param dictionaryItems
    * @return
    * @throws Exception
    */
    public int batchOperate(DictionaryItem[] dictionaryItems) throws  Exception;
    /**
    * 更新瀛楀吀椤�
    * @param dictionaryItem
    * @return
    * @throws Exception
    */
    public int update(DictionaryItem dictionaryItem) throws  Exception;

    /**
    * 通过查询对象更新瀛楀吀椤�
    * @param dictionaryItem
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(DictionaryItem dictionaryItem, DictionaryItem_Example example) throws  Exception;

    /**
    * 删除瀛楀吀椤�
    * @param dictionaryItem
    * @return
    * @throws Exception
    */
    public int delete(DictionaryItem dictionaryItem) throws  Exception;


    /**
    * 删除瀛楀吀椤�
    * @param dictionaryItemId
    * @return
    * @throws Exception
    */
    public int delete(long dictionaryItemId) throws  Exception;


    /**
    * 查找所有瀛楀吀椤�
    * @return
    */
    public List<DictionaryItem> getDictionaryItemAll()  throws  Exception;

    /**
    * 通过瀛楀吀椤�ID查询瀛楀吀椤�
    * @param dictionaryItemId
    * @return
    * @throws Exception
    */
    public DictionaryItem getDictionaryItemByPK(long dictionaryItemId)  throws  Exception;

    /**
    * 通过MAP参数查询瀛楀吀椤�
    * @param params
    * @return
    * @throws Exception
    */
    public List<DictionaryItem> getDictionaryItemListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询瀛楀吀椤�
    * @param example
    * @return
    * @throws Exception
    */
    public List<DictionaryItem> getDictionaryItemListByExample(DictionaryItem_Example example) throws  Exception;


    /**
    * 通过MAP参数查询瀛楀吀椤�数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getDictionaryItemCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询瀛楀吀椤�数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getDictionaryItemCountByExample(DictionaryItem_Example example) throws  Exception;


 }
