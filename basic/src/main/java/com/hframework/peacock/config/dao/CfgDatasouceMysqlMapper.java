package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgDatasouceMysql;
import com.hframework.peacock.config.domain.model.CfgDatasouceMysql_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgDatasouceMysqlMapper {
    int countByExample(CfgDatasouceMysql_Example example);

    int deleteByExample(CfgDatasouceMysql_Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(CfgDatasouceMysql record);

    int insertSelective(CfgDatasouceMysql record);

    List<CfgDatasouceMysql> selectByExample(CfgDatasouceMysql_Example example);

    CfgDatasouceMysql selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CfgDatasouceMysql record, @Param("example") CfgDatasouceMysql_Example example);

    int updateByExample(@Param("record") CfgDatasouceMysql record, @Param("example") CfgDatasouceMysql_Example example);

    int updateByPrimaryKeySelective(CfgDatasouceMysql record);

    int updateByPrimaryKey(CfgDatasouceMysql record);
}