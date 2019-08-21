package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgApiConfMapper;
import com.hframework.peacock.config.domain.model.CfgApiConf;
import com.hframework.peacock.config.domain.model.CfgApiConf_Example;
import com.hframework.peacock.config.service.interfaces.ICfgApiConfSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgApiConfSV")
public class CfgApiConfSVImpl  implements ICfgApiConfSV {

	@Resource
	private CfgApiConfMapper cfgApiConfMapper;
  


    /**
    * 创建API配置表
    * @param cfgApiConf
    * @return
    * @throws Exception
    */
    public int create(CfgApiConf cfgApiConf) throws Exception {
        return cfgApiConfMapper.insertSelective(cfgApiConf);
    }

    /**
    * 批量维护API配置表
    * @param cfgApiConfs
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgApiConf[] cfgApiConfs) throws  Exception{
        int result = 0;
        if(cfgApiConfs != null) {
            for (CfgApiConf cfgApiConf : cfgApiConfs) {
                if(cfgApiConf.getId() == null) {
                    result += this.create(cfgApiConf);
                }else {
                    result += this.update(cfgApiConf);
                }
            }
        }
        return result;
    }

    /**
    * 更新API配置表
    * @param cfgApiConf
    * @return
    * @throws Exception
    */
    public int update(CfgApiConf cfgApiConf) throws  Exception {
        return cfgApiConfMapper.updateByPrimaryKeySelective(cfgApiConf);
    }

    /**
    * 通过查询对象更新API配置表
    * @param cfgApiConf
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgApiConf cfgApiConf, CfgApiConf_Example example) throws  Exception {
        return cfgApiConfMapper.updateByExampleSelective(cfgApiConf, example);
    }

    /**
    * 删除API配置表
    * @param cfgApiConf
    * @return
    * @throws Exception
    */
    public int delete(CfgApiConf cfgApiConf) throws  Exception {
        return cfgApiConfMapper.deleteByPrimaryKey(cfgApiConf.getId());
    }

    /**
    * 删除API配置表
    * @param cfgApiConfId
    * @return
    * @throws Exception
    */
    public int delete(long cfgApiConfId) throws  Exception {
        return cfgApiConfMapper.deleteByPrimaryKey(cfgApiConfId);
    }

    /**
    * 查找所有API配置表
    * @return
    */
    public List<CfgApiConf> getCfgApiConfAll()  throws  Exception {
        return cfgApiConfMapper.selectByExampleWithBLOBs(new CfgApiConf_Example());
    }

    /**
    * 通过API配置表ID查询API配置表
    * @param cfgApiConfId
    * @return
    * @throws Exception
    */
    public CfgApiConf getCfgApiConfByPK(long cfgApiConfId)  throws  Exception {
        return cfgApiConfMapper.selectByPrimaryKey(cfgApiConfId);
    }


    /**
    * 通过MAP参数查询API配置表
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgApiConf> getCfgApiConfListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询API配置表
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgApiConf> getCfgApiConfListByExample(CfgApiConf_Example example) throws  Exception {
        return cfgApiConfMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询API配置表数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgApiConfCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询API配置表数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgApiConfCountByExample(CfgApiConf_Example example) throws  Exception {
        return cfgApiConfMapper.countByExample(example);
    }


  	//getter
 	
	public CfgApiConfMapper getCfgApiConfMapper(){
		return cfgApiConfMapper;
	}
	//setter
	public void setCfgApiConfMapper(CfgApiConfMapper cfgApiConfMapper){
    	this.cfgApiConfMapper = cfgApiConfMapper;
    }
}
