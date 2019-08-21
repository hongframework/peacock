package com.hframework.peacock.config.dao;

import com.hframework.peacock.config.domain.model.ThirdPubResponse;
import com.hframework.peacock.config.domain.model.ThirdPubResponse_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPubResponseMapper {
    int countByExample(ThirdPubResponse_Example example);

    int deleteByExample(ThirdPubResponse_Example example);

    int deleteByPrimaryKey(Long id);

    int insert(ThirdPubResponse record);

    int insertSelective(ThirdPubResponse record);

    List<ThirdPubResponse> selectByExample(ThirdPubResponse_Example example);

    ThirdPubResponse selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ThirdPubResponse record, @Param("example") ThirdPubResponse_Example example);

    int updateByExample(@Param("record") ThirdPubResponse record, @Param("example") ThirdPubResponse_Example example);

    int updateByPrimaryKeySelective(ThirdPubResponse record);

    int updateByPrimaryKey(ThirdPubResponse record);
}