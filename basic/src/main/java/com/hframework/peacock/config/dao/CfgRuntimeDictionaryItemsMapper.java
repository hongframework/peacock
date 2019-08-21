package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgRuntimeDictionaryItems;
import com.hframework.peacock.config.domain.model.CfgRuntimeDictionaryItems_Example;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgRuntimeDictionaryItemsMapper {
    int countByExample(CfgRuntimeDictionaryItems_Example example);

    int deleteByExample(CfgRuntimeDictionaryItems_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgRuntimeDictionaryItems record);

    int insertSelective(CfgRuntimeDictionaryItems record);

    List<CfgRuntimeDictionaryItems> selectByExample(CfgRuntimeDictionaryItems_Example example);

    CfgRuntimeDictionaryItems selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgRuntimeDictionaryItems record, @Param("example") CfgRuntimeDictionaryItems_Example example);

    int updateByExample(@Param("record") CfgRuntimeDictionaryItems record, @Param("example") CfgRuntimeDictionaryItems_Example example);

    int updateByPrimaryKeySelective(CfgRuntimeDictionaryItems record);

    int updateByPrimaryKey(CfgRuntimeDictionaryItems record);
}