package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgApiDef;
import com.hframework.peacock.config.domain.model.CfgApiDef_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgApiDefMapper {
    int countByExample(CfgApiDef_Example example);

    int deleteByExample(CfgApiDef_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgApiDef record);

    int insertSelective(CfgApiDef record);

    List<CfgApiDef> selectByExample(CfgApiDef_Example example);

    CfgApiDef selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgApiDef record, @Param("example") CfgApiDef_Example example);

    int updateByExample(@Param("record") CfgApiDef record, @Param("example") CfgApiDef_Example example);

    int updateByPrimaryKeySelective(CfgApiDef record);

    int updateByPrimaryKey(CfgApiDef record);
}