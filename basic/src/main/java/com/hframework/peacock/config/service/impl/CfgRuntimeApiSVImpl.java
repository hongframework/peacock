package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgRuntimeApiMapper;
import com.hframework.peacock.config.domain.model.CfgRuntimeApi;
import com.hframework.peacock.config.domain.model.CfgRuntimeApi_Example;
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeApiSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgRuntimeApiSV")
public class CfgRuntimeApiSVImpl  implements ICfgRuntimeApiSV {

	@Resource
	private CfgRuntimeApiMapper cfgRuntimeApiMapper;
  


    /**
    * 创建动态API
    * @param cfgRuntimeApi
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeApi cfgRuntimeApi) throws Exception {
        return cfgRuntimeApiMapper.insertSelective(cfgRuntimeApi);
    }

    /**
    * 批量维护动态API
    * @param cfgRuntimeApis
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeApi[] cfgRuntimeApis) throws  Exception{
        int result = 0;
        if(cfgRuntimeApis != null) {
            for (CfgRuntimeApi cfgRuntimeApi : cfgRuntimeApis) {
                if(cfgRuntimeApi.getId() == null) {
                    result += this.create(cfgRuntimeApi);
                }else {
                    result += this.update(cfgRuntimeApi);
                }
            }
        }
        return result;
    }

    /**
    * 更新动态API
    * @param cfgRuntimeApi
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeApi cfgRuntimeApi) throws  Exception {
        return cfgRuntimeApiMapper.updateByPrimaryKeySelective(cfgRuntimeApi);
    }

    /**
    * 通过查询对象更新动态API
    * @param cfgRuntimeApi
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeApi cfgRuntimeApi, CfgRuntimeApi_Example example) throws  Exception {
        return cfgRuntimeApiMapper.updateByExampleSelective(cfgRuntimeApi, example);
    }

    /**
    * 删除动态API
    * @param cfgRuntimeApi
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeApi cfgRuntimeApi) throws  Exception {
        return cfgRuntimeApiMapper.deleteByPrimaryKey(cfgRuntimeApi.getId());
    }

    /**
    * 删除动态API
    * @param cfgRuntimeApiId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeApiId) throws  Exception {
        return cfgRuntimeApiMapper.deleteByPrimaryKey(cfgRuntimeApiId);
    }

    /**
    * 查找所有动态API
    * @return
    */
    public List<CfgRuntimeApi> getCfgRuntimeApiAll()  throws  Exception {
        return cfgRuntimeApiMapper.selectByExampleWithBLOBs(new CfgRuntimeApi_Example());
    }

    /**
    * 通过动态APIID查询动态API
    * @param cfgRuntimeApiId
    * @return
    * @throws Exception
    */
    public CfgRuntimeApi getCfgRuntimeApiByPK(long cfgRuntimeApiId)  throws  Exception {
        return cfgRuntimeApiMapper.selectByPrimaryKey(cfgRuntimeApiId);
    }


    /**
    * 通过MAP参数查询动态API
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeApi> getCfgRuntimeApiListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询动态API
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeApi> getCfgRuntimeApiListByExample(CfgRuntimeApi_Example example) throws  Exception {
        return cfgRuntimeApiMapper.selectByExampleWithBLOBs(example);
    }

    /**
    * 通过MAP参数查询动态API数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeApiCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询动态API数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeApiCountByExample(CfgRuntimeApi_Example example) throws  Exception {
        return cfgRuntimeApiMapper.countByExample(example);
    }


  	//getter
 	
	public CfgRuntimeApiMapper getCfgRuntimeApiMapper(){
		return cfgRuntimeApiMapper;
	}
	//setter
	public void setCfgRuntimeApiMapper(CfgRuntimeApiMapper cfgRuntimeApiMapper){
    	this.cfgRuntimeApiMapper = cfgRuntimeApiMapper;
    }
}
