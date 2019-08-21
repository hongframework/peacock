package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.ThirdDomainParameter;
import com.hframework.peacock.config.domain.model.ThirdDomainParameter_Example;


public interface IThirdDomainParameterSV   {

  
    /**
    * 创建访问域参数
    * @param thirdDomainParameter
    * @return
    * @throws Exception
    */
    public int create(ThirdDomainParameter thirdDomainParameter) throws  Exception;

    /**
    * 批量维护访问域参数
    * @param thirdDomainParameters
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdDomainParameter[] thirdDomainParameters) throws  Exception;
    /**
    * 更新访问域参数
    * @param thirdDomainParameter
    * @return
    * @throws Exception
    */
    public int update(ThirdDomainParameter thirdDomainParameter) throws  Exception;

    /**
    * 通过查询对象更新访问域参数
    * @param thirdDomainParameter
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdDomainParameter thirdDomainParameter, ThirdDomainParameter_Example example) throws  Exception;

    /**
    * 删除访问域参数
    * @param thirdDomainParameter
    * @return
    * @throws Exception
    */
    public int delete(ThirdDomainParameter thirdDomainParameter) throws  Exception;


    /**
    * 删除访问域参数
    * @param thirdDomainParameterId
    * @return
    * @throws Exception
    */
    public int delete(long thirdDomainParameterId) throws  Exception;


    /**
    * 查找所有访问域参数
    * @return
    */
    public List<ThirdDomainParameter> getThirdDomainParameterAll()  throws  Exception;

    /**
    * 通过访问域参数ID查询访问域参数
    * @param thirdDomainParameterId
    * @return
    * @throws Exception
    */
    public ThirdDomainParameter getThirdDomainParameterByPK(long thirdDomainParameterId)  throws  Exception;

    /**
    * 通过MAP参数查询访问域参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdDomainParameter> getThirdDomainParameterListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询访问域参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdDomainParameter> getThirdDomainParameterListByExample(ThirdDomainParameter_Example example) throws  Exception;


    /**
    * 通过MAP参数查询访问域参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdDomainParameterCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询访问域参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdDomainParameterCountByExample(ThirdDomainParameter_Example example) throws  Exception;


 }
