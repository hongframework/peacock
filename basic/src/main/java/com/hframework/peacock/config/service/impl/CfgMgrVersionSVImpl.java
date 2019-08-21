package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgMgrVersion;
import com.hframework.peacock.config.domain.model.CfgMgrVersion_Example;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.hframework.peacock.config.dao.CfgMgrVersionMapper;
import com.hframework.peacock.config.service.interfaces.ICfgMgrVersionSV;

@Service("iCfgMgrVersionSV")
public class CfgMgrVersionSVImpl  implements ICfgMgrVersionSV {

	@Resource
	private CfgMgrVersionMapper cfgMgrVersionMapper;
  


    /**
    * 创建版本
    * @param cfgMgrVersion
    * @return
    * @throws Exception
    */
    public int create(CfgMgrVersion cfgMgrVersion) throws Exception {
        return cfgMgrVersionMapper.insertSelective(cfgMgrVersion);
    }

    /**
    * 批量维护版本
    * @param cfgMgrVersions
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgMgrVersion[] cfgMgrVersions) throws  Exception{
        int result = 0;
        if(cfgMgrVersions != null) {
            for (CfgMgrVersion cfgMgrVersion : cfgMgrVersions) {
                if(cfgMgrVersion.getId() == null) {
                    result += this.create(cfgMgrVersion);
                }else {
                    result += this.update(cfgMgrVersion);
                }
            }
        }
        return result;
    }

    /**
    * 更新版本
    * @param cfgMgrVersion
    * @return
    * @throws Exception
    */
    public int update(CfgMgrVersion cfgMgrVersion) throws  Exception {
        return cfgMgrVersionMapper.updateByPrimaryKeySelective(cfgMgrVersion);
    }

    /**
    * 通过查询对象更新版本
    * @param cfgMgrVersion
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgMgrVersion cfgMgrVersion, CfgMgrVersion_Example example) throws  Exception {
        return cfgMgrVersionMapper.updateByExampleSelective(cfgMgrVersion, example);
    }

    /**
    * 删除版本
    * @param cfgMgrVersion
    * @return
    * @throws Exception
    */
    public int delete(CfgMgrVersion cfgMgrVersion) throws  Exception {
        return cfgMgrVersionMapper.deleteByPrimaryKey(cfgMgrVersion.getId());
    }

    /**
    * 删除版本
    * @param cfgMgrVersionId
    * @return
    * @throws Exception
    */
    public int delete(long cfgMgrVersionId) throws  Exception {
        return cfgMgrVersionMapper.deleteByPrimaryKey(cfgMgrVersionId);
    }

    /**
    * 查找所有版本
    * @return
    */
    public List<CfgMgrVersion> getCfgMgrVersionAll()  throws  Exception {
        return cfgMgrVersionMapper.selectByExample(new CfgMgrVersion_Example());
    }

    /**
    * 通过版本ID查询版本
    * @param cfgMgrVersionId
    * @return
    * @throws Exception
    */
    public CfgMgrVersion getCfgMgrVersionByPK(long cfgMgrVersionId)  throws  Exception {
        return cfgMgrVersionMapper.selectByPrimaryKey(cfgMgrVersionId);
    }


    /**
    * 通过MAP参数查询版本
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgMgrVersion> getCfgMgrVersionListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询版本
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgMgrVersion> getCfgMgrVersionListByExample(CfgMgrVersion_Example example) throws  Exception {
        return cfgMgrVersionMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询版本数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgMgrVersionCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询版本数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgMgrVersionCountByExample(CfgMgrVersion_Example example) throws  Exception {
        return cfgMgrVersionMapper.countByExample(example);
    }


  	//getter
 	
	public CfgMgrVersionMapper getCfgMgrVersionMapper(){
		return cfgMgrVersionMapper;
	}
	//setter
	public void setCfgMgrVersionMapper(CfgMgrVersionMapper cfgMgrVersionMapper){
    	this.cfgMgrVersionMapper = cfgMgrVersionMapper;
    }
}
