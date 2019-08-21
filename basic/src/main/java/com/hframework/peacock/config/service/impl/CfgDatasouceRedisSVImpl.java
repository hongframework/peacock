package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgDatasouceRedisMapper;
import com.hframework.peacock.config.domain.model.CfgDatasouceRedis;
import com.hframework.peacock.config.domain.model.CfgDatasouceRedis_Example;
import com.hframework.peacock.config.service.interfaces.ICfgDatasouceRedisSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgDatasouceRedisSV")
public class CfgDatasouceRedisSVImpl  implements ICfgDatasouceRedisSV {

	@Resource
	private CfgDatasouceRedisMapper cfgDatasouceRedisMapper;
  


    /**
    * 创建REDIS
    * @param cfgDatasouceRedis
    * @return
    * @throws Exception
    */
    public int create(CfgDatasouceRedis cfgDatasouceRedis) throws Exception {
        return cfgDatasouceRedisMapper.insertSelective(cfgDatasouceRedis);
    }

    /**
    * 批量维护REDIS
    * @param cfgDatasouceRediss
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgDatasouceRedis[] cfgDatasouceRediss) throws  Exception{
        int result = 0;
        if(cfgDatasouceRediss != null) {
            for (CfgDatasouceRedis cfgDatasouceRedis : cfgDatasouceRediss) {
                if(cfgDatasouceRedis.getId() == null) {
                    result += this.create(cfgDatasouceRedis);
                }else {
                    result += this.update(cfgDatasouceRedis);
                }
            }
        }
        return result;
    }

    /**
    * 更新REDIS
    * @param cfgDatasouceRedis
    * @return
    * @throws Exception
    */
    public int update(CfgDatasouceRedis cfgDatasouceRedis) throws  Exception {
        return cfgDatasouceRedisMapper.updateByPrimaryKeySelective(cfgDatasouceRedis);
    }

    /**
    * 通过查询对象更新REDIS
    * @param cfgDatasouceRedis
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgDatasouceRedis cfgDatasouceRedis, CfgDatasouceRedis_Example example) throws  Exception {
        return cfgDatasouceRedisMapper.updateByExampleSelective(cfgDatasouceRedis, example);
    }

    /**
    * 删除REDIS
    * @param cfgDatasouceRedis
    * @return
    * @throws Exception
    */
    public int delete(CfgDatasouceRedis cfgDatasouceRedis) throws  Exception {
        return cfgDatasouceRedisMapper.deleteByPrimaryKey(cfgDatasouceRedis.getId());
    }

    /**
    * 删除REDIS
    * @param cfgDatasouceRedisId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgDatasouceRedisId) throws  Exception {
        return cfgDatasouceRedisMapper.deleteByPrimaryKey(cfgDatasouceRedisId);
    }

    /**
    * 查找所有REDIS
    * @return
    */
    public List<CfgDatasouceRedis> getCfgDatasouceRedisAll()  throws  Exception {
        return cfgDatasouceRedisMapper.selectByExample(new CfgDatasouceRedis_Example());
    }

    /**
    * 通过REDISID查询REDIS
    * @param cfgDatasouceRedisId
    * @return
    * @throws Exception
    */
    public CfgDatasouceRedis getCfgDatasouceRedisByPK(Integer cfgDatasouceRedisId)  throws  Exception {
        return cfgDatasouceRedisMapper.selectByPrimaryKey(cfgDatasouceRedisId);
    }


    /**
    * 通过MAP参数查询REDIS
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgDatasouceRedis> getCfgDatasouceRedisListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询REDIS
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgDatasouceRedis> getCfgDatasouceRedisListByExample(CfgDatasouceRedis_Example example) throws  Exception {
        return cfgDatasouceRedisMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询REDIS数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgDatasouceRedisCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询REDIS数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgDatasouceRedisCountByExample(CfgDatasouceRedis_Example example) throws  Exception {
        return cfgDatasouceRedisMapper.countByExample(example);
    }


  	//getter
 	
	public CfgDatasouceRedisMapper getCfgDatasouceRedisMapper(){
		return cfgDatasouceRedisMapper;
	}
	//setter
	public void setCfgDatasouceRedisMapper(CfgDatasouceRedisMapper cfgDatasouceRedisMapper){
    	this.cfgDatasouceRedisMapper = cfgDatasouceRedisMapper;
    }
}
