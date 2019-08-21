package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.service.interfaces.ICfgRuntimeResponseSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.hframework.peacock.config.domain.model.CfgRuntimeResponse;
import com.hframework.peacock.config.domain.model.CfgRuntimeResponse_Example;
import com.hframework.peacock.config.dao.CfgRuntimeResponseMapper;

@Service("iCfgRuntimeResponseSV")
public class CfgRuntimeResponseSVImpl  implements ICfgRuntimeResponseSV {

	@Resource
	private CfgRuntimeResponseMapper cfgRuntimeResponseMapper;
  


    /**
    * 创建响应参数
    * @param cfgRuntimeResponse
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeResponse cfgRuntimeResponse) throws Exception {
        return cfgRuntimeResponseMapper.insertSelective(cfgRuntimeResponse);
    }

    /**
    * 批量维护响应参数
    * @param cfgRuntimeResponses
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeResponse[] cfgRuntimeResponses) throws  Exception{
        int result = 0;
        if(cfgRuntimeResponses != null) {
            for (CfgRuntimeResponse cfgRuntimeResponse : cfgRuntimeResponses) {
                if(cfgRuntimeResponse.getId() == null) {
                    result += this.create(cfgRuntimeResponse);
                }else {
                    result += this.update(cfgRuntimeResponse);
                }
            }
        }
        return result;
    }

    /**
    * 更新响应参数
    * @param cfgRuntimeResponse
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeResponse cfgRuntimeResponse) throws  Exception {
        return cfgRuntimeResponseMapper.updateByPrimaryKeySelective(cfgRuntimeResponse);
    }

    /**
    * 通过查询对象更新响应参数
    * @param cfgRuntimeResponse
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeResponse cfgRuntimeResponse, CfgRuntimeResponse_Example example) throws  Exception {
        return cfgRuntimeResponseMapper.updateByExampleSelective(cfgRuntimeResponse, example);
    }

    /**
    * 删除响应参数
    * @param cfgRuntimeResponse
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeResponse cfgRuntimeResponse) throws  Exception {
        return cfgRuntimeResponseMapper.deleteByPrimaryKey(cfgRuntimeResponse.getId());
    }

    /**
    * 删除响应参数
    * @param cfgRuntimeResponseId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeResponseId) throws  Exception {
        return cfgRuntimeResponseMapper.deleteByPrimaryKey(cfgRuntimeResponseId);
    }

    /**
    * 查找所有响应参数
    * @return
    */
    public List<CfgRuntimeResponse> getCfgRuntimeResponseAll()  throws  Exception {
        return cfgRuntimeResponseMapper.selectByExample(new CfgRuntimeResponse_Example());
    }

    /**
    * 通过响应参数ID查询响应参数
    * @param cfgRuntimeResponseId
    * @return
    * @throws Exception
    */
    public CfgRuntimeResponse getCfgRuntimeResponseByPK(long cfgRuntimeResponseId)  throws  Exception {
        return cfgRuntimeResponseMapper.selectByPrimaryKey(cfgRuntimeResponseId);
    }


    /**
    * 通过MAP参数查询响应参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeResponse> getCfgRuntimeResponseListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询响应参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeResponse> getCfgRuntimeResponseListByExample(CfgRuntimeResponse_Example example) throws  Exception {
        return cfgRuntimeResponseMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询响应参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeResponseCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询响应参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeResponseCountByExample(CfgRuntimeResponse_Example example) throws  Exception {
        return cfgRuntimeResponseMapper.countByExample(example);
    }


  	//getter
 	
	public CfgRuntimeResponseMapper getCfgRuntimeResponseMapper(){
		return cfgRuntimeResponseMapper;
	}
	//setter
	public void setCfgRuntimeResponseMapper(CfgRuntimeResponseMapper cfgRuntimeResponseMapper){
    	this.cfgRuntimeResponseMapper = cfgRuntimeResponseMapper;
    }
}
