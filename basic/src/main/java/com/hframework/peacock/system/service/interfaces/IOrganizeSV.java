package com.hframework.peacock.system.service.interfaces;

import java.util.*;
import com.hframework.peacock.system.domain.model.Organize;
import com.hframework.peacock.system.domain.model.Organize_Example;


public interface IOrganizeSV   {

  
    /**
    * 创建缁勭粐
    * @param organize
    * @return
    * @throws Exception
    */
    public int create(Organize organize) throws  Exception;

    /**
    * 批量维护缁勭粐
    * @param organizes
    * @return
    * @throws Exception
    */
    public int batchOperate(Organize[] organizes) throws  Exception;
    /**
    * 更新缁勭粐
    * @param organize
    * @return
    * @throws Exception
    */
    public int update(Organize organize) throws  Exception;

    /**
    * 通过查询对象更新缁勭粐
    * @param organize
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(Organize organize, Organize_Example example) throws  Exception;

    /**
    * 删除缁勭粐
    * @param organize
    * @return
    * @throws Exception
    */
    public int delete(Organize organize) throws  Exception;


    /**
    * 删除缁勭粐
    * @param organizeId
    * @return
    * @throws Exception
    */
    public int delete(long organizeId) throws  Exception;


    /**
    * 查找所有缁勭粐
    * @return
    */
    public List<Organize> getOrganizeAll()  throws  Exception;

    /**
    * 通过缁勭粐ID查询缁勭粐
    * @param organizeId
    * @return
    * @throws Exception
    */
    public Organize getOrganizeByPK(long organizeId)  throws  Exception;

    /**
    * 通过MAP参数查询缁勭粐
    * @param params
    * @return
    * @throws Exception
    */
    public List<Organize> getOrganizeListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询缁勭粐
    * @param example
    * @return
    * @throws Exception
    */
    public List<Organize> getOrganizeListByExample(Organize_Example example) throws  Exception;

    /**
    * 通过父级缁勭粐ID查询缁勭粐树
    * @param organize
    * @return
    * @throws Exception
    */
    public Map<Long, List<Organize>> getOrganizeTreeByParentId(Organize organize, Organize_Example example)  throws  Exception;

    /**
    * 通过MAP参数查询缁勭粐数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getOrganizeCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询缁勭粐数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getOrganizeCountByExample(Organize_Example example) throws  Exception;


 }
