package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgTestCase;
import com.hframework.peacock.config.domain.model.CfgTestCase_Example;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgTestCaseMapper {
    int countByExample(CfgTestCase_Example example);

    int deleteByExample(CfgTestCase_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgTestCase record);

    int insertSelective(CfgTestCase record);

    List<CfgTestCase> selectByExample(CfgTestCase_Example example);

    CfgTestCase selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgTestCase record, @Param("example") CfgTestCase_Example example);

    int updateByExample(@Param("record") CfgTestCase record, @Param("example") CfgTestCase_Example example);

    int updateByPrimaryKeySelective(CfgTestCase record);

    int updateByPrimaryKey(CfgTestCase record);
}