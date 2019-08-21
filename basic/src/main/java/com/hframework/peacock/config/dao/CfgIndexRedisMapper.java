package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgIndexRedis;
import com.hframework.peacock.config.domain.model.CfgIndexRedis_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgIndexRedisMapper {
    int countByExample(CfgIndexRedis_Example example);

    int deleteByExample(CfgIndexRedis_Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(CfgIndexRedis record);

    int insertSelective(CfgIndexRedis record);

    List<CfgIndexRedis> selectByExample(CfgIndexRedis_Example example);

    CfgIndexRedis selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CfgIndexRedis record, @Param("example") CfgIndexRedis_Example example);

    int updateByExample(@Param("record") CfgIndexRedis record, @Param("example") CfgIndexRedis_Example example);

    int updateByPrimaryKeySelective(CfgIndexRedis record);

    int updateByPrimaryKey(CfgIndexRedis record);
}