package com.hframework.peacock.system.service.impl;

import java.util.*;

import com.hframework.peacock.system.service.interfaces.IRoleAuthorizeSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.hframework.peacock.system.domain.model.RoleAuthorize;
import com.hframework.peacock.system.domain.model.RoleAuthorize_Example;
import com.hframework.peacock.system.dao.RoleAuthorizeMapper;

@Service("iRoleAuthorizeSV")
public class RoleAuthorizeSVImpl  implements IRoleAuthorizeSV {

	@Resource
	private RoleAuthorizeMapper roleAuthorizeMapper;
  


    /**
    * 创建瑙掕壊鎺堟潈
    * @param roleAuthorize
    * @return
    * @throws Exception
    */
    public int create(RoleAuthorize roleAuthorize) throws Exception {
        return roleAuthorizeMapper.insertSelective(roleAuthorize);
    }

    /**
    * 批量维护瑙掕壊鎺堟潈
    * @param roleAuthorizes
    * @return
    * @throws Exception
    */
    public int batchOperate(RoleAuthorize[] roleAuthorizes) throws  Exception{
        int result = 0;
        if(roleAuthorizes != null) {
            for (RoleAuthorize roleAuthorize : roleAuthorizes) {
                if(roleAuthorize.getRoleAuthorizeId() == null) {
                    result += this.create(roleAuthorize);
                }else {
                    result += this.update(roleAuthorize);
                }
            }
        }
        return result;
    }

    /**
    * 更新瑙掕壊鎺堟潈
    * @param roleAuthorize
    * @return
    * @throws Exception
    */
    public int update(RoleAuthorize roleAuthorize) throws  Exception {
        return roleAuthorizeMapper.updateByPrimaryKeySelective(roleAuthorize);
    }

    /**
    * 通过查询对象更新瑙掕壊鎺堟潈
    * @param roleAuthorize
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(RoleAuthorize roleAuthorize, RoleAuthorize_Example example) throws  Exception {
        return roleAuthorizeMapper.updateByExampleSelective(roleAuthorize, example);
    }

    /**
    * 删除瑙掕壊鎺堟潈
    * @param roleAuthorize
    * @return
    * @throws Exception
    */
    public int delete(RoleAuthorize roleAuthorize) throws  Exception {
        return roleAuthorizeMapper.deleteByPrimaryKey(roleAuthorize.getRoleAuthorizeId());
    }

    /**
    * 删除瑙掕壊鎺堟潈
    * @param roleAuthorizeId
    * @return
    * @throws Exception
    */
    public int delete(long roleAuthorizeId) throws  Exception {
        return roleAuthorizeMapper.deleteByPrimaryKey(roleAuthorizeId);
    }

    /**
    * 查找所有瑙掕壊鎺堟潈
    * @return
    */
    public List<RoleAuthorize> getRoleAuthorizeAll()  throws  Exception {
        return roleAuthorizeMapper.selectByExample(new RoleAuthorize_Example());
    }

    /**
    * 通过瑙掕壊鎺堟潈ID查询瑙掕壊鎺堟潈
    * @param roleAuthorizeId
    * @return
    * @throws Exception
    */
    public RoleAuthorize getRoleAuthorizeByPK(long roleAuthorizeId)  throws  Exception {
        return roleAuthorizeMapper.selectByPrimaryKey(roleAuthorizeId);
    }


    /**
    * 通过MAP参数查询瑙掕壊鎺堟潈
    * @param params
    * @return
    * @throws Exception
    */
    public List<RoleAuthorize> getRoleAuthorizeListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询瑙掕壊鎺堟潈
    * @param example
    * @return
    * @throws Exception
    */
    public List<RoleAuthorize> getRoleAuthorizeListByExample(RoleAuthorize_Example example) throws  Exception {
        return roleAuthorizeMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询瑙掕壊鎺堟潈数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getRoleAuthorizeCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询瑙掕壊鎺堟潈数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getRoleAuthorizeCountByExample(RoleAuthorize_Example example) throws  Exception {
        return roleAuthorizeMapper.countByExample(example);
    }


  	//getter
 	
	public RoleAuthorizeMapper getRoleAuthorizeMapper(){
		return roleAuthorizeMapper;
	}
	//setter
	public void setRoleAuthorizeMapper(RoleAuthorizeMapper roleAuthorizeMapper){
    	this.roleAuthorizeMapper = roleAuthorizeMapper;
    }
}
