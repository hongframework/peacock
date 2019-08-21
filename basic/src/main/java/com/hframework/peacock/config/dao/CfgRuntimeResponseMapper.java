package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgRuntimeResponse;
import com.hframework.peacock.config.domain.model.CfgRuntimeResponse_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgRuntimeResponseMapper {
    int countByExample(CfgRuntimeResponse_Example example);

    int deleteByExample(CfgRuntimeResponse_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgRuntimeResponse record);

    int insertSelective(CfgRuntimeResponse record);

    List<CfgRuntimeResponse> selectByExample(CfgRuntimeResponse_Example example);

    CfgRuntimeResponse selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgRuntimeResponse record, @Param("example") CfgRuntimeResponse_Example example);

    int updateByExample(@Param("record") CfgRuntimeResponse record, @Param("example") CfgRuntimeResponse_Example example);

    int updateByPrimaryKeySelective(CfgRuntimeResponse record);

    int updateByPrimaryKey(CfgRuntimeResponse record);
}