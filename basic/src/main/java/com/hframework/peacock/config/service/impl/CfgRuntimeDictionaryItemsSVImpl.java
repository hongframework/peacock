package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgRuntimeDictionaryItemsMapper;
import com.hframework.peacock.config.domain.model.CfgRuntimeDictionaryItems;
import com.hframework.peacock.config.domain.model.CfgRuntimeDictionaryItems_Example;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.hframework.peacock.config.service.interfaces.ICfgRuntimeDictionaryItemsSV;

@Service("iCfgRuntimeDictionaryItemsSV")
public class CfgRuntimeDictionaryItemsSVImpl  implements ICfgRuntimeDictionaryItemsSV {

	@Resource
	private CfgRuntimeDictionaryItemsMapper cfgRuntimeDictionaryItemsMapper;
  


    /**
    * 创建API字典项
    * @param cfgRuntimeDictionaryItems
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems) throws Exception {
        return cfgRuntimeDictionaryItemsMapper.insertSelective(cfgRuntimeDictionaryItems);
    }

    /**
    * 批量维护API字典项
    * @param cfgRuntimeDictionaryItemss
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeDictionaryItems[] cfgRuntimeDictionaryItemss) throws  Exception{
        int result = 0;
        if(cfgRuntimeDictionaryItemss != null) {
            for (CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems : cfgRuntimeDictionaryItemss) {
                if(cfgRuntimeDictionaryItems.getId() == null) {
                    result += this.create(cfgRuntimeDictionaryItems);
                }else {
                    result += this.update(cfgRuntimeDictionaryItems);
                }
            }
        }
        return result;
    }

    /**
    * 更新API字典项
    * @param cfgRuntimeDictionaryItems
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems) throws  Exception {
        return cfgRuntimeDictionaryItemsMapper.updateByPrimaryKeySelective(cfgRuntimeDictionaryItems);
    }

    /**
    * 通过查询对象更新API字典项
    * @param cfgRuntimeDictionaryItems
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems, CfgRuntimeDictionaryItems_Example example) throws  Exception {
        return cfgRuntimeDictionaryItemsMapper.updateByExampleSelective(cfgRuntimeDictionaryItems, example);
    }

    /**
    * 删除API字典项
    * @param cfgRuntimeDictionaryItems
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems) throws  Exception {
        return cfgRuntimeDictionaryItemsMapper.deleteByPrimaryKey(cfgRuntimeDictionaryItems.getId());
    }

    /**
    * 删除API字典项
    * @param cfgRuntimeDictionaryItemsId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeDictionaryItemsId) throws  Exception {
        return cfgRuntimeDictionaryItemsMapper.deleteByPrimaryKey(cfgRuntimeDictionaryItemsId);
    }

    /**
    * 查找所有API字典项
    * @return
    */
    public List<CfgRuntimeDictionaryItems> getCfgRuntimeDictionaryItemsAll()  throws  Exception {
        return cfgRuntimeDictionaryItemsMapper.selectByExample(new CfgRuntimeDictionaryItems_Example());
    }

    /**
    * 通过API字典项ID查询API字典项
    * @param cfgRuntimeDictionaryItemsId
    * @return
    * @throws Exception
    */
    public CfgRuntimeDictionaryItems getCfgRuntimeDictionaryItemsByPK(long cfgRuntimeDictionaryItemsId)  throws  Exception {
        return cfgRuntimeDictionaryItemsMapper.selectByPrimaryKey(cfgRuntimeDictionaryItemsId);
    }


    /**
    * 通过MAP参数查询API字典项
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeDictionaryItems> getCfgRuntimeDictionaryItemsListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询API字典项
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeDictionaryItems> getCfgRuntimeDictionaryItemsListByExample(CfgRuntimeDictionaryItems_Example example) throws  Exception {
        return cfgRuntimeDictionaryItemsMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询API字典项数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeDictionaryItemsCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询API字典项数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeDictionaryItemsCountByExample(CfgRuntimeDictionaryItems_Example example) throws  Exception {
        return cfgRuntimeDictionaryItemsMapper.countByExample(example);
    }


  	//getter
 	
	public CfgRuntimeDictionaryItemsMapper getCfgRuntimeDictionaryItemsMapper(){
		return cfgRuntimeDictionaryItemsMapper;
	}
	//setter
	public void setCfgRuntimeDictionaryItemsMapper(CfgRuntimeDictionaryItemsMapper cfgRuntimeDictionaryItemsMapper){
    	this.cfgRuntimeDictionaryItemsMapper = cfgRuntimeDictionaryItemsMapper;
    }
}
