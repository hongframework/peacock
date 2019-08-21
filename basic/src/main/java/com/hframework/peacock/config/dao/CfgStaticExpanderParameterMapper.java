package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgStaticExpanderParameter;
import com.hframework.peacock.config.domain.model.CfgStaticExpanderParameter_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgStaticExpanderParameterMapper {
    int countByExample(CfgStaticExpanderParameter_Example example);

    int deleteByExample(CfgStaticExpanderParameter_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgStaticExpanderParameter record);

    int insertSelective(CfgStaticExpanderParameter record);

    List<CfgStaticExpanderParameter> selectByExample(CfgStaticExpanderParameter_Example example);

    CfgStaticExpanderParameter selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgStaticExpanderParameter record, @Param("example") CfgStaticExpanderParameter_Example example);

    int updateByExample(@Param("record") CfgStaticExpanderParameter record, @Param("example") CfgStaticExpanderParameter_Example example);

    int updateByPrimaryKeySelective(CfgStaticExpanderParameter record);

    int updateByPrimaryKey(CfgStaticExpanderParameter record);
}