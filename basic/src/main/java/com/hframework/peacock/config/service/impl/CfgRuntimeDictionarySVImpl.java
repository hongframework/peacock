package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgRuntimeDictionary;
import com.hframework.peacock.config.domain.model.CfgRuntimeDictionary_Example;
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeDictionarySV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.hframework.peacock.config.dao.CfgRuntimeDictionaryMapper;

@Service("iCfgRuntimeDictionarySV")
public class CfgRuntimeDictionarySVImpl  implements ICfgRuntimeDictionarySV {

	@Resource
	private CfgRuntimeDictionaryMapper cfgRuntimeDictionaryMapper;
  


    /**
    * 创建API字典
    * @param cfgRuntimeDictionary
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeDictionary cfgRuntimeDictionary) throws Exception {
        return cfgRuntimeDictionaryMapper.insertSelective(cfgRuntimeDictionary);
    }

    /**
    * 批量维护API字典
    * @param cfgRuntimeDictionarys
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeDictionary[] cfgRuntimeDictionarys) throws  Exception{
        int result = 0;
        if(cfgRuntimeDictionarys != null) {
            for (CfgRuntimeDictionary cfgRuntimeDictionary : cfgRuntimeDictionarys) {
                if(cfgRuntimeDictionary.getId() == null) {
                    result += this.create(cfgRuntimeDictionary);
                }else {
                    result += this.update(cfgRuntimeDictionary);
                }
            }
        }
        return result;
    }

    /**
    * 更新API字典
    * @param cfgRuntimeDictionary
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeDictionary cfgRuntimeDictionary) throws  Exception {
        return cfgRuntimeDictionaryMapper.updateByPrimaryKeySelective(cfgRuntimeDictionary);
    }

    /**
    * 通过查询对象更新API字典
    * @param cfgRuntimeDictionary
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeDictionary cfgRuntimeDictionary, CfgRuntimeDictionary_Example example) throws  Exception {
        return cfgRuntimeDictionaryMapper.updateByExampleSelective(cfgRuntimeDictionary, example);
    }

    /**
    * 删除API字典
    * @param cfgRuntimeDictionary
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeDictionary cfgRuntimeDictionary) throws  Exception {
        return cfgRuntimeDictionaryMapper.deleteByPrimaryKey(cfgRuntimeDictionary.getId());
    }

    /**
    * 删除API字典
    * @param cfgRuntimeDictionaryId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeDictionaryId) throws  Exception {
        return cfgRuntimeDictionaryMapper.deleteByPrimaryKey(cfgRuntimeDictionaryId);
    }

    /**
    * 查找所有API字典
    * @return
    */
    public List<CfgRuntimeDictionary> getCfgRuntimeDictionaryAll()  throws  Exception {
        return cfgRuntimeDictionaryMapper.selectByExample(new CfgRuntimeDictionary_Example());
    }

    /**
    * 通过API字典ID查询API字典
    * @param cfgRuntimeDictionaryId
    * @return
    * @throws Exception
    */
    public CfgRuntimeDictionary getCfgRuntimeDictionaryByPK(long cfgRuntimeDictionaryId)  throws  Exception {
        return cfgRuntimeDictionaryMapper.selectByPrimaryKey(cfgRuntimeDictionaryId);
    }


    /**
    * 通过MAP参数查询API字典
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeDictionary> getCfgRuntimeDictionaryListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询API字典
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeDictionary> getCfgRuntimeDictionaryListByExample(CfgRuntimeDictionary_Example example) throws  Exception {
        return cfgRuntimeDictionaryMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询API字典数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeDictionaryCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询API字典数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeDictionaryCountByExample(CfgRuntimeDictionary_Example example) throws  Exception {
        return cfgRuntimeDictionaryMapper.countByExample(example);
    }


  	//getter
 	
	public CfgRuntimeDictionaryMapper getCfgRuntimeDictionaryMapper(){
		return cfgRuntimeDictionaryMapper;
	}
	//setter
	public void setCfgRuntimeDictionaryMapper(CfgRuntimeDictionaryMapper cfgRuntimeDictionaryMapper){
    	this.cfgRuntimeDictionaryMapper = cfgRuntimeDictionaryMapper;
    }
}
