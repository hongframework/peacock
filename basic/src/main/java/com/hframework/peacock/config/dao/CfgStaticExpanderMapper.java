package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgStaticExpander;
import com.hframework.peacock.config.domain.model.CfgStaticExpander_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgStaticExpanderMapper {
    int countByExample(CfgStaticExpander_Example example);

    int deleteByExample(CfgStaticExpander_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgStaticExpander record);

    int insertSelective(CfgStaticExpander record);

    List<CfgStaticExpander> selectByExample(CfgStaticExpander_Example example);

    CfgStaticExpander selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgStaticExpander record, @Param("example") CfgStaticExpander_Example example);

    int updateByExample(@Param("record") CfgStaticExpander record, @Param("example") CfgStaticExpander_Example example);

    int updateByPrimaryKeySelective(CfgStaticExpander record);

    int updateByPrimaryKey(CfgStaticExpander record);
}