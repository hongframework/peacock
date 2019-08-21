package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.ThirdDomain;
import com.hframework.peacock.config.domain.model.ThirdDomain_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdDomainMapper {
    int countByExample(ThirdDomain_Example example);

    int deleteByExample(ThirdDomain_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(ThirdDomain record);

    int insertSelective(ThirdDomain record);

    List<ThirdDomain> selectByExample(ThirdDomain_Example example);

    ThirdDomain selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ThirdDomain record, @Param("example") ThirdDomain_Example example);

    int updateByExample(@Param("record") ThirdDomain record, @Param("example") ThirdDomain_Example example);

    int updateByPrimaryKeySelective(ThirdDomain record);

    int updateByPrimaryKey(ThirdDomain record);
}