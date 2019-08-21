package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgRuntimeParameter;
import com.hframework.peacock.config.domain.model.CfgRuntimeParameter_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgRuntimeParameterMapper {
    int countByExample(CfgRuntimeParameter_Example example);

    int deleteByExample(CfgRuntimeParameter_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgRuntimeParameter record);

    int insertSelective(CfgRuntimeParameter record);

    List<CfgRuntimeParameter> selectByExample(CfgRuntimeParameter_Example example);

    CfgRuntimeParameter selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgRuntimeParameter record, @Param("example") CfgRuntimeParameter_Example example);

    int updateByExample(@Param("record") CfgRuntimeParameter record, @Param("example") CfgRuntimeParameter_Example example);

    int updateByPrimaryKeySelective(CfgRuntimeParameter record);

    int updateByPrimaryKey(CfgRuntimeParameter record);
}