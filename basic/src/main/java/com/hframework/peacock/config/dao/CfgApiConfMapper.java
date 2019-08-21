package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.CfgApiConf;
import com.hframework.peacock.config.domain.model.CfgApiConf_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CfgApiConfMapper {
    int countByExample(CfgApiConf_Example example);

    int deleteByExample(CfgApiConf_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(CfgApiConf record);

    int insertSelective(CfgApiConf record);

    List<CfgApiConf> selectByExampleWithBLOBs(CfgApiConf_Example example);

    List<CfgApiConf> selectByExample(CfgApiConf_Example example);

    CfgApiConf selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CfgApiConf record, @Param("example") CfgApiConf_Example example);

    int updateByExampleWithBLOBs(@Param("record") CfgApiConf record, @Param("example") CfgApiConf_Example example);

    int updateByExample(@Param("record") CfgApiConf record, @Param("example") CfgApiConf_Example example);

    int updateByPrimaryKeySelective(CfgApiConf record);

    int updateByPrimaryKeyWithBLOBs(CfgApiConf record);

    int updateByPrimaryKey(CfgApiConf record);
}