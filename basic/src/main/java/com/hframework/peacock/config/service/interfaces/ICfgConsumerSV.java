package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgConsumer;
import com.hframework.peacock.config.domain.model.CfgConsumer_Example;


public interface ICfgConsumerSV   {

  
    /**
    * 创建API娑堣垂鑰�
    * @param cfgConsumer
    * @return
    * @throws Exception
    */
    public int create(CfgConsumer cfgConsumer) throws  Exception;

    /**
    * 批量维护API娑堣垂鑰�
    * @param cfgConsumers
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgConsumer[] cfgConsumers) throws  Exception;
    /**
    * 更新API娑堣垂鑰�
    * @param cfgConsumer
    * @return
    * @throws Exception
    */
    public int update(CfgConsumer cfgConsumer) throws  Exception;

    /**
    * 通过查询对象更新API娑堣垂鑰�
    * @param cfgConsumer
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgConsumer cfgConsumer, CfgConsumer_Example example) throws  Exception;

    /**
    * 删除API娑堣垂鑰�
    * @param cfgConsumer
    * @return
    * @throws Exception
    */
    public int delete(CfgConsumer cfgConsumer) throws  Exception;


    /**
    * 删除API娑堣垂鑰�
    * @param cfgConsumerId
    * @return
    * @throws Exception
    */
    public int delete(long cfgConsumerId) throws  Exception;


    /**
    * 查找所有API娑堣垂鑰�
    * @return
    */
    public List<CfgConsumer> getCfgConsumerAll()  throws  Exception;

    /**
    * 通过API娑堣垂鑰�ID查询API娑堣垂鑰�
    * @param cfgConsumerId
    * @return
    * @throws Exception
    */
    public CfgConsumer getCfgConsumerByPK(long cfgConsumerId)  throws  Exception;

    /**
    * 通过MAP参数查询API娑堣垂鑰�
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgConsumer> getCfgConsumerListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询API娑堣垂鑰�
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgConsumer> getCfgConsumerListByExample(CfgConsumer_Example example) throws  Exception;


    /**
    * 通过MAP参数查询API娑堣垂鑰�数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgConsumerCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询API娑堣垂鑰�数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgConsumerCountByExample(CfgConsumer_Example example) throws  Exception;


 }
