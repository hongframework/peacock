package com.hframework.peacock.system.service.interfaces;

import java.util.*;
import com.hframework.peacock.system.domain.model.RoleAuthorize;
import com.hframework.peacock.system.domain.model.RoleAuthorize_Example;


public interface IRoleAuthorizeSV   {

  
    /**
    * 创建瑙掕壊鎺堟潈
    * @param roleAuthorize
    * @return
    * @throws Exception
    */
    public int create(RoleAuthorize roleAuthorize) throws  Exception;

    /**
    * 批量维护瑙掕壊鎺堟潈
    * @param roleAuthorizes
    * @return
    * @throws Exception
    */
    public int batchOperate(RoleAuthorize[] roleAuthorizes) throws  Exception;
    /**
    * 更新瑙掕壊鎺堟潈
    * @param roleAuthorize
    * @return
    * @throws Exception
    */
    public int update(RoleAuthorize roleAuthorize) throws  Exception;

    /**
    * 通过查询对象更新瑙掕壊鎺堟潈
    * @param roleAuthorize
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(RoleAuthorize roleAuthorize, RoleAuthorize_Example example) throws  Exception;

    /**
    * 删除瑙掕壊鎺堟潈
    * @param roleAuthorize
    * @return
    * @throws Exception
    */
    public int delete(RoleAuthorize roleAuthorize) throws  Exception;


    /**
    * 删除瑙掕壊鎺堟潈
    * @param roleAuthorizeId
    * @return
    * @throws Exception
    */
    public int delete(long roleAuthorizeId) throws  Exception;


    /**
    * 查找所有瑙掕壊鎺堟潈
    * @return
    */
    public List<RoleAuthorize> getRoleAuthorizeAll()  throws  Exception;

    /**
    * 通过瑙掕壊鎺堟潈ID查询瑙掕壊鎺堟潈
    * @param roleAuthorizeId
    * @return
    * @throws Exception
    */
    public RoleAuthorize getRoleAuthorizeByPK(long roleAuthorizeId)  throws  Exception;

    /**
    * 通过MAP参数查询瑙掕壊鎺堟潈
    * @param params
    * @return
    * @throws Exception
    */
    public List<RoleAuthorize> getRoleAuthorizeListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询瑙掕壊鎺堟潈
    * @param example
    * @return
    * @throws Exception
    */
    public List<RoleAuthorize> getRoleAuthorizeListByExample(RoleAuthorize_Example example) throws  Exception;


    /**
    * 通过MAP参数查询瑙掕壊鎺堟潈数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getRoleAuthorizeCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询瑙掕壊鎺堟潈数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getRoleAuthorizeCountByExample(RoleAuthorize_Example example) throws  Exception;


 }
