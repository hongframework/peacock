package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgDatasouceRedis;
import com.hframework.peacock.config.domain.model.CfgDatasouceRedis_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgDatasouceRedisMapper {
    int countByExample(CfgDatasouceRedis_Example example);

    int deleteByExample(CfgDatasouceRedis_Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(CfgDatasouceRedis record);

    int insertSelective(CfgDatasouceRedis record);

    List<CfgDatasouceRedis> selectByExample(CfgDatasouceRedis_Example example);

    CfgDatasouceRedis selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CfgDatasouceRedis record, @Param("example") CfgDatasouceRedis_Example example);

    int updateByExample(@Param("record") CfgDatasouceRedis record, @Param("example") CfgDatasouceRedis_Example example);

    int updateByPrimaryKeySelective(CfgDatasouceRedis record);

    int updateByPrimaryKey(CfgDatasouceRedis record);
}