package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.ThirdPubRequest;
import com.hframework.peacock.config.domain.model.ThirdPubRequest_Example;


public interface IThirdPubRequestSV   {

  
    /**
    * 创建公共请求参数
    * @param thirdPubRequest
    * @return
    * @throws Exception
    */
    public int create(ThirdPubRequest thirdPubRequest) throws  Exception;

    /**
    * 批量维护公共请求参数
    * @param thirdPubRequests
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdPubRequest[] thirdPubRequests) throws  Exception;
    /**
    * 更新公共请求参数
    * @param thirdPubRequest
    * @return
    * @throws Exception
    */
    public int update(ThirdPubRequest thirdPubRequest) throws  Exception;

    /**
    * 通过查询对象更新公共请求参数
    * @param thirdPubRequest
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdPubRequest thirdPubRequest, ThirdPubRequest_Example example) throws  Exception;

    /**
    * 删除公共请求参数
    * @param thirdPubRequest
    * @return
    * @throws Exception
    */
    public int delete(ThirdPubRequest thirdPubRequest) throws  Exception;


    /**
    * 删除公共请求参数
    * @param thirdPubRequestId
    * @return
    * @throws Exception
    */
    public int delete(long thirdPubRequestId) throws  Exception;


    /**
    * 查找所有公共请求参数
    * @return
    */
    public List<ThirdPubRequest> getThirdPubRequestAll()  throws  Exception;

    /**
    * 通过公共请求参数ID查询公共请求参数
    * @param thirdPubRequestId
    * @return
    * @throws Exception
    */
    public ThirdPubRequest getThirdPubRequestByPK(long thirdPubRequestId)  throws  Exception;

    /**
    * 通过MAP参数查询公共请求参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdPubRequest> getThirdPubRequestListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询公共请求参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdPubRequest> getThirdPubRequestListByExample(ThirdPubRequest_Example example) throws  Exception;


    /**
    * 通过MAP参数查询公共请求参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdPubRequestCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询公共请求参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdPubRequestCountByExample(ThirdPubRequest_Example example) throws  Exception;


 }
