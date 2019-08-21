package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgIndexMapper;
import com.hframework.peacock.config.domain.model.CfgIndex;
import com.hframework.peacock.config.domain.model.CfgIndex_Example;
import com.hframework.peacock.config.service.interfaces.ICfgIndexSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgIndexSV")
public class CfgIndexSVImpl  implements ICfgIndexSV {

	@Resource
	private CfgIndexMapper cfgIndexMapper;
  


    /**
    * 创建鎸囨爣
    * @param cfgIndex
    * @return
    * @throws Exception
    */
    public int create(CfgIndex cfgIndex) throws Exception {
        return cfgIndexMapper.insertSelective(cfgIndex);
    }

    /**
    * 批量维护鎸囨爣
    * @param cfgIndexs
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgIndex[] cfgIndexs) throws  Exception{
        int result = 0;
        if(cfgIndexs != null) {
            for (CfgIndex cfgIndex : cfgIndexs) {
                if(cfgIndex.getId() == null) {
                    result += this.create(cfgIndex);
                }else {
                    result += this.update(cfgIndex);
                }
            }
        }
        return result;
    }

    /**
    * 更新鎸囨爣
    * @param cfgIndex
    * @return
    * @throws Exception
    */
    public int update(CfgIndex cfgIndex) throws  Exception {
        return cfgIndexMapper.updateByPrimaryKeySelective(cfgIndex);
    }

    /**
    * 通过查询对象更新鎸囨爣
    * @param cfgIndex
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgIndex cfgIndex, CfgIndex_Example example) throws  Exception {
        return cfgIndexMapper.updateByExampleSelective(cfgIndex, example);
    }

    /**
    * 删除鎸囨爣
    * @param cfgIndex
    * @return
    * @throws Exception
    */
    public int delete(CfgIndex cfgIndex) throws  Exception {
        return cfgIndexMapper.deleteByPrimaryKey(cfgIndex.getId());
    }

    /**
    * 删除鎸囨爣
    * @param cfgIndexId
    * @return
    * @throws Exception
    */
    public int delete(long cfgIndexId) throws  Exception {
        return cfgIndexMapper.deleteByPrimaryKey(Integer.parseInt(String.valueOf(cfgIndexId)));
    }

    /**
    * 查找所有鎸囨爣
    * @return
    */
    public List<CfgIndex> getCfgIndexAll()  throws  Exception {
        return cfgIndexMapper.selectByExample(new CfgIndex_Example());
    }

    /**
    * 通过鎸囨爣ID查询鎸囨爣
    * @param cfgIndexId
    * @return
    * @throws Exception
    */
    public CfgIndex getCfgIndexByPK(long cfgIndexId)  throws  Exception {
        return cfgIndexMapper.selectByPrimaryKey(Integer.parseInt(String.valueOf(cfgIndexId)));
    }


    /**
    * 通过MAP参数查询鎸囨爣
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgIndex> getCfgIndexListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询鎸囨爣
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgIndex> getCfgIndexListByExample(CfgIndex_Example example) throws  Exception {
        return cfgIndexMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询鎸囨爣数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgIndexCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询鎸囨爣数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgIndexCountByExample(CfgIndex_Example example) throws  Exception {
        return cfgIndexMapper.countByExample(example);
    }


  	//getter
 	
	public CfgIndexMapper getCfgIndexMapper(){
		return cfgIndexMapper;
	}
	//setter
	public void setCfgIndexMapper(CfgIndexMapper cfgIndexMapper){
    	this.cfgIndexMapper = cfgIndexMapper;
    }
}
