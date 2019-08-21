package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgDatasouceMysqlMapper;
import com.hframework.peacock.config.domain.model.CfgDatasouceMysql;
import com.hframework.peacock.config.domain.model.CfgDatasouceMysql_Example;
import com.hframework.peacock.config.service.interfaces.ICfgDatasouceMysqlSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgDatasouceMysqlSV")
public class CfgDatasouceMysqlSVImpl  implements ICfgDatasouceMysqlSV {

	@Resource
	private CfgDatasouceMysqlMapper cfgDatasouceMysqlMapper;
  


    /**
    * 创建MYSQL
    * @param cfgDatasouceMysql
    * @return
    * @throws Exception
    */
    public int create(CfgDatasouceMysql cfgDatasouceMysql) throws Exception {
        return cfgDatasouceMysqlMapper.insertSelective(cfgDatasouceMysql);
    }

    /**
    * 批量维护MYSQL
    * @param cfgDatasouceMysqls
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgDatasouceMysql[] cfgDatasouceMysqls) throws  Exception{
        int result = 0;
        if(cfgDatasouceMysqls != null) {
            for (CfgDatasouceMysql cfgDatasouceMysql : cfgDatasouceMysqls) {
                if(cfgDatasouceMysql.getId() == null) {
                    result += this.create(cfgDatasouceMysql);
                }else {
                    result += this.update(cfgDatasouceMysql);
                }
            }
        }
        return result;
    }

    /**
    * 更新MYSQL
    * @param cfgDatasouceMysql
    * @return
    * @throws Exception
    */
    public int update(CfgDatasouceMysql cfgDatasouceMysql) throws  Exception {
        return cfgDatasouceMysqlMapper.updateByPrimaryKeySelective(cfgDatasouceMysql);
    }

    /**
    * 通过查询对象更新MYSQL
    * @param cfgDatasouceMysql
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgDatasouceMysql cfgDatasouceMysql, CfgDatasouceMysql_Example example) throws  Exception {
        return cfgDatasouceMysqlMapper.updateByExampleSelective(cfgDatasouceMysql, example);
    }

    /**
    * 删除MYSQL
    * @param cfgDatasouceMysql
    * @return
    * @throws Exception
    */
    public int delete(CfgDatasouceMysql cfgDatasouceMysql) throws  Exception {
        return cfgDatasouceMysqlMapper.deleteByPrimaryKey(cfgDatasouceMysql.getId());
    }

    /**
    * 删除MYSQL
    * @param cfgDatasouceMysqlId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgDatasouceMysqlId) throws  Exception {
        return cfgDatasouceMysqlMapper.deleteByPrimaryKey(cfgDatasouceMysqlId);
    }

    /**
    * 查找所有MYSQL
    * @return
    */
    public List<CfgDatasouceMysql> getCfgDatasouceMysqlAll()  throws  Exception {
        return cfgDatasouceMysqlMapper.selectByExample(new CfgDatasouceMysql_Example());
    }

    /**
    * 通过MYSQLID查询MYSQL
    * @param cfgDatasouceMysqlId
    * @return
    * @throws Exception
    */
    public CfgDatasouceMysql getCfgDatasouceMysqlByPK(Integer cfgDatasouceMysqlId)  throws  Exception {
        return cfgDatasouceMysqlMapper.selectByPrimaryKey(cfgDatasouceMysqlId);
    }


    /**
    * 通过MAP参数查询MYSQL
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgDatasouceMysql> getCfgDatasouceMysqlListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询MYSQL
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgDatasouceMysql> getCfgDatasouceMysqlListByExample(CfgDatasouceMysql_Example example) throws  Exception {
        return cfgDatasouceMysqlMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询MYSQL数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgDatasouceMysqlCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询MYSQL数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgDatasouceMysqlCountByExample(CfgDatasouceMysql_Example example) throws  Exception {
        return cfgDatasouceMysqlMapper.countByExample(example);
    }


  	//getter
 	
	public CfgDatasouceMysqlMapper getCfgDatasouceMysqlMapper(){
		return cfgDatasouceMysqlMapper;
	}
	//setter
	public void setCfgDatasouceMysqlMapper(CfgDatasouceMysqlMapper cfgDatasouceMysqlMapper){
    	this.cfgDatasouceMysqlMapper = cfgDatasouceMysqlMapper;
    }
}
