package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgIndexHbase;
import com.hframework.peacock.config.domain.model.CfgIndexHbase_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgIndexHbaseMapper {
    int countByExample(CfgIndexHbase_Example example);

    int deleteByExample(CfgIndexHbase_Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(CfgIndexHbase record);

    int insertSelective(CfgIndexHbase record);

    List<CfgIndexHbase> selectByExample(CfgIndexHbase_Example example);

    CfgIndexHbase selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CfgIndexHbase record, @Param("example") CfgIndexHbase_Example example);

    int updateByExample(@Param("record") CfgIndexHbase record, @Param("example") CfgIndexHbase_Example example);

    int updateByPrimaryKeySelective(CfgIndexHbase record);

    int updateByPrimaryKey(CfgIndexHbase record);
}