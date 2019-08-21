package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.ThirdCommonParameter;
import com.hframework.peacock.config.domain.model.ThirdCommonParameter_Example;


public interface IThirdCommonParameterSV   {

  
    /**
    * 创建域通用参数
    * @param thirdCommonParameter
    * @return
    * @throws Exception
    */
    public int create(ThirdCommonParameter thirdCommonParameter) throws  Exception;

    /**
    * 批量维护域通用参数
    * @param thirdCommonParameters
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdCommonParameter[] thirdCommonParameters) throws  Exception;
    /**
    * 更新域通用参数
    * @param thirdCommonParameter
    * @return
    * @throws Exception
    */
    public int update(ThirdCommonParameter thirdCommonParameter) throws  Exception;

    /**
    * 通过查询对象更新域通用参数
    * @param thirdCommonParameter
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdCommonParameter thirdCommonParameter, ThirdCommonParameter_Example example) throws  Exception;

    /**
    * 删除域通用参数
    * @param thirdCommonParameter
    * @return
    * @throws Exception
    */
    public int delete(ThirdCommonParameter thirdCommonParameter) throws  Exception;


    /**
    * 删除域通用参数
    * @param thirdCommonParameterId
    * @return
    * @throws Exception
    */
    public int delete(long thirdCommonParameterId) throws  Exception;


    /**
    * 查找所有域通用参数
    * @return
    */
    public List<ThirdCommonParameter> getThirdCommonParameterAll()  throws  Exception;

    /**
    * 通过域通用参数ID查询域通用参数
    * @param thirdCommonParameterId
    * @return
    * @throws Exception
    */
    public ThirdCommonParameter getThirdCommonParameterByPK(long thirdCommonParameterId)  throws  Exception;

    /**
    * 通过MAP参数查询域通用参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdCommonParameter> getThirdCommonParameterListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询域通用参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdCommonParameter> getThirdCommonParameterListByExample(ThirdCommonParameter_Example example) throws  Exception;


    /**
    * 通过MAP参数查询域通用参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdCommonParameterCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询域通用参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdCommonParameterCountByExample(ThirdCommonParameter_Example example) throws  Exception;


 }
