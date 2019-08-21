package com.hframework.peacock.config.service.interfaces;



import java.util.*;

import com.hframework.peacock.config.domain.model.CfgMgrProgram;
import com.hframework.peacock.config.domain.model.CfgMgrProgram_Example;


public interface ICfgMgrProgramSV   {



  

    /**

    * 创建项目

    * @param cfgMgrProgram

    * @return

    * @throws Exception

    */

    public int create(CfgMgrProgram cfgMgrProgram) throws  Exception;



    /**

    * 批量维护项目

    * @param cfgMgrPrograms

    * @return

    * @throws Exception

    */

    public int batchOperate(CfgMgrProgram[] cfgMgrPrograms) throws  Exception;

    /**

    * 更新项目

    * @param cfgMgrProgram

    * @return

    * @throws Exception

    */

    public int update(CfgMgrProgram cfgMgrProgram) throws  Exception;



    /**

    * 通过查询对象更新项目

    * @param cfgMgrProgram

    * @param example

    * @return

    * @throws Exception

    */

    public int updateByExample(CfgMgrProgram cfgMgrProgram, CfgMgrProgram_Example example) throws  Exception;



    /**

    * 删除项目

    * @param cfgMgrProgram

    * @return

    * @throws Exception

    */

    public int delete(CfgMgrProgram cfgMgrProgram) throws  Exception;





    /**

    * 删除项目

    * @param cfgMgrProgramId

    * @return

    * @throws Exception

    */

    public int delete(long cfgMgrProgramId) throws  Exception;





    /**

    * 查找所有项目

    * @return

    */

    public List<CfgMgrProgram> getCfgMgrProgramAll()  throws  Exception;



    /**

    * 通过项目ID查询项目

    * @param cfgMgrProgramId

    * @return

    * @throws Exception

    */

    public CfgMgrProgram getCfgMgrProgramByPK(long cfgMgrProgramId)  throws  Exception;



    /**

    * 通过MAP参数查询项目

    * @param params

    * @return

    * @throws Exception

    */

    public List<CfgMgrProgram> getCfgMgrProgramListByParam(Map<String, Object> params)  throws  Exception;





    /**

    * 通过查询对象查询项目

    * @param example

    * @return

    * @throws Exception

    */

    public List<CfgMgrProgram> getCfgMgrProgramListByExample(CfgMgrProgram_Example example) throws  Exception;





    /**

    * 通过MAP参数查询项目数量

    * @param params

    * @return

    * @throws Exception

    */

    public int getCfgMgrProgramCountByParam(Map<String, Object> params)  throws  Exception;





    /**

    * 通过查询对象查询项目数量

    * @param example

    * @return

    * @throws Exception

    */

    public int getCfgMgrProgramCountByExample(CfgMgrProgram_Example example) throws  Exception;





 }
