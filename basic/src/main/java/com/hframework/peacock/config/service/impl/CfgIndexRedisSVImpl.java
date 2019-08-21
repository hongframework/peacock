package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgIndexRedisMapper;
import com.hframework.peacock.config.domain.model.CfgIndexRedis;
import com.hframework.peacock.config.domain.model.CfgIndexRedis_Example;
import com.hframework.peacock.config.service.interfaces.ICfgIndexRedisSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgIndexRedisSV")
public class CfgIndexRedisSVImpl  implements ICfgIndexRedisSV {

	@Resource
	private CfgIndexRedisMapper cfgIndexRedisMapper;
  


    /**
    * 创建REDIS鎸囨爣
    * @param cfgIndexRedis
    * @return
    * @throws Exception
    */
    public int create(CfgIndexRedis cfgIndexRedis) throws Exception {
        return cfgIndexRedisMapper.insertSelective(cfgIndexRedis);
    }

    /**
    * 批量维护REDIS鎸囨爣
    * @param cfgIndexRediss
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgIndexRedis[] cfgIndexRediss) throws  Exception{
        int result = 0;
        if(cfgIndexRediss != null) {
            for (CfgIndexRedis cfgIndexRedis : cfgIndexRediss) {
                if(cfgIndexRedis.getId() == null) {
                    result += this.create(cfgIndexRedis);
                }else {
                    result += this.update(cfgIndexRedis);
                }
            }
        }
        return result;
    }

    /**
    * 更新REDIS鎸囨爣
    * @param cfgIndexRedis
    * @return
    * @throws Exception
    */
    public int update(CfgIndexRedis cfgIndexRedis) throws  Exception {
        return cfgIndexRedisMapper.updateByPrimaryKeySelective(cfgIndexRedis);
    }

    /**
    * 通过查询对象更新REDIS鎸囨爣
    * @param cfgIndexRedis
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgIndexRedis cfgIndexRedis, CfgIndexRedis_Example example) throws  Exception {
        return cfgIndexRedisMapper.updateByExampleSelective(cfgIndexRedis, example);
    }

    /**
    * 删除REDIS鎸囨爣
    * @param cfgIndexRedis
    * @return
    * @throws Exception
    */
    public int delete(CfgIndexRedis cfgIndexRedis) throws  Exception {
        return cfgIndexRedisMapper.deleteByPrimaryKey(cfgIndexRedis.getId());
    }

    /**
    * 删除REDIS鎸囨爣
    * @param cfgIndexRedisId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgIndexRedisId) throws  Exception {
        return cfgIndexRedisMapper.deleteByPrimaryKey(cfgIndexRedisId);
    }

    /**
    * 查找所有REDIS鎸囨爣
    * @return
    */
    public List<CfgIndexRedis> getCfgIndexRedisAll()  throws  Exception {
        return cfgIndexRedisMapper.selectByExample(new CfgIndexRedis_Example());
    }

    /**
    * 通过REDIS鎸囨爣ID查询REDIS鎸囨爣
    * @param cfgIndexRedisId
    * @return
    * @throws Exception
    */
    public CfgIndexRedis getCfgIndexRedisByPK(Integer cfgIndexRedisId)  throws  Exception {
        return cfgIndexRedisMapper.selectByPrimaryKey(cfgIndexRedisId);
    }


    /**
    * 通过MAP参数查询REDIS鎸囨爣
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgIndexRedis> getCfgIndexRedisListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询REDIS鎸囨爣
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgIndexRedis> getCfgIndexRedisListByExample(CfgIndexRedis_Example example) throws  Exception {
        return cfgIndexRedisMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询REDIS鎸囨爣数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgIndexRedisCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询REDIS鎸囨爣数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgIndexRedisCountByExample(CfgIndexRedis_Example example) throws  Exception {
        return cfgIndexRedisMapper.countByExample(example);
    }


  	//getter
 	
	public CfgIndexRedisMapper getCfgIndexRedisMapper(){
		return cfgIndexRedisMapper;
	}
	//setter
	public void setCfgIndexRedisMapper(CfgIndexRedisMapper cfgIndexRedisMapper){
    	this.cfgIndexRedisMapper = cfgIndexRedisMapper;
    }
}
