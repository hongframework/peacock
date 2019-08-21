package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgConsumer;
import com.hframework.peacock.config.domain.model.CfgConsumer_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgConsumerMapper {
    int countByExample(CfgConsumer_Example example);

    int deleteByExample(CfgConsumer_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgConsumer record);

    int insertSelective(CfgConsumer record);

    List<CfgConsumer> selectByExample(CfgConsumer_Example example);

    CfgConsumer selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgConsumer record, @Param("example") CfgConsumer_Example example);

    int updateByExample(@Param("record") CfgConsumer record, @Param("example") CfgConsumer_Example example);

    int updateByPrimaryKeySelective(CfgConsumer record);

    int updateByPrimaryKey(CfgConsumer record);
}