package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgConsumerAuth;
import com.hframework.peacock.config.domain.model.CfgConsumerAuth_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgConsumerAuthMapper {
    int countByExample(CfgConsumerAuth_Example example);

    int deleteByExample(CfgConsumerAuth_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgConsumerAuth record);

    int insertSelective(CfgConsumerAuth record);

    List<CfgConsumerAuth> selectByExample(CfgConsumerAuth_Example example);

    CfgConsumerAuth selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgConsumerAuth record, @Param("example") CfgConsumerAuth_Example example);

    int updateByExample(@Param("record") CfgConsumerAuth record, @Param("example") CfgConsumerAuth_Example example);

    int updateByPrimaryKeySelective(CfgConsumerAuth record);

    int updateByPrimaryKey(CfgConsumerAuth record);
}