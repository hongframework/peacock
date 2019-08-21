package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgRuntimeRule;
import com.hframework.peacock.config.domain.model.CfgRuntimeRule_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgRuntimeRuleMapper {
    int countByExample(CfgRuntimeRule_Example example);

    int deleteByExample(CfgRuntimeRule_Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(CfgRuntimeRule record);

    int insertSelective(CfgRuntimeRule record);

    List<CfgRuntimeRule> selectByExampleWithBLOBs(CfgRuntimeRule_Example example);

    List<CfgRuntimeRule> selectByExample(CfgRuntimeRule_Example example);

    CfgRuntimeRule selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CfgRuntimeRule record, @Param("example") CfgRuntimeRule_Example example);

    int updateByExampleWithBLOBs(@Param("record") CfgRuntimeRule record, @Param("example") CfgRuntimeRule_Example example);

    int updateByExample(@Param("record") CfgRuntimeRule record, @Param("example") CfgRuntimeRule_Example example);

    int updateByPrimaryKeySelective(CfgRuntimeRule record);

    int updateByPrimaryKeyWithBLOBs(CfgRuntimeRule record);

    int updateByPrimaryKey(CfgRuntimeRule record);
}