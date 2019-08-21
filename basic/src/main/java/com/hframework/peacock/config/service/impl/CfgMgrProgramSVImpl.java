package com.hframework.peacock.config.service.impl;



import java.util.*;

import com.hframework.peacock.config.domain.model.CfgMgrProgram;
import com.hframework.peacock.config.domain.model.CfgMgrProgram_Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.hframework.peacock.config.dao.CfgMgrProgramMapper;

import com.hframework.peacock.config.service.interfaces.ICfgMgrProgramSV;



@Service("iCfgMgrProgramSV")

public class CfgMgrProgramSVImpl  implements ICfgMgrProgramSV {



	@Resource

	private CfgMgrProgramMapper cfgMgrProgramMapper;

  





    /**

    * 创建项目

    * @param cfgMgrProgram

    * @return

    * @throws Exception

    */

    public int create(CfgMgrProgram cfgMgrProgram) throws Exception {

        return cfgMgrProgramMapper.insertSelective(cfgMgrProgram);

    }



    /**

    * 批量维护项目

    * @param cfgMgrPrograms

    * @return

    * @throws Exception

    */

    public int batchOperate(CfgMgrProgram[] cfgMgrPrograms) throws  Exception{

        int result = 0;

        if(cfgMgrPrograms != null) {

            for (CfgMgrProgram cfgMgrProgram : cfgMgrPrograms) {

                if(cfgMgrProgram.getId() == null) {

                    result += this.create(cfgMgrProgram);

                }else {

                    result += this.update(cfgMgrProgram);

                }

            }

        }

        return result;

    }



    /**

    * 更新项目

    * @param cfgMgrProgram

    * @return

    * @throws Exception

    */

    public int update(CfgMgrProgram cfgMgrProgram) throws  Exception {

        return cfgMgrProgramMapper.updateByPrimaryKeySelective(cfgMgrProgram);

    }



    /**

    * 通过查询对象更新项目

    * @param cfgMgrProgram

    * @param example

    * @return

    * @throws Exception

    */

    public int updateByExample(CfgMgrProgram cfgMgrProgram, CfgMgrProgram_Example example) throws  Exception {

        return cfgMgrProgramMapper.updateByExampleSelective(cfgMgrProgram, example);

    }



    /**

    * 删除项目

    * @param cfgMgrProgram

    * @return

    * @throws Exception

    */

    public int delete(CfgMgrProgram cfgMgrProgram) throws  Exception {

        return cfgMgrProgramMapper.deleteByPrimaryKey(cfgMgrProgram.getId());

    }



    /**

    * 删除项目

    * @param cfgMgrProgramId

    * @return

    * @throws Exception

    */

    public int delete(long cfgMgrProgramId) throws  Exception {

        return cfgMgrProgramMapper.deleteByPrimaryKey(cfgMgrProgramId);

    }



    /**

    * 查找所有项目

    * @return

    */

    public List<CfgMgrProgram> getCfgMgrProgramAll()  throws  Exception {

        return cfgMgrProgramMapper.selectByExample(new CfgMgrProgram_Example());

    }



    /**

    * 通过项目ID查询项目

    * @param cfgMgrProgramId

    * @return

    * @throws Exception

    */

    public CfgMgrProgram getCfgMgrProgramByPK(long cfgMgrProgramId)  throws  Exception {

        return cfgMgrProgramMapper.selectByPrimaryKey(cfgMgrProgramId);

    }





    /**

    * 通过MAP参数查询项目

    * @param params

    * @return

    * @throws Exception

    */

    public List<CfgMgrProgram> getCfgMgrProgramListByParam(Map<String, Object> params)  throws  Exception {

        return null;

    }







    /**

    * 通过查询对象查询项目

    * @param example

    * @return

    * @throws Exception

    */

    public List<CfgMgrProgram> getCfgMgrProgramListByExample(CfgMgrProgram_Example example) throws  Exception {

        return cfgMgrProgramMapper.selectByExample(example);

    }



    /**

    * 通过MAP参数查询项目数量

    * @param params

    * @return

    * @throws Exception

    */

    public int getCfgMgrProgramCountByParam(Map<String, Object> params)  throws  Exception {

        return 0;

    }



    /**

    * 通过查询对象查询项目数量

    * @param example

    * @return

    * @throws Exception

    */

    public int getCfgMgrProgramCountByExample(CfgMgrProgram_Example example) throws  Exception {

        return cfgMgrProgramMapper.countByExample(example);

    }





  	//getter

 	

	public CfgMgrProgramMapper getCfgMgrProgramMapper(){

		return cfgMgrProgramMapper;

	}

	//setter

	public void setCfgMgrProgramMapper(CfgMgrProgramMapper cfgMgrProgramMapper){

    	this.cfgMgrProgramMapper = cfgMgrProgramMapper;

    }

}
