package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgConsumerAuth;
import com.hframework.peacock.config.domain.model.CfgConsumerAuth_Example;


public interface ICfgConsumerAuthSV   {

  
    /**
    * 创建API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuth
    * @return
    * @throws Exception
    */
    public int create(CfgConsumerAuth cfgConsumerAuth) throws  Exception;

    /**
    * 批量维护API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuths
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgConsumerAuth[] cfgConsumerAuths) throws  Exception;
    /**
    * 更新API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuth
    * @return
    * @throws Exception
    */
    public int update(CfgConsumerAuth cfgConsumerAuth) throws  Exception;

    /**
    * 通过查询对象更新API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuth
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgConsumerAuth cfgConsumerAuth, CfgConsumerAuth_Example example) throws  Exception;

    /**
    * 删除API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuth
    * @return
    * @throws Exception
    */
    public int delete(CfgConsumerAuth cfgConsumerAuth) throws  Exception;


    /**
    * 删除API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuthId
    * @return
    * @throws Exception
    */
    public int delete(long cfgConsumerAuthId) throws  Exception;


    /**
    * 查找所有API娑堣垂鑰呮巿鏉�
    * @return
    */
    public List<CfgConsumerAuth> getCfgConsumerAuthAll()  throws  Exception;

    /**
    * 通过API娑堣垂鑰呮巿鏉�ID查询API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuthId
    * @return
    * @throws Exception
    */
    public CfgConsumerAuth getCfgConsumerAuthByPK(long cfgConsumerAuthId)  throws  Exception;

    /**
    * 通过MAP参数查询API娑堣垂鑰呮巿鏉�
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgConsumerAuth> getCfgConsumerAuthListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询API娑堣垂鑰呮巿鏉�
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgConsumerAuth> getCfgConsumerAuthListByExample(CfgConsumerAuth_Example example) throws  Exception;


    /**
    * 通过MAP参数查询API娑堣垂鑰呮巿鏉�数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgConsumerAuthCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询API娑堣垂鑰呮巿鏉�数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgConsumerAuthCountByExample(CfgConsumerAuth_Example example) throws  Exception;


 }
