package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgMgrModule;
import com.hframework.peacock.config.domain.model.CfgMgrModule_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgMgrModuleMapper {
    int countByExample(CfgMgrModule_Example example);

    int deleteByExample(CfgMgrModule_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgMgrModule record);

    int insertSelective(CfgMgrModule record);

    List<CfgMgrModule> selectByExample(CfgMgrModule_Example example);

    CfgMgrModule selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgMgrModule record, @Param("example") CfgMgrModule_Example example);

    int updateByExample(@Param("record") CfgMgrModule record, @Param("example") CfgMgrModule_Example example);

    int updateByPrimaryKeySelective(CfgMgrModule record);

    int updateByPrimaryKey(CfgMgrModule record);
}