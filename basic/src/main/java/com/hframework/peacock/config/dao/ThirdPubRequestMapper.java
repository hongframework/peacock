package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.ThirdPubRequest;
import com.hframework.peacock.config.domain.model.ThirdPubRequest_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPubRequestMapper {
    int countByExample(ThirdPubRequest_Example example);

    int deleteByExample(ThirdPubRequest_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(ThirdPubRequest record);

    int insertSelective(ThirdPubRequest record);

    List<ThirdPubRequest> selectByExample(ThirdPubRequest_Example example);

    ThirdPubRequest selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ThirdPubRequest record, @Param("example") ThirdPubRequest_Example example);

    int updateByExample(@Param("record") ThirdPubRequest record, @Param("example") ThirdPubRequest_Example example);

    int updateByPrimaryKeySelective(ThirdPubRequest record);

    int updateByPrimaryKey(ThirdPubRequest record);
}