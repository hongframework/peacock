package com.hframework.peacock.system.dao;

import com.hframework.peacock.system.domain.model.Dictionary;
import com.hframework.peacock.system.domain.model.Dictionary_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryMapper {
    int countByExample(Dictionary_Example example);

    int deleteByExample(Dictionary_Example example);

    int deleteByPrimaryKey(Long dictionaryId);

    int insert(Dictionary record);

    int insertSelective(Dictionary record);

    List<Dictionary> selectByExample(Dictionary_Example example);

    Dictionary selectByPrimaryKey(Long dictionaryId);

    int updateByExampleSelective(@Param("record") Dictionary record, @Param("example") Dictionary_Example example);

    int updateByExample(@Param("record") Dictionary record, @Param("example") Dictionary_Example example);

    int updateByPrimaryKeySelective(Dictionary record);

    int updateByPrimaryKey(Dictionary record);
}