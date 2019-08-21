package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgDatasouceHbaseMapper;
import com.hframework.peacock.config.domain.model.CfgDatasouceHbase;
import com.hframework.peacock.config.domain.model.CfgDatasouceHbase_Example;
import com.hframework.peacock.config.service.interfaces.ICfgDatasouceHbaseSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgDatasouceHbaseSV")
public class CfgDatasouceHbaseSVImpl  implements ICfgDatasouceHbaseSV {

	@Resource
	private CfgDatasouceHbaseMapper cfgDatasouceHbaseMapper;
  


    /**
    * 创建HBASE
    * @param cfgDatasouceHbase
    * @return
    * @throws Exception
    */
    public int create(CfgDatasouceHbase cfgDatasouceHbase) throws Exception {
        return cfgDatasouceHbaseMapper.insertSelective(cfgDatasouceHbase);
    }

    /**
    * 批量维护HBASE
    * @param cfgDatasouceHbases
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgDatasouceHbase[] cfgDatasouceHbases) throws  Exception{
        int result = 0;
        if(cfgDatasouceHbases != null) {
            for (CfgDatasouceHbase cfgDatasouceHbase : cfgDatasouceHbases) {
                if(cfgDatasouceHbase.getId() == null) {
                    result += this.create(cfgDatasouceHbase);
                }else {
                    result += this.update(cfgDatasouceHbase);
                }
            }
        }
        return result;
    }

    /**
    * 更新HBASE
    * @param cfgDatasouceHbase
    * @return
    * @throws Exception
    */
    public int update(CfgDatasouceHbase cfgDatasouceHbase) throws  Exception {
        return cfgDatasouceHbaseMapper.updateByPrimaryKeySelective(cfgDatasouceHbase);
    }

    /**
    * 通过查询对象更新HBASE
    * @param cfgDatasouceHbase
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgDatasouceHbase cfgDatasouceHbase, CfgDatasouceHbase_Example example) throws  Exception {
        return cfgDatasouceHbaseMapper.updateByExampleSelective(cfgDatasouceHbase, example);
    }

    /**
    * 删除HBASE
    * @param cfgDatasouceHbase
    * @return
    * @throws Exception
    */
    public int delete(CfgDatasouceHbase cfgDatasouceHbase) throws  Exception {
        return cfgDatasouceHbaseMapper.deleteByPrimaryKey(cfgDatasouceHbase.getId());
    }

    /**
    * 删除HBASE
    * @param cfgDatasouceHbaseId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgDatasouceHbaseId) throws  Exception {
        return cfgDatasouceHbaseMapper.deleteByPrimaryKey(cfgDatasouceHbaseId);
    }

    /**
    * 查找所有HBASE
    * @return
    */
    public List<CfgDatasouceHbase> getCfgDatasouceHbaseAll()  throws  Exception {
        return cfgDatasouceHbaseMapper.selectByExample(new CfgDatasouceHbase_Example());
    }

    /**
    * 通过HBASEID查询HBASE
    * @param cfgDatasouceHbaseId
    * @return
    * @throws Exception
    */
    public CfgDatasouceHbase getCfgDatasouceHbaseByPK(Integer cfgDatasouceHbaseId)  throws  Exception {
        return cfgDatasouceHbaseMapper.selectByPrimaryKey(cfgDatasouceHbaseId);
    }


    /**
    * 通过MAP参数查询HBASE
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgDatasouceHbase> getCfgDatasouceHbaseListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询HBASE
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgDatasouceHbase> getCfgDatasouceHbaseListByExample(CfgDatasouceHbase_Example example) throws  Exception {
        return cfgDatasouceHbaseMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询HBASE数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgDatasouceHbaseCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询HBASE数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgDatasouceHbaseCountByExample(CfgDatasouceHbase_Example example) throws  Exception {
        return cfgDatasouceHbaseMapper.countByExample(example);
    }


  	//getter
 	
	public CfgDatasouceHbaseMapper getCfgDatasouceHbaseMapper(){
		return cfgDatasouceHbaseMapper;
	}
	//setter
	public void setCfgDatasouceHbaseMapper(CfgDatasouceHbaseMapper cfgDatasouceHbaseMapper){
    	this.cfgDatasouceHbaseMapper = cfgDatasouceHbaseMapper;
    }
}
