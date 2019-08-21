package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.ThirdApi;
import com.hframework.peacock.config.domain.model.ThirdApi_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdApiMapper {
    int countByExample(ThirdApi_Example example);

    int deleteByExample(ThirdApi_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(ThirdApi record);

    int insertSelective(ThirdApi record);

    List<ThirdApi> selectByExampleWithBLOBs(ThirdApi_Example example);

    List<ThirdApi> selectByExample(ThirdApi_Example example);

    ThirdApi selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ThirdApi record, @Param("example") ThirdApi_Example example);

    int updateByExampleWithBLOBs(@Param("record") ThirdApi record, @Param("example") ThirdApi_Example example);

    int updateByExample(@Param("record") ThirdApi record, @Param("example") ThirdApi_Example example);

    int updateByPrimaryKeySelective(ThirdApi record);

    int updateByPrimaryKeyWithBLOBs(ThirdApi record);

    int updateByPrimaryKey(ThirdApi record);
}