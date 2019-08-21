package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgIndex;
import com.hframework.peacock.config.domain.model.CfgIndex_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgIndexMapper {
    int countByExample(CfgIndex_Example example);

    int deleteByExample(CfgIndex_Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(CfgIndex record);

    int insertSelective(CfgIndex record);

    List<CfgIndex> selectByExample(CfgIndex_Example example);

    CfgIndex selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CfgIndex record, @Param("example") CfgIndex_Example example);

    int updateByExample(@Param("record") CfgIndex record, @Param("example") CfgIndex_Example example);

    int updateByPrimaryKeySelective(CfgIndex record);

    int updateByPrimaryKey(CfgIndex record);
}