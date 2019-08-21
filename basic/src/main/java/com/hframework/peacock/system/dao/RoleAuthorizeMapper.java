package com.hframework.peacock.system.dao;

import com.hframework.peacock.system.domain.model.RoleAuthorize;
import com.hframework.peacock.system.domain.model.RoleAuthorize_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleAuthorizeMapper {
    int countByExample(RoleAuthorize_Example example);

    int deleteByExample(RoleAuthorize_Example example);

    int deleteByPrimaryKey(Long roleAuthorizeId);

    int insert(RoleAuthorize record);

    int insertSelective(RoleAuthorize record);

    List<RoleAuthorize> selectByExample(RoleAuthorize_Example example);

    RoleAuthorize selectByPrimaryKey(Long roleAuthorizeId);

    int updateByExampleSelective(@Param("record") RoleAuthorize record, @Param("example") RoleAuthorize_Example example);

    int updateByExample(@Param("record") RoleAuthorize record, @Param("example") RoleAuthorize_Example example);

    int updateByPrimaryKeySelective(RoleAuthorize record);

    int updateByPrimaryKey(RoleAuthorize record);
}