package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgRuntimeTrace;
import com.hframework.peacock.config.domain.model.CfgRuntimeTrace_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgRuntimeTraceMapper {
    int countByExample(CfgRuntimeTrace_Example example);

    int deleteByExample(CfgRuntimeTrace_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgRuntimeTrace record);

    int insertSelective(CfgRuntimeTrace record);

    List<CfgRuntimeTrace> selectByExampleWithBLOBs(CfgRuntimeTrace_Example example);

    List<CfgRuntimeTrace> selectByExample(CfgRuntimeTrace_Example example);

    CfgRuntimeTrace selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgRuntimeTrace record, @Param("example") CfgRuntimeTrace_Example example);

    int updateByExampleWithBLOBs(@Param("record") CfgRuntimeTrace record, @Param("example") CfgRuntimeTrace_Example example);

    int updateByExample(@Param("record") CfgRuntimeTrace record, @Param("example") CfgRuntimeTrace_Example example);

    int updateByPrimaryKeySelective(CfgRuntimeTrace record);

    int updateByPrimaryKeyWithBLOBs(CfgRuntimeTrace record);

    int updateByPrimaryKey(CfgRuntimeTrace record);
}