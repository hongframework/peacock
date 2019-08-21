package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgIndexMysqlMapper;
import com.hframework.peacock.config.domain.model.CfgIndexMysql;
import com.hframework.peacock.config.domain.model.CfgIndexMysql_Example;
import com.hframework.peacock.config.service.interfaces.ICfgIndexMysqlSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgIndexMysqlSV")
public class CfgIndexMysqlSVImpl  implements ICfgIndexMysqlSV {

	@Resource
	private CfgIndexMysqlMapper cfgIndexMysqlMapper;
  


    /**
    * 创建MYSQL鎸囨爣
    * @param cfgIndexMysql
    * @return
    * @throws Exception
    */
    public int create(CfgIndexMysql cfgIndexMysql) throws Exception {
        return cfgIndexMysqlMapper.insertSelective(cfgIndexMysql);
    }

    /**
    * 批量维护MYSQL鎸囨爣
    * @param cfgIndexMysqls
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgIndexMysql[] cfgIndexMysqls) throws  Exception{
        int result = 0;
        if(cfgIndexMysqls != null) {
            for (CfgIndexMysql cfgIndexMysql : cfgIndexMysqls) {
                if(cfgIndexMysql.getId() == null) {
                    result += this.create(cfgIndexMysql);
                }else {
                    result += this.update(cfgIndexMysql);
                }
            }
        }
        return result;
    }

    /**
    * 更新MYSQL鎸囨爣
    * @param cfgIndexMysql
    * @return
    * @throws Exception
    */
    public int update(CfgIndexMysql cfgIndexMysql) throws  Exception {
        return cfgIndexMysqlMapper.updateByPrimaryKeySelective(cfgIndexMysql);
    }

    /**
    * 通过查询对象更新MYSQL鎸囨爣
    * @param cfgIndexMysql
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgIndexMysql cfgIndexMysql, CfgIndexMysql_Example example) throws  Exception {
        return cfgIndexMysqlMapper.updateByExampleSelective(cfgIndexMysql, example);
    }

    /**
    * 删除MYSQL鎸囨爣
    * @param cfgIndexMysql
    * @return
    * @throws Exception
    */
    public int delete(CfgIndexMysql cfgIndexMysql) throws  Exception {
        return cfgIndexMysqlMapper.deleteByPrimaryKey(cfgIndexMysql.getId());
    }

    /**
    * 删除MYSQL鎸囨爣
    * @param cfgIndexMysqlId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgIndexMysqlId) throws  Exception {
        return cfgIndexMysqlMapper.deleteByPrimaryKey(cfgIndexMysqlId);
    }

    /**
    * 查找所有MYSQL鎸囨爣
    * @return
    */
    public List<CfgIndexMysql> getCfgIndexMysqlAll()  throws  Exception {
        return cfgIndexMysqlMapper.selectByExample(new CfgIndexMysql_Example());
    }

    /**
    * 通过MYSQL鎸囨爣ID查询MYSQL鎸囨爣
    * @param cfgIndexMysqlId
    * @return
    * @throws Exception
    */
    public CfgIndexMysql getCfgIndexMysqlByPK(Integer cfgIndexMysqlId)  throws  Exception {
        return cfgIndexMysqlMapper.selectByPrimaryKey(cfgIndexMysqlId);
    }


    /**
    * 通过MAP参数查询MYSQL鎸囨爣
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgIndexMysql> getCfgIndexMysqlListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询MYSQL鎸囨爣
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgIndexMysql> getCfgIndexMysqlListByExample(CfgIndexMysql_Example example) throws  Exception {
        return cfgIndexMysqlMapper.selectByExampleWithBLOBs(example);
    }

    /**
    * 通过MAP参数查询MYSQL鎸囨爣数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgIndexMysqlCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询MYSQL鎸囨爣数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgIndexMysqlCountByExample(CfgIndexMysql_Example example) throws  Exception {
        return cfgIndexMysqlMapper.countByExample(example);
    }


  	//getter
 	
	public CfgIndexMysqlMapper getCfgIndexMysqlMapper(){
		return cfgIndexMysqlMapper;
	}
	//setter
	public void setCfgIndexMysqlMapper(CfgIndexMysqlMapper cfgIndexMysqlMapper){
    	this.cfgIndexMysqlMapper = cfgIndexMysqlMapper;
    }
}
