package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgDatasouceHbase;
import com.hframework.peacock.config.domain.model.CfgDatasouceHbase_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgDatasouceHbaseMapper {
    int countByExample(CfgDatasouceHbase_Example example);

    int deleteByExample(CfgDatasouceHbase_Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(CfgDatasouceHbase record);

    int insertSelective(CfgDatasouceHbase record);

    List<CfgDatasouceHbase> selectByExample(CfgDatasouceHbase_Example example);

    CfgDatasouceHbase selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CfgDatasouceHbase record, @Param("example") CfgDatasouceHbase_Example example);

    int updateByExample(@Param("record") CfgDatasouceHbase record, @Param("example") CfgDatasouceHbase_Example example);

    int updateByPrimaryKeySelective(CfgDatasouceHbase record);

    int updateByPrimaryKey(CfgDatasouceHbase record);
}