package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.ThirdHelpParameter;
import com.hframework.peacock.config.domain.model.ThirdHelpParameter_Example;


public interface IThirdHelpParameterSV   {

  
    /**
    * 创建访问域常规参数
    * @param thirdHelpParameter
    * @return
    * @throws Exception
    */
    public int create(ThirdHelpParameter thirdHelpParameter) throws  Exception;

    /**
    * 批量维护访问域常规参数
    * @param thirdHelpParameters
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdHelpParameter[] thirdHelpParameters) throws  Exception;
    /**
    * 更新访问域常规参数
    * @param thirdHelpParameter
    * @return
    * @throws Exception
    */
    public int update(ThirdHelpParameter thirdHelpParameter) throws  Exception;

    /**
    * 通过查询对象更新访问域常规参数
    * @param thirdHelpParameter
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdHelpParameter thirdHelpParameter, ThirdHelpParameter_Example example) throws  Exception;

    /**
    * 删除访问域常规参数
    * @param thirdHelpParameter
    * @return
    * @throws Exception
    */
    public int delete(ThirdHelpParameter thirdHelpParameter) throws  Exception;


    /**
    * 删除访问域常规参数
    * @param thirdHelpParameterId
    * @return
    * @throws Exception
    */
    public int delete(long thirdHelpParameterId) throws  Exception;


    /**
    * 查找所有访问域常规参数
    * @return
    */
    public List<ThirdHelpParameter> getThirdHelpParameterAll()  throws  Exception;

    /**
    * 通过访问域常规参数ID查询访问域常规参数
    * @param thirdHelpParameterId
    * @return
    * @throws Exception
    */
    public ThirdHelpParameter getThirdHelpParameterByPK(long thirdHelpParameterId)  throws  Exception;

    /**
    * 通过MAP参数查询访问域常规参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdHelpParameter> getThirdHelpParameterListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询访问域常规参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdHelpParameter> getThirdHelpParameterListByExample(ThirdHelpParameter_Example example) throws  Exception;


    /**
    * 通过MAP参数查询访问域常规参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdHelpParameterCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询访问域常规参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdHelpParameterCountByExample(ThirdHelpParameter_Example example) throws  Exception;


 }
