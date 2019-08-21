package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.ThirdCommonParameter;
import com.hframework.peacock.config.domain.model.ThirdCommonParameter_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdCommonParameterMapper {
    int countByExample(ThirdCommonParameter_Example example);

    int deleteByExample(ThirdCommonParameter_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(ThirdCommonParameter record);

    int insertSelective(ThirdCommonParameter record);

    List<ThirdCommonParameter> selectByExample(ThirdCommonParameter_Example example);

    ThirdCommonParameter selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ThirdCommonParameter record, @Param("example") ThirdCommonParameter_Example example);

    int updateByExample(@Param("record") ThirdCommonParameter record, @Param("example") ThirdCommonParameter_Example example);

    int updateByPrimaryKeySelective(ThirdCommonParameter record);

    int updateByPrimaryKey(ThirdCommonParameter record);
}