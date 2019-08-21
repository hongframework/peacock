package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgRuntimeTraceMapper;
import com.hframework.peacock.config.domain.model.CfgRuntimeTrace;
import com.hframework.peacock.config.domain.model.CfgRuntimeTrace_Example;
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeTraceSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgRuntimeTraceSV")
public class CfgRuntimeTraceSVImpl  implements ICfgRuntimeTraceSV {

	@Resource
	private CfgRuntimeTraceMapper cfgRuntimeTraceMapper;
  


    /**
    * 创建跟踪配置
    * @param cfgRuntimeTrace
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeTrace cfgRuntimeTrace) throws Exception {
        return cfgRuntimeTraceMapper.insertSelective(cfgRuntimeTrace);
    }

    /**
    * 批量维护跟踪配置
    * @param cfgRuntimeTraces
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeTrace[] cfgRuntimeTraces) throws  Exception{
        int result = 0;
        if(cfgRuntimeTraces != null) {
            for (CfgRuntimeTrace cfgRuntimeTrace : cfgRuntimeTraces) {
                if(cfgRuntimeTrace.getId() == null) {
                    result += this.create(cfgRuntimeTrace);
                }else {
                    result += this.update(cfgRuntimeTrace);
                }
            }
        }
        return result;
    }

    /**
    * 更新跟踪配置
    * @param cfgRuntimeTrace
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeTrace cfgRuntimeTrace) throws  Exception {
        return cfgRuntimeTraceMapper.updateByPrimaryKeySelective(cfgRuntimeTrace);
    }

    /**
    * 通过查询对象更新跟踪配置
    * @param cfgRuntimeTrace
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeTrace cfgRuntimeTrace, CfgRuntimeTrace_Example example) throws  Exception {
        return cfgRuntimeTraceMapper.updateByExampleSelective(cfgRuntimeTrace, example);
    }

    /**
    * 删除跟踪配置
    * @param cfgRuntimeTrace
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeTrace cfgRuntimeTrace) throws  Exception {
        return cfgRuntimeTraceMapper.deleteByPrimaryKey(cfgRuntimeTrace.getId());
    }

    /**
    * 删除跟踪配置
    * @param cfgRuntimeTraceId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeTraceId) throws  Exception {
        return cfgRuntimeTraceMapper.deleteByPrimaryKey(cfgRuntimeTraceId);
    }

    /**
    * 查找所有跟踪配置
    * @return
    */
    public List<CfgRuntimeTrace> getCfgRuntimeTraceAll()  throws  Exception {
        return cfgRuntimeTraceMapper.selectByExample(new CfgRuntimeTrace_Example());
    }

    /**
    * 通过跟踪配置ID查询跟踪配置
    * @param cfgRuntimeTraceId
    * @return
    * @throws Exception
    */
    public CfgRuntimeTrace getCfgRuntimeTraceByPK(long cfgRuntimeTraceId)  throws  Exception {
        return cfgRuntimeTraceMapper.selectByPrimaryKey(cfgRuntimeTraceId);
    }


    /**
    * 通过MAP参数查询跟踪配置
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeTrace> getCfgRuntimeTraceListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询跟踪配置
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeTrace> getCfgRuntimeTraceListByExample(CfgRuntimeTrace_Example example) throws  Exception {
        return cfgRuntimeTraceMapper.selectByExampleWithBLOBs(example);
    }

    /**
    * 通过MAP参数查询跟踪配置数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeTraceCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询跟踪配置数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeTraceCountByExample(CfgRuntimeTrace_Example example) throws  Exception {
        return cfgRuntimeTraceMapper.countByExample(example);
    }


  	//getter
 	
	public CfgRuntimeTraceMapper getCfgRuntimeTraceMapper(){
		return cfgRuntimeTraceMapper;
	}
	//setter
	public void setCfgRuntimeTraceMapper(CfgRuntimeTraceMapper cfgRuntimeTraceMapper){
    	this.cfgRuntimeTraceMapper = cfgRuntimeTraceMapper;
    }
}
