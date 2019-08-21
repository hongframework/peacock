package com.hframework.peacock.system.service.impl;

import java.util.*;

import com.hframework.peacock.system.service.interfaces.IDictionaryItemSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.hframework.peacock.system.domain.model.DictionaryItem;
import com.hframework.peacock.system.domain.model.DictionaryItem_Example;
import com.hframework.peacock.system.dao.DictionaryItemMapper;

@Service("iDictionaryItemSV")
public class DictionaryItemSVImpl  implements IDictionaryItemSV {

	@Resource
	private DictionaryItemMapper dictionaryItemMapper;
  


    /**
    * 创建瀛楀吀椤�
    * @param dictionaryItem
    * @return
    * @throws Exception
    */
    public int create(DictionaryItem dictionaryItem) throws Exception {
        return dictionaryItemMapper.insertSelective(dictionaryItem);
    }

    /**
    * 批量维护瀛楀吀椤�
    * @param dictionaryItems
    * @return
    * @throws Exception
    */
    public int batchOperate(DictionaryItem[] dictionaryItems) throws  Exception{
        int result = 0;
        if(dictionaryItems != null) {
            for (DictionaryItem dictionaryItem : dictionaryItems) {
                if(dictionaryItem.getDictionaryItemId() == null) {
                    result += this.create(dictionaryItem);
                }else {
                    result += this.update(dictionaryItem);
                }
            }
        }
        return result;
    }

    /**
    * 更新瀛楀吀椤�
    * @param dictionaryItem
    * @return
    * @throws Exception
    */
    public int update(DictionaryItem dictionaryItem) throws  Exception {
        return dictionaryItemMapper.updateByPrimaryKeySelective(dictionaryItem);
    }

    /**
    * 通过查询对象更新瀛楀吀椤�
    * @param dictionaryItem
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(DictionaryItem dictionaryItem, DictionaryItem_Example example) throws  Exception {
        return dictionaryItemMapper.updateByExampleSelective(dictionaryItem, example);
    }

    /**
    * 删除瀛楀吀椤�
    * @param dictionaryItem
    * @return
    * @throws Exception
    */
    public int delete(DictionaryItem dictionaryItem) throws  Exception {
        return dictionaryItemMapper.deleteByPrimaryKey(dictionaryItem.getDictionaryItemId());
    }

    /**
    * 删除瀛楀吀椤�
    * @param dictionaryItemId
    * @return
    * @throws Exception
    */
    public int delete(long dictionaryItemId) throws  Exception {
        return dictionaryItemMapper.deleteByPrimaryKey(dictionaryItemId);
    }

    /**
    * 查找所有瀛楀吀椤�
    * @return
    */
    public List<DictionaryItem> getDictionaryItemAll()  throws  Exception {
        return dictionaryItemMapper.selectByExample(new DictionaryItem_Example());
    }

    /**
    * 通过瀛楀吀椤�ID查询瀛楀吀椤�
    * @param dictionaryItemId
    * @return
    * @throws Exception
    */
    public DictionaryItem getDictionaryItemByPK(long dictionaryItemId)  throws  Exception {
        return dictionaryItemMapper.selectByPrimaryKey(dictionaryItemId);
    }


    /**
    * 通过MAP参数查询瀛楀吀椤�
    * @param params
    * @return
    * @throws Exception
    */
    public List<DictionaryItem> getDictionaryItemListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询瀛楀吀椤�
    * @param example
    * @return
    * @throws Exception
    */
    public List<DictionaryItem> getDictionaryItemListByExample(DictionaryItem_Example example) throws  Exception {
        return dictionaryItemMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询瀛楀吀椤�数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getDictionaryItemCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询瀛楀吀椤�数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getDictionaryItemCountByExample(DictionaryItem_Example example) throws  Exception {
        return dictionaryItemMapper.countByExample(example);
    }


  	//getter
 	
	public DictionaryItemMapper getDictionaryItemMapper(){
		return dictionaryItemMapper;
	}
	//setter
	public void setDictionaryItemMapper(DictionaryItemMapper dictionaryItemMapper){
    	this.dictionaryItemMapper = dictionaryItemMapper;
    }
}
