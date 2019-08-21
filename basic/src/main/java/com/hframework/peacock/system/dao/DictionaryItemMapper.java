package com.hframework.peacock.system.dao;

import com.hframework.peacock.system.domain.model.DictionaryItem;
import com.hframework.peacock.system.domain.model.DictionaryItem_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryItemMapper {
    int countByExample(DictionaryItem_Example example);

    int deleteByExample(DictionaryItem_Example example);

    int deleteByPrimaryKey(Long dictionaryItemId);

    int insert(DictionaryItem record);

    int insertSelective(DictionaryItem record);

    List<DictionaryItem> selectByExample(DictionaryItem_Example example);

    DictionaryItem selectByPrimaryKey(Long dictionaryItemId);

    int updateByExampleSelective(@Param("record") DictionaryItem record, @Param("example") DictionaryItem_Example example);

    int updateByExample(@Param("record") DictionaryItem record, @Param("example") DictionaryItem_Example example);

    int updateByPrimaryKeySelective(DictionaryItem record);

    int updateByPrimaryKey(DictionaryItem record);
}