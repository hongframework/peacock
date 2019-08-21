package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgIndexMysql;
import com.hframework.peacock.config.domain.model.CfgIndexMysql_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgIndexMysqlMapper {
    int countByExample(CfgIndexMysql_Example example);

    int deleteByExample(CfgIndexMysql_Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(CfgIndexMysql record);

    int insertSelective(CfgIndexMysql record);

    List<CfgIndexMysql> selectByExampleWithBLOBs(CfgIndexMysql_Example example);

    List<CfgIndexMysql> selectByExample(CfgIndexMysql_Example example);

    CfgIndexMysql selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CfgIndexMysql record, @Param("example") CfgIndexMysql_Example example);

    int updateByExampleWithBLOBs(@Param("record") CfgIndexMysql record, @Param("example") CfgIndexMysql_Example example);

    int updateByExample(@Param("record") CfgIndexMysql record, @Param("example") CfgIndexMysql_Example example);

    int updateByPrimaryKeySelective(CfgIndexMysql record);

    int updateByPrimaryKeyWithBLOBs(CfgIndexMysql record);

    int updateByPrimaryKey(CfgIndexMysql record);
}