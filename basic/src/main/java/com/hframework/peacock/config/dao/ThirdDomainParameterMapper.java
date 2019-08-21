package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.ThirdDomainParameter;
import com.hframework.peacock.config.domain.model.ThirdDomainParameter_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdDomainParameterMapper {
    int countByExample(ThirdDomainParameter_Example example);

    int deleteByExample(ThirdDomainParameter_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(ThirdDomainParameter record);

    int insertSelective(ThirdDomainParameter record);

    List<ThirdDomainParameter> selectByExample(ThirdDomainParameter_Example example);

    ThirdDomainParameter selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ThirdDomainParameter record, @Param("example") ThirdDomainParameter_Example example);

    int updateByExample(@Param("record") ThirdDomainParameter record, @Param("example") ThirdDomainParameter_Example example);

    int updateByPrimaryKeySelective(ThirdDomainParameter record);

    int updateByPrimaryKey(ThirdDomainParameter record);
}