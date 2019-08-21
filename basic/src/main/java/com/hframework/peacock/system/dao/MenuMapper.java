package com.hframework.peacock.system.dao;

import com.hframework.peacock.system.domain.model.Menu;
import com.hframework.peacock.system.domain.model.Menu_Example;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuMapper {
    int countByExample(Menu_Example example);

    int deleteByExample(Menu_Example example);

    int deleteByPrimaryKey(Long menuId);

    int insert(Menu record);

    int insertSelective(Menu record);

    List<Menu> selectByExample(Menu_Example example);

    Menu selectByPrimaryKey(Long menuId);

    int updateByExampleSelective(@Param("record") Menu record, @Param("example") Menu_Example example);

    int updateByExample(@Param("record") Menu record, @Param("example") Menu_Example example);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);
}