package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.service.interfaces.ICfgStaticExpanderParameterSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.hframework.peacock.config.domain.model.CfgStaticExpanderParameter;
import com.hframework.peacock.config.domain.model.CfgStaticExpanderParameter_Example;
import com.hframework.peacock.config.dao.CfgStaticExpanderParameterMapper;

@Service("iCfgStaticExpanderParameterSV")
public class CfgStaticExpanderParameterSVImpl  implements ICfgStaticExpanderParameterSV {

	@Resource
	private CfgStaticExpanderParameterMapper cfgStaticExpanderParameterMapper;
  


    /**
    * 创建静态扩展器参数
    * @param cfgStaticExpanderParameter
    * @return
    * @throws Exception
    */
    public int create(CfgStaticExpanderParameter cfgStaticExpanderParameter) throws Exception {
        return cfgStaticExpanderParameterMapper.insertSelective(cfgStaticExpanderParameter);
    }

    /**
    * 批量维护静态扩展器参数
    * @param cfgStaticExpanderParameters
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgStaticExpanderParameter[] cfgStaticExpanderParameters) throws  Exception{
        int result = 0;
        if(cfgStaticExpanderParameters != null) {
            for (CfgStaticExpanderParameter cfgStaticExpanderParameter : cfgStaticExpanderParameters) {
                if(cfgStaticExpanderParameter.getId() == null) {
                    result += this.create(cfgStaticExpanderParameter);
                }else {
                    result += this.update(cfgStaticExpanderParameter);
                }
            }
        }
        return result;
    }

    /**
    * 更新静态扩展器参数
    * @param cfgStaticExpanderParameter
    * @return
    * @throws Exception
    */
    public int update(CfgStaticExpanderParameter cfgStaticExpanderParameter) throws  Exception {
        return cfgStaticExpanderParameterMapper.updateByPrimaryKeySelective(cfgStaticExpanderParameter);
    }

    /**
    * 通过查询对象更新静态扩展器参数
    * @param cfgStaticExpanderParameter
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgStaticExpanderParameter cfgStaticExpanderParameter, CfgStaticExpanderParameter_Example example) throws  Exception {
        return cfgStaticExpanderParameterMapper.updateByExampleSelective(cfgStaticExpanderParameter, example);
    }

    /**
    * 删除静态扩展器参数
    * @param cfgStaticExpanderParameter
    * @return
    * @throws Exception
    */
    public int delete(CfgStaticExpanderParameter cfgStaticExpanderParameter) throws  Exception {
        return cfgStaticExpanderParameterMapper.deleteByPrimaryKey(cfgStaticExpanderParameter.getId());
    }

    /**
    * 删除静态扩展器参数
    * @param cfgStaticExpanderParameterId
    * @return
    * @throws Exception
    */
    public int delete(long cfgStaticExpanderParameterId) throws  Exception {
        return cfgStaticExpanderParameterMapper.deleteByPrimaryKey(cfgStaticExpanderParameterId);
    }

    /**
    * 查找所有静态扩展器参数
    * @return
    */
    public List<CfgStaticExpanderParameter> getCfgStaticExpanderParameterAll()  throws  Exception {
        return cfgStaticExpanderParameterMapper.selectByExample(new CfgStaticExpanderParameter_Example());
    }

    /**
    * 通过静态扩展器参数ID查询静态扩展器参数
    * @param cfgStaticExpanderParameterId
    * @return
    * @throws Exception
    */
    public CfgStaticExpanderParameter getCfgStaticExpanderParameterByPK(long cfgStaticExpanderParameterId)  throws  Exception {
        return cfgStaticExpanderParameterMapper.selectByPrimaryKey(cfgStaticExpanderParameterId);
    }


    /**
    * 通过MAP参数查询静态扩展器参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgStaticExpanderParameter> getCfgStaticExpanderParameterListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询静态扩展器参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgStaticExpanderParameter> getCfgStaticExpanderParameterListByExample(CfgStaticExpanderParameter_Example example) throws  Exception {
        return cfgStaticExpanderParameterMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询静态扩展器参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgStaticExpanderParameterCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询静态扩展器参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgStaticExpanderParameterCountByExample(CfgStaticExpanderParameter_Example example) throws  Exception {
        return cfgStaticExpanderParameterMapper.countByExample(example);
    }


  	//getter
 	
	public CfgStaticExpanderParameterMapper getCfgStaticExpanderParameterMapper(){
		return cfgStaticExpanderParameterMapper;
	}
	//setter
	public void setCfgStaticExpanderParameterMapper(CfgStaticExpanderParameterMapper cfgStaticExpanderParameterMapper){
    	this.cfgStaticExpanderParameterMapper = cfgStaticExpanderParameterMapper;
    }
}
