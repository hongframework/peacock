package com.hframework.peacock.system.dao;

import com.hframework.peacock.system.domain.model.UserAuthorize;
import com.hframework.peacock.system.domain.model.UserAuthorize_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorizeMapper {
    int countByExample(UserAuthorize_Example example);

    int deleteByExample(UserAuthorize_Example example);

    int deleteByPrimaryKey(Long userAuthorizeId);

    int insert(UserAuthorize record);

    int insertSelective(UserAuthorize record);

    List<UserAuthorize> selectByExample(UserAuthorize_Example example);

    UserAuthorize selectByPrimaryKey(Long userAuthorizeId);

    int updateByExampleSelective(@Param("record") UserAuthorize record, @Param("example") UserAuthorize_Example example);

    int updateByExample(@Param("record") UserAuthorize record, @Param("example") UserAuthorize_Example example);

    int updateByPrimaryKeySelective(UserAuthorize record);

    int updateByPrimaryKey(UserAuthorize record);
}