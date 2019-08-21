package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgNode;
import com.hframework.peacock.config.domain.model.CfgNode_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgNodeMapper {
    int countByExample(CfgNode_Example example);

    int deleteByExample(CfgNode_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgNode record);

    int insertSelective(CfgNode record);

    List<CfgNode> selectByExample(CfgNode_Example example);

    CfgNode selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgNode record, @Param("example") CfgNode_Example example);

    int updateByExample(@Param("record") CfgNode record, @Param("example") CfgNode_Example example);

    int updateByPrimaryKeySelective(CfgNode record);

    int updateByPrimaryKey(CfgNode record);
}