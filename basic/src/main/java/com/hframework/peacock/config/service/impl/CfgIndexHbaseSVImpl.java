package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgIndexHbaseMapper;
import com.hframework.peacock.config.domain.model.CfgIndexHbase;
import com.hframework.peacock.config.domain.model.CfgIndexHbase_Example;
import com.hframework.peacock.config.service.interfaces.ICfgIndexHbaseSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgIndexHbaseSV")
public class CfgIndexHbaseSVImpl  implements ICfgIndexHbaseSV {

	@Resource
	private CfgIndexHbaseMapper cfgIndexHbaseMapper;
  


    /**
    * 创建HBASE鎸囨爣
    * @param cfgIndexHbase
    * @return
    * @throws Exception
    */
    public int create(CfgIndexHbase cfgIndexHbase) throws Exception {
        return cfgIndexHbaseMapper.insertSelective(cfgIndexHbase);
    }

    /**
    * 批量维护HBASE鎸囨爣
    * @param cfgIndexHbases
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgIndexHbase[] cfgIndexHbases) throws  Exception{
        int result = 0;
        if(cfgIndexHbases != null) {
            for (CfgIndexHbase cfgIndexHbase : cfgIndexHbases) {
                if(cfgIndexHbase.getId() == null) {
                    result += this.create(cfgIndexHbase);
                }else {
                    result += this.update(cfgIndexHbase);
                }
            }
        }
        return result;
    }

    /**
    * 更新HBASE鎸囨爣
    * @param cfgIndexHbase
    * @return
    * @throws Exception
    */
    public int update(CfgIndexHbase cfgIndexHbase) throws  Exception {
        return cfgIndexHbaseMapper.updateByPrimaryKeySelective(cfgIndexHbase);
    }

    /**
    * 通过查询对象更新HBASE鎸囨爣
    * @param cfgIndexHbase
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgIndexHbase cfgIndexHbase, CfgIndexHbase_Example example) throws  Exception {
        return cfgIndexHbaseMapper.updateByExampleSelective(cfgIndexHbase, example);
    }

    /**
    * 删除HBASE鎸囨爣
    * @param cfgIndexHbase
    * @return
    * @throws Exception
    */
    public int delete(CfgIndexHbase cfgIndexHbase) throws  Exception {
        return cfgIndexHbaseMapper.deleteByPrimaryKey(cfgIndexHbase.getId());
    }

    /**
    * 删除HBASE鎸囨爣
    * @param cfgIndexHbaseId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgIndexHbaseId) throws  Exception {
        return cfgIndexHbaseMapper.deleteByPrimaryKey(cfgIndexHbaseId);
    }

    /**
    * 查找所有HBASE鎸囨爣
    * @return
    */
    public List<CfgIndexHbase> getCfgIndexHbaseAll()  throws  Exception {
        return cfgIndexHbaseMapper.selectByExample(new CfgIndexHbase_Example());
    }

    /**
    * 通过HBASE鎸囨爣ID查询HBASE鎸囨爣
    * @param cfgIndexHbaseId
    * @return
    * @throws Exception
    */
    public CfgIndexHbase getCfgIndexHbaseByPK(Integer cfgIndexHbaseId)  throws  Exception {
        return cfgIndexHbaseMapper.selectByPrimaryKey(cfgIndexHbaseId);
    }


    /**
    * 通过MAP参数查询HBASE鎸囨爣
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgIndexHbase> getCfgIndexHbaseListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询HBASE鎸囨爣
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgIndexHbase> getCfgIndexHbaseListByExample(CfgIndexHbase_Example example) throws  Exception {
        return cfgIndexHbaseMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询HBASE鎸囨爣数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgIndexHbaseCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询HBASE鎸囨爣数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgIndexHbaseCountByExample(CfgIndexHbase_Example example) throws  Exception {
        return cfgIndexHbaseMapper.countByExample(example);
    }


  	//getter
 	
	public CfgIndexHbaseMapper getCfgIndexHbaseMapper(){
		return cfgIndexHbaseMapper;
	}
	//setter
	public void setCfgIndexHbaseMapper(CfgIndexHbaseMapper cfgIndexHbaseMapper){
    	this.cfgIndexHbaseMapper = cfgIndexHbaseMapper;
    }
}
