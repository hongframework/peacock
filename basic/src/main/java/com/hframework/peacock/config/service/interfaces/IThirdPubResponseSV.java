package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.ThirdPubResponse;
import com.hframework.peacock.config.domain.model.ThirdPubResponse_Example;


public interface IThirdPubResponseSV   {

  
    /**
    * 创建公共响应参数
    * @param thirdPubResponse
    * @return
    * @throws Exception
    */
    public int create(ThirdPubResponse thirdPubResponse) throws  Exception;

    /**
    * 批量维护公共响应参数
    * @param thirdPubResponses
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdPubResponse[] thirdPubResponses) throws  Exception;
    /**
    * 更新公共响应参数
    * @param thirdPubResponse
    * @return
    * @throws Exception
    */
    public int update(ThirdPubResponse thirdPubResponse) throws  Exception;

    /**
    * 通过查询对象更新公共响应参数
    * @param thirdPubResponse
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdPubResponse thirdPubResponse, ThirdPubResponse_Example example) throws  Exception;

    /**
    * 删除公共响应参数
    * @param thirdPubResponse
    * @return
    * @throws Exception
    */
    public int delete(ThirdPubResponse thirdPubResponse) throws  Exception;


    /**
    * 删除公共响应参数
    * @param thirdPubResponseId
    * @return
    * @throws Exception
    */
    public int delete(long thirdPubResponseId) throws  Exception;


    /**
    * 查找所有公共响应参数
    * @return
    */
    public List<ThirdPubResponse> getThirdPubResponseAll()  throws  Exception;

    /**
    * 通过公共响应参数ID查询公共响应参数
    * @param thirdPubResponseId
    * @return
    * @throws Exception
    */
    public ThirdPubResponse getThirdPubResponseByPK(long thirdPubResponseId)  throws  Exception;

    /**
    * 通过MAP参数查询公共响应参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdPubResponse> getThirdPubResponseListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询公共响应参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdPubResponse> getThirdPubResponseListByExample(ThirdPubResponse_Example example) throws  Exception;


    /**
    * 通过MAP参数查询公共响应参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdPubResponseCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询公共响应参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdPubResponseCountByExample(ThirdPubResponse_Example example) throws  Exception;


 }
