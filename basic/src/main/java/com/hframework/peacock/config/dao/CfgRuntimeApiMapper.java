package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgRuntimeApi;
import com.hframework.peacock.config.domain.model.CfgRuntimeApi_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgRuntimeApiMapper {
    int countByExample(CfgRuntimeApi_Example example);

    int deleteByExample(CfgRuntimeApi_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgRuntimeApi record);

    int insertSelective(CfgRuntimeApi record);

    List<CfgRuntimeApi> selectByExampleWithBLOBs(CfgRuntimeApi_Example example);

    List<CfgRuntimeApi> selectByExample(CfgRuntimeApi_Example example);

    CfgRuntimeApi selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgRuntimeApi record, @Param("example") CfgRuntimeApi_Example example);

    int updateByExampleWithBLOBs(@Param("record") CfgRuntimeApi record, @Param("example") CfgRuntimeApi_Example example);

    int updateByExample(@Param("record") CfgRuntimeApi record, @Param("example") CfgRuntimeApi_Example example);

    int updateByPrimaryKeySelective(CfgRuntimeApi record);

    int updateByPrimaryKeyWithBLOBs(CfgRuntimeApi record);

    int updateByPrimaryKey(CfgRuntimeApi record);
}