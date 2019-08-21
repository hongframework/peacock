package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.ThirdHelpParameter;
import com.hframework.peacock.config.domain.model.ThirdHelpParameter_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdHelpParameterMapper {
    int countByExample(ThirdHelpParameter_Example example);

    int deleteByExample(ThirdHelpParameter_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(ThirdHelpParameter record);

    int insertSelective(ThirdHelpParameter record);

    List<ThirdHelpParameter> selectByExample(ThirdHelpParameter_Example example);

    ThirdHelpParameter selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ThirdHelpParameter record, @Param("example") ThirdHelpParameter_Example example);

    int updateByExample(@Param("record") ThirdHelpParameter record, @Param("example") ThirdHelpParameter_Example example);

    int updateByPrimaryKeySelective(ThirdHelpParameter record);

    int updateByPrimaryKey(ThirdHelpParameter record);
}