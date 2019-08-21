package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgMgrProgram;
import com.hframework.peacock.config.domain.model.CfgMgrProgram_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgMgrProgramMapper {
    int countByExample(CfgMgrProgram_Example example);

    int deleteByExample(CfgMgrProgram_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgMgrProgram record);

    int insertSelective(CfgMgrProgram record);

    List<CfgMgrProgram> selectByExample(CfgMgrProgram_Example example);

    CfgMgrProgram selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgMgrProgram record, @Param("example") CfgMgrProgram_Example example);

    int updateByExample(@Param("record") CfgMgrProgram record, @Param("example") CfgMgrProgram_Example example);

    int updateByPrimaryKeySelective(CfgMgrProgram record);

    int updateByPrimaryKey(CfgMgrProgram record);
}