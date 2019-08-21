package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.ThirdApi;
import com.hframework.peacock.config.domain.model.ThirdApi_Example;


public interface IThirdApiSV   {

  
    /**
    * 创建访问API
    * @param thirdApi
    * @return
    * @throws Exception
    */
    public int create(ThirdApi thirdApi) throws  Exception;

    /**
    * 批量维护访问API
    * @param thirdApis
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdApi[] thirdApis) throws  Exception;
    /**
    * 更新访问API
    * @param thirdApi
    * @return
    * @throws Exception
    */
    public int update(ThirdApi thirdApi) throws  Exception;

    /**
    * 通过查询对象更新访问API
    * @param thirdApi
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdApi thirdApi, ThirdApi_Example example) throws  Exception;

    /**
    * 删除访问API
    * @param thirdApi
    * @return
    * @throws Exception
    */
    public int delete(ThirdApi thirdApi) throws  Exception;


    /**
    * 删除访问API
    * @param thirdApiId
    * @return
    * @throws Exception
    */
    public int delete(long thirdApiId) throws  Exception;


    /**
    * 查找所有访问API
    * @return
    */
    public List<ThirdApi> getThirdApiAll()  throws  Exception;

    /**
    * 通过访问APIID查询访问API
    * @param thirdApiId
    * @return
    * @throws Exception
    */
    public ThirdApi getThirdApiByPK(long thirdApiId)  throws  Exception;

    /**
    * 通过MAP参数查询访问API
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdApi> getThirdApiListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询访问API
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdApi> getThirdApiListByExample(ThirdApi_Example example) throws  Exception;


    /**
    * 通过MAP参数查询访问API数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdApiCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询访问API数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdApiCountByExample(ThirdApi_Example example) throws  Exception;


 }
