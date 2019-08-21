package com.hframework.peacock.system.dao;

import com.hframework.peacock.system.domain.model.Role;
import com.hframework.peacock.system.domain.model.Role_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMapper {
    int countByExample(Role_Example example);

    int deleteByExample(Role_Example example);

    int deleteByPrimaryKey(Long roleId);

    int insert(Role record);

    int insertSelective(Role record);

    List<Role> selectByExample(Role_Example example);

    Role selectByPrimaryKey(Long roleId);

    int updateByExampleSelective(@Param("record") Role record, @Param("example") Role_Example example);

    int updateByExample(@Param("record") Role record, @Param("example") Role_Example example);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}