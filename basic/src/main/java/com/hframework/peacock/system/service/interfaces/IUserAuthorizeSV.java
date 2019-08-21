package com.hframework.peacock.system.service.interfaces;

import java.util.*;
import com.hframework.peacock.system.domain.model.UserAuthorize;
import com.hframework.peacock.system.domain.model.UserAuthorize_Example;


public interface IUserAuthorizeSV   {

  
    /**
    * 创建鐢ㄦ埛鎺堟潈
    * @param userAuthorize
    * @return
    * @throws Exception
    */
    public int create(UserAuthorize userAuthorize) throws  Exception;

    /**
    * 批量维护鐢ㄦ埛鎺堟潈
    * @param userAuthorizes
    * @return
    * @throws Exception
    */
    public int batchOperate(UserAuthorize[] userAuthorizes) throws  Exception;
    /**
    * 更新鐢ㄦ埛鎺堟潈
    * @param userAuthorize
    * @return
    * @throws Exception
    */
    public int update(UserAuthorize userAuthorize) throws  Exception;

    /**
    * 通过查询对象更新鐢ㄦ埛鎺堟潈
    * @param userAuthorize
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(UserAuthorize userAuthorize, UserAuthorize_Example example) throws  Exception;

    /**
    * 删除鐢ㄦ埛鎺堟潈
    * @param userAuthorize
    * @return
    * @throws Exception
    */
    public int delete(UserAuthorize userAuthorize) throws  Exception;


    /**
    * 删除鐢ㄦ埛鎺堟潈
    * @param userAuthorizeId
    * @return
    * @throws Exception
    */
    public int delete(long userAuthorizeId) throws  Exception;


    /**
    * 查找所有鐢ㄦ埛鎺堟潈
    * @return
    */
    public List<UserAuthorize> getUserAuthorizeAll()  throws  Exception;

    /**
    * 通过鐢ㄦ埛鎺堟潈ID查询鐢ㄦ埛鎺堟潈
    * @param userAuthorizeId
    * @return
    * @throws Exception
    */
    public UserAuthorize getUserAuthorizeByPK(long userAuthorizeId)  throws  Exception;

    /**
    * 通过MAP参数查询鐢ㄦ埛鎺堟潈
    * @param params
    * @return
    * @throws Exception
    */
    public List<UserAuthorize> getUserAuthorizeListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询鐢ㄦ埛鎺堟潈
    * @param example
    * @return
    * @throws Exception
    */
    public List<UserAuthorize> getUserAuthorizeListByExample(UserAuthorize_Example example) throws  Exception;


    /**
    * 通过MAP参数查询鐢ㄦ埛鎺堟潈数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getUserAuthorizeCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询鐢ㄦ埛鎺堟潈数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getUserAuthorizeCountByExample(UserAuthorize_Example example) throws  Exception;


 }
