package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgMgrModule;
import com.hframework.peacock.config.domain.model.CfgMgrModule_Example;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.hframework.peacock.config.dao.CfgMgrModuleMapper;
import com.hframework.peacock.config.service.interfaces.ICfgMgrModuleSV;

@Service("iCfgMgrModuleSV")
public class CfgMgrModuleSVImpl  implements ICfgMgrModuleSV {

	@Resource
	private CfgMgrModuleMapper cfgMgrModuleMapper;
  


    /**
    * 创建模块
    * @param cfgMgrModule
    * @return
    * @throws Exception
    */
    public int create(CfgMgrModule cfgMgrModule) throws Exception {
        return cfgMgrModuleMapper.insertSelective(cfgMgrModule);
    }

    /**
    * 批量维护模块
    * @param cfgMgrModules
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgMgrModule[] cfgMgrModules) throws  Exception{
        int result = 0;
        if(cfgMgrModules != null) {
            for (CfgMgrModule cfgMgrModule : cfgMgrModules) {
                if(cfgMgrModule.getId() == null) {
                    result += this.create(cfgMgrModule);
                }else {
                    result += this.update(cfgMgrModule);
                }
            }
        }
        return result;
    }

    /**
    * 更新模块
    * @param cfgMgrModule
    * @return
    * @throws Exception
    */
    public int update(CfgMgrModule cfgMgrModule) throws  Exception {
        return cfgMgrModuleMapper.updateByPrimaryKeySelective(cfgMgrModule);
    }

    /**
    * 通过查询对象更新模块
    * @param cfgMgrModule
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgMgrModule cfgMgrModule, CfgMgrModule_Example example) throws  Exception {
        return cfgMgrModuleMapper.updateByExampleSelective(cfgMgrModule, example);
    }

    /**
    * 删除模块
    * @param cfgMgrModule
    * @return
    * @throws Exception
    */
    public int delete(CfgMgrModule cfgMgrModule) throws  Exception {
        return cfgMgrModuleMapper.deleteByPrimaryKey(cfgMgrModule.getId());
    }

    /**
    * 删除模块
    * @param cfgMgrModuleId
    * @return
    * @throws Exception
    */
    public int delete(long cfgMgrModuleId) throws  Exception {
        return cfgMgrModuleMapper.deleteByPrimaryKey(cfgMgrModuleId);
    }

    /**
    * 查找所有模块
    * @return
    */
    public List<CfgMgrModule> getCfgMgrModuleAll()  throws  Exception {
        return cfgMgrModuleMapper.selectByExample(new CfgMgrModule_Example());
    }

    /**
    * 通过模块ID查询模块
    * @param cfgMgrModuleId
    * @return
    * @throws Exception
    */
    public CfgMgrModule getCfgMgrModuleByPK(long cfgMgrModuleId)  throws  Exception {
        return cfgMgrModuleMapper.selectByPrimaryKey(cfgMgrModuleId);
    }


    /**
    * 通过MAP参数查询模块
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgMgrModule> getCfgMgrModuleListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询模块
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgMgrModule> getCfgMgrModuleListByExample(CfgMgrModule_Example example) throws  Exception {
        return cfgMgrModuleMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询模块数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgMgrModuleCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询模块数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgMgrModuleCountByExample(CfgMgrModule_Example example) throws  Exception {
        return cfgMgrModuleMapper.countByExample(example);
    }


  	//getter
 	
	public CfgMgrModuleMapper getCfgMgrModuleMapper(){
		return cfgMgrModuleMapper;
	}
	//setter
	public void setCfgMgrModuleMapper(CfgMgrModuleMapper cfgMgrModuleMapper){
    	this.cfgMgrModuleMapper = cfgMgrModuleMapper;
    }
}
