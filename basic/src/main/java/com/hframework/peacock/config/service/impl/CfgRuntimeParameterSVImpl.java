package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.service.interfaces.ICfgRuntimeParameterSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.hframework.peacock.config.domain.model.CfgRuntimeParameter;
import com.hframework.peacock.config.domain.model.CfgRuntimeParameter_Example;
import com.hframework.peacock.config.dao.CfgRuntimeParameterMapper;

@Service("iCfgRuntimeParameterSV")
public class CfgRuntimeParameterSVImpl  implements ICfgRuntimeParameterSV {

	@Resource
	private CfgRuntimeParameterMapper cfgRuntimeParameterMapper;
  


    /**
    * 创建参数
    * @param cfgRuntimeParameter
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeParameter cfgRuntimeParameter) throws Exception {
        return cfgRuntimeParameterMapper.insertSelective(cfgRuntimeParameter);
    }

    /**
    * 批量维护参数
    * @param cfgRuntimeParameters
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeParameter[] cfgRuntimeParameters) throws  Exception{
        int result = 0;
        if(cfgRuntimeParameters != null) {
            for (CfgRuntimeParameter cfgRuntimeParameter : cfgRuntimeParameters) {
                if(cfgRuntimeParameter.getId() == null) {
                    result += this.create(cfgRuntimeParameter);
                }else {
                    result += this.update(cfgRuntimeParameter);
                }
            }
        }
        return result;
    }

    /**
    * 更新参数
    * @param cfgRuntimeParameter
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeParameter cfgRuntimeParameter) throws  Exception {
        return cfgRuntimeParameterMapper.updateByPrimaryKeySelective(cfgRuntimeParameter);
    }

    /**
    * 通过查询对象更新参数
    * @param cfgRuntimeParameter
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeParameter cfgRuntimeParameter, CfgRuntimeParameter_Example example) throws  Exception {
        return cfgRuntimeParameterMapper.updateByExampleSelective(cfgRuntimeParameter, example);
    }

    /**
    * 删除参数
    * @param cfgRuntimeParameter
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeParameter cfgRuntimeParameter) throws  Exception {
        return cfgRuntimeParameterMapper.deleteByPrimaryKey(cfgRuntimeParameter.getId());
    }

    /**
    * 删除参数
    * @param cfgRuntimeParameterId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeParameterId) throws  Exception {
        return cfgRuntimeParameterMapper.deleteByPrimaryKey(cfgRuntimeParameterId);
    }

    /**
    * 查找所有参数
    * @return
    */
    public List<CfgRuntimeParameter> getCfgRuntimeParameterAll()  throws  Exception {
        return cfgRuntimeParameterMapper.selectByExample(new CfgRuntimeParameter_Example());
    }

    /**
    * 通过参数ID查询参数
    * @param cfgRuntimeParameterId
    * @return
    * @throws Exception
    */
    public CfgRuntimeParameter getCfgRuntimeParameterByPK(long cfgRuntimeParameterId)  throws  Exception {
        return cfgRuntimeParameterMapper.selectByPrimaryKey(cfgRuntimeParameterId);
    }


    /**
    * 通过MAP参数查询参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeParameter> getCfgRuntimeParameterListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeParameter> getCfgRuntimeParameterListByExample(CfgRuntimeParameter_Example example) throws  Exception {
        return cfgRuntimeParameterMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeParameterCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeParameterCountByExample(CfgRuntimeParameter_Example example) throws  Exception {
        return cfgRuntimeParameterMapper.countByExample(example);
    }


  	//getter
 	
	public CfgRuntimeParameterMapper getCfgRuntimeParameterMapper(){
		return cfgRuntimeParameterMapper;
	}
	//setter
	public void setCfgRuntimeParameterMapper(CfgRuntimeParameterMapper cfgRuntimeParameterMapper){
    	this.cfgRuntimeParameterMapper = cfgRuntimeParameterMapper;
    }
}
