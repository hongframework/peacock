package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.ThirdDomain;
import com.hframework.peacock.config.domain.model.ThirdDomain_Example;


public interface IThirdDomainSV   {

  
    /**
    * 创建访问域
    * @param thirdDomain
    * @return
    * @throws Exception
    */
    public int create(ThirdDomain thirdDomain) throws  Exception;

    /**
    * 批量维护访问域
    * @param thirdDomains
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdDomain[] thirdDomains) throws  Exception;
    /**
    * 更新访问域
    * @param thirdDomain
    * @return
    * @throws Exception
    */
    public int update(ThirdDomain thirdDomain) throws  Exception;

    /**
    * 通过查询对象更新访问域
    * @param thirdDomain
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdDomain thirdDomain, ThirdDomain_Example example) throws  Exception;

    /**
    * 删除访问域
    * @param thirdDomain
    * @return
    * @throws Exception
    */
    public int delete(ThirdDomain thirdDomain) throws  Exception;


    /**
    * 删除访问域
    * @param thirdDomainId
    * @return
    * @throws Exception
    */
    public int delete(long thirdDomainId) throws  Exception;


    /**
    * 查找所有访问域
    * @return
    */
    public List<ThirdDomain> getThirdDomainAll()  throws  Exception;

    /**
    * 通过访问域ID查询访问域
    * @param thirdDomainId
    * @return
    * @throws Exception
    */
    public ThirdDomain getThirdDomainByPK(long thirdDomainId)  throws  Exception;

    /**
    * 通过MAP参数查询访问域
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdDomain> getThirdDomainListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询访问域
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdDomain> getThirdDomainListByExample(ThirdDomain_Example example) throws  Exception;


    /**
    * 通过MAP参数查询访问域数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdDomainCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询访问域数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdDomainCountByExample(ThirdDomain_Example example) throws  Exception;


 }
