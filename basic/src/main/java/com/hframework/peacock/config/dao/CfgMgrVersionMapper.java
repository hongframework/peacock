package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgMgrVersion;
import com.hframework.peacock.config.domain.model.CfgMgrVersion_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgMgrVersionMapper {
    int countByExample(CfgMgrVersion_Example example);

    int deleteByExample(CfgMgrVersion_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgMgrVersion record);

    int insertSelective(CfgMgrVersion record);

    List<CfgMgrVersion> selectByExample(CfgMgrVersion_Example example);

    CfgMgrVersion selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgMgrVersion record, @Param("example") CfgMgrVersion_Example example);

    int updateByExample(@Param("record") CfgMgrVersion record, @Param("example") CfgMgrVersion_Example example);

    int updateByPrimaryKeySelective(CfgMgrVersion record);

    int updateByPrimaryKey(CfgMgrVersion record);
}