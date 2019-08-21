package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.ThirdPublicRule;
import com.hframework.peacock.config.domain.model.ThirdPublicRule_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPublicRuleMapper {
    int countByExample(ThirdPublicRule_Example example);

    int deleteByExample(ThirdPublicRule_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(ThirdPublicRule record);

    int insertSelective(ThirdPublicRule record);

    List<ThirdPublicRule> selectByExample(ThirdPublicRule_Example example);

    ThirdPublicRule selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ThirdPublicRule record, @Param("example") ThirdPublicRule_Example example);

    int updateByExample(@Param("record") ThirdPublicRule record, @Param("example") ThirdPublicRule_Example example);

    int updateByPrimaryKeySelective(ThirdPublicRule record);

    int updateByPrimaryKey(ThirdPublicRule record);
}