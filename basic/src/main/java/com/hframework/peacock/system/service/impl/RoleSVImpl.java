package com.hframework.peacock.system.service.impl;

import java.util.*;

import com.hframework.peacock.system.service.interfaces.IRoleSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.hframework.peacock.system.domain.model.Role;
import com.hframework.peacock.system.domain.model.Role_Example;
import com.hframework.peacock.system.dao.RoleMapper;

@Service("iRoleSV")
public class RoleSVImpl  implements IRoleSV {

	@Resource
	private RoleMapper roleMapper;
  


    /**
    * 创建瑙掕壊
    * @param role
    * @return
    * @throws Exception
    */
    public int create(Role role) throws Exception {
        return roleMapper.insertSelective(role);
    }

    /**
    * 批量维护瑙掕壊
    * @param roles
    * @return
    * @throws Exception
    */
    public int batchOperate(Role[] roles) throws  Exception{
        int result = 0;
        if(roles != null) {
            for (Role role : roles) {
                if(role.getRoleId() == null) {
                    result += this.create(role);
                }else {
                    result += this.update(role);
                }
            }
        }
        return result;
    }

    /**
    * 更新瑙掕壊
    * @param role
    * @return
    * @throws Exception
    */
    public int update(Role role) throws  Exception {
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    /**
    * 通过查询对象更新瑙掕壊
    * @param role
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(Role role, Role_Example example) throws  Exception {
        return roleMapper.updateByExampleSelective(role, example);
    }

    /**
    * 删除瑙掕壊
    * @param role
    * @return
    * @throws Exception
    */
    public int delete(Role role) throws  Exception {
        return roleMapper.deleteByPrimaryKey(role.getRoleId());
    }

    /**
    * 删除瑙掕壊
    * @param roleId
    * @return
    * @throws Exception
    */
    public int delete(long roleId) throws  Exception {
        return roleMapper.deleteByPrimaryKey(roleId);
    }

    /**
    * 查找所有瑙掕壊
    * @return
    */
    public List<Role> getRoleAll()  throws  Exception {
        return roleMapper.selectByExample(new Role_Example());
    }

    /**
    * 通过瑙掕壊ID查询瑙掕壊
    * @param roleId
    * @return
    * @throws Exception
    */
    public Role getRoleByPK(long roleId)  throws  Exception {
        return roleMapper.selectByPrimaryKey(roleId);
    }


    /**
    * 通过MAP参数查询瑙掕壊
    * @param params
    * @return
    * @throws Exception
    */
    public List<Role> getRoleListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询瑙掕壊
    * @param example
    * @return
    * @throws Exception
    */
    public List<Role> getRoleListByExample(Role_Example example) throws  Exception {
        return roleMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询瑙掕壊数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getRoleCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询瑙掕壊数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getRoleCountByExample(Role_Example example) throws  Exception {
        return roleMapper.countByExample(example);
    }


  	//getter
 	
	public RoleMapper getRoleMapper(){
		return roleMapper;
	}
	//setter
	public void setRoleMapper(RoleMapper roleMapper){
    	this.roleMapper = roleMapper;
    }
}
