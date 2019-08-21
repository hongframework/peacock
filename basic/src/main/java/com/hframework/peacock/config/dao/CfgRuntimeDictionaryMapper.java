package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgRuntimeDictionary;
import com.hframework.peacock.config.domain.model.CfgRuntimeDictionary_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgRuntimeDictionaryMapper {
    int countByExample(CfgRuntimeDictionary_Example example);

    int deleteByExample(CfgRuntimeDictionary_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgRuntimeDictionary record);

    int insertSelective(CfgRuntimeDictionary record);

    List<CfgRuntimeDictionary> selectByExample(CfgRuntimeDictionary_Example example);

    CfgRuntimeDictionary selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgRuntimeDictionary record, @Param("example") CfgRuntimeDictionary_Example example);

    int updateByExample(@Param("record") CfgRuntimeDictionary record, @Param("example") CfgRuntimeDictionary_Example example);

    int updateByPrimaryKeySelective(CfgRuntimeDictionary record);

    int updateByPrimaryKey(CfgRuntimeDictionary record);
}