package com.hframework.peacock.system.service.interfaces;

import java.util.*;
import com.hframework.peacock.system.domain.model.Menu;
import com.hframework.peacock.system.domain.model.Menu_Example;


public interface IMenuSV   {

  
    /**
    * 创建鑿滃崟
    * @param menu
    * @return
    * @throws Exception
    */
    public int create(Menu menu) throws  Exception;

    /**
    * 批量维护鑿滃崟
    * @param menus
    * @return
    * @throws Exception
    */
    public int batchOperate(Menu[] menus) throws  Exception;
    /**
    * 更新鑿滃崟
    * @param menu
    * @return
    * @throws Exception
    */
    public int update(Menu menu) throws  Exception;

    /**
    * 通过查询对象更新鑿滃崟
    * @param menu
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(Menu menu, Menu_Example example) throws  Exception;

    /**
    * 删除鑿滃崟
    * @param menu
    * @return
    * @throws Exception
    */
    public int delete(Menu menu) throws  Exception;


    /**
    * 删除鑿滃崟
    * @param menuId
    * @return
    * @throws Exception
    */
    public int delete(long menuId) throws  Exception;


    /**
    * 查找所有鑿滃崟
    * @return
    */
    public List<Menu> getMenuAll()  throws  Exception;

    /**
    * 通过鑿滃崟ID查询鑿滃崟
    * @param menuId
    * @return
    * @throws Exception
    */
    public Menu getMenuByPK(long menuId)  throws  Exception;

    /**
    * 通过MAP参数查询鑿滃崟
    * @param params
    * @return
    * @throws Exception
    */
    public List<Menu> getMenuListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询鑿滃崟
    * @param example
    * @return
    * @throws Exception
    */
    public List<Menu> getMenuListByExample(Menu_Example example) throws  Exception;

    /**
    * 通过父级鑿滃崟ID查询鑿滃崟树
    * @param menu
    * @return
    * @throws Exception
    */
    public Map<Long, List<Menu>> getMenuTreeByParentId(Menu menu, Menu_Example example)  throws  Exception;

    /**
    * 通过MAP参数查询鑿滃崟数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getMenuCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询鑿滃崟数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getMenuCountByExample(Menu_Example example) throws  Exception;


 }
