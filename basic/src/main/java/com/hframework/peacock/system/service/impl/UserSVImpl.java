package com.hframework.peacock.system.service.impl;

import java.util.*;

import com.hframework.peacock.system.service.interfaces.IUserSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.hframework.peacock.system.domain.model.User;
import com.hframework.peacock.system.domain.model.User_Example;
import com.hframework.peacock.system.dao.UserMapper;

@Service("iUserSV")
public class UserSVImpl  implements IUserSV {

	@Resource
	private UserMapper userMapper;
  


    /**
    * 创建鐢ㄦ埛
    * @param user
    * @return
    * @throws Exception
    */
    public int create(User user) throws Exception {
        return userMapper.insertSelective(user);
    }

    /**
    * 批量维护鐢ㄦ埛
    * @param users
    * @return
    * @throws Exception
    */
    public int batchOperate(User[] users) throws  Exception{
        int result = 0;
        if(users != null) {
            for (User user : users) {
                if(user.getUserId() == null) {
                    result += this.create(user);
                }else {
                    result += this.update(user);
                }
            }
        }
        return result;
    }

    /**
    * 更新鐢ㄦ埛
    * @param user
    * @return
    * @throws Exception
    */
    public int update(User user) throws  Exception {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
    * 通过查询对象更新鐢ㄦ埛
    * @param user
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(User user, User_Example example) throws  Exception {
        return userMapper.updateByExampleSelective(user, example);
    }

    /**
    * 删除鐢ㄦ埛
    * @param user
    * @return
    * @throws Exception
    */
    public int delete(User user) throws  Exception {
        return userMapper.deleteByPrimaryKey(user.getUserId());
    }

    /**
    * 删除鐢ㄦ埛
    * @param userId
    * @return
    * @throws Exception
    */
    public int delete(long userId) throws  Exception {
        return userMapper.deleteByPrimaryKey(userId);
    }

    /**
    * 查找所有鐢ㄦ埛
    * @return
    */
    public List<User> getUserAll()  throws  Exception {
        return userMapper.selectByExample(new User_Example());
    }

    /**
    * 通过鐢ㄦ埛ID查询鐢ㄦ埛
    * @param userId
    * @return
    * @throws Exception
    */
    public User getUserByPK(long userId)  throws  Exception {
        return userMapper.selectByPrimaryKey(userId);
    }


    /**
    * 通过MAP参数查询鐢ㄦ埛
    * @param params
    * @return
    * @throws Exception
    */
    public List<User> getUserListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询鐢ㄦ埛
    * @param example
    * @return
    * @throws Exception
    */
    public List<User> getUserListByExample(User_Example example) throws  Exception {
        return userMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询鐢ㄦ埛数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getUserCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询鐢ㄦ埛数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getUserCountByExample(User_Example example) throws  Exception {
        return userMapper.countByExample(example);
    }


  	//getter
 	
	public UserMapper getUserMapper(){
		return userMapper;
	}
	//setter
	public void setUserMapper(UserMapper userMapper){
    	this.userMapper = userMapper;
    }
}
