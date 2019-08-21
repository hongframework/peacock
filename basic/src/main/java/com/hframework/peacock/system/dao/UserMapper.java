package com.hframework.peacock.system.dao;

import com.hframework.peacock.system.domain.model.User;
import com.hframework.peacock.system.domain.model.User_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int countByExample(User_Example example);

    int deleteByExample(User_Example example);

    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(User_Example example);

    User selectByPrimaryKey(Long userId);

    int updateByExampleSelective(@Param("record") User record, @Param("example") User_Example example);

    int updateByExample(@Param("record") User record, @Param("example") User_Example example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}