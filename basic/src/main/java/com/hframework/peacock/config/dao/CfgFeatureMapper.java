package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgFeature;
import com.hframework.peacock.config.domain.model.CfgFeature_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgFeatureMapper {
    int countByExample(CfgFeature_Example example);

    int deleteByExample(CfgFeature_Example example);

    int deleteByPrimaryKey(Integer id);

    int insert(CfgFeature record);

    int insertSelective(CfgFeature record);

    List<CfgFeature> selectByExample(CfgFeature_Example example);

    CfgFeature selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CfgFeature record, @Param("example") CfgFeature_Example example);

    int updateByExample(@Param("record") CfgFeature record, @Param("example") CfgFeature_Example example);

    int updateByPrimaryKeySelective(CfgFeature record);

    int updateByPrimaryKey(CfgFeature record);
}