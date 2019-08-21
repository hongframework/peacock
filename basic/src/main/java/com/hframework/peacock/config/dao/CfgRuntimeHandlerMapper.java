package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgRuntimeHandler;
import com.hframework.peacock.config.domain.model.CfgRuntimeHandler_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgRuntimeHandlerMapper {
    int countByExample(CfgRuntimeHandler_Example example);

    int deleteByExample(CfgRuntimeHandler_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgRuntimeHandler record);

    int insertSelective(CfgRuntimeHandler record);

    List<CfgRuntimeHandler> selectByExampleWithBLOBs(CfgRuntimeHandler_Example example);

    List<CfgRuntimeHandler> selectByExample(CfgRuntimeHandler_Example example);

    CfgRuntimeHandler selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgRuntimeHandler record, @Param("example") CfgRuntimeHandler_Example example);

    int updateByExampleWithBLOBs(@Param("record") CfgRuntimeHandler record, @Param("example") CfgRuntimeHandler_Example example);

    int updateByExample(@Param("record") CfgRuntimeHandler record, @Param("example") CfgRuntimeHandler_Example example);

    int updateByPrimaryKeySelective(CfgRuntimeHandler record);

    int updateByPrimaryKeyWithBLOBs(CfgRuntimeHandler record);

    int updateByPrimaryKey(CfgRuntimeHandler record);
}