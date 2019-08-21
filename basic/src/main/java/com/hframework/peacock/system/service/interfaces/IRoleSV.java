package com.hframework.peacock.system.service.interfaces;

import java.util.*;
import com.hframework.peacock.system.domain.model.Role;
import com.hframework.peacock.system.domain.model.Role_Example;


public interface IRoleSV   {

  
    /**
    * 创建瑙掕壊
    * @param role
    * @return
    * @throws Exception
    */
    public int create(Role role) throws  Exception;

    /**
    * 批量维护瑙掕壊
    * @param roles
    * @return
    * @throws Exception
    */
    public int batchOperate(Role[] roles) throws  Exception;
    /**
    * 更新瑙掕壊
    * @param role
    * @return
    * @throws Exception
    */
    public int update(Role role) throws  Exception;

    /**
    * 通过查询对象更新瑙掕壊
    * @param role
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(Role role, Role_Example example) throws  Exception;

    /**
    * 删除瑙掕壊
    * @param role
    * @return
    * @throws Exception
    */
    public int delete(Role role) throws  Exception;


    /**
    * 删除瑙掕壊
    * @param roleId
    * @return
    * @throws Exception
    */
    public int delete(long roleId) throws  Exception;


    /**
    * 查找所有瑙掕壊
    * @return
    */
    public List<Role> getRoleAll()  throws  Exception;

    /**
    * 通过瑙掕壊ID查询瑙掕壊
    * @param roleId
    * @return
    * @throws Exception
    */
    public Role getRoleByPK(long roleId)  throws  Exception;

    /**
    * 通过MAP参数查询瑙掕壊
    * @param params
    * @return
    * @throws Exception
    */
    public List<Role> getRoleListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询瑙掕壊
    * @param example
    * @return
    * @throws Exception
    */
    public List<Role> getRoleListByExample(Role_Example example) throws  Exception;


    /**
    * 通过MAP参数查询瑙掕壊数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getRoleCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询瑙掕壊数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getRoleCountByExample(Role_Example example) throws  Exception;


 }
