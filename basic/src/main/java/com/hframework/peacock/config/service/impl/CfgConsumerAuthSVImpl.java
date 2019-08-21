package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgConsumerAuthMapper;
import com.hframework.peacock.config.domain.model.CfgConsumerAuth;
import com.hframework.peacock.config.domain.model.CfgConsumerAuth_Example;
import com.hframework.peacock.config.service.interfaces.ICfgConsumerAuthSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgConsumerAuthSV")
public class CfgConsumerAuthSVImpl  implements ICfgConsumerAuthSV {

	@Resource
	private CfgConsumerAuthMapper cfgConsumerAuthMapper;
  


    /**
    * 创建API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuth
    * @return
    * @throws Exception
    */
    public int create(CfgConsumerAuth cfgConsumerAuth) throws Exception {
        return cfgConsumerAuthMapper.insertSelective(cfgConsumerAuth);
    }

    /**
    * 批量维护API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuths
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgConsumerAuth[] cfgConsumerAuths) throws  Exception{
        int result = 0;
        if(cfgConsumerAuths != null) {
            for (CfgConsumerAuth cfgConsumerAuth : cfgConsumerAuths) {
                if(cfgConsumerAuth.getId() == null) {
                    result += this.create(cfgConsumerAuth);
                }else {
                    result += this.update(cfgConsumerAuth);
                }
            }
        }
        return result;
    }

    /**
    * 更新API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuth
    * @return
    * @throws Exception
    */
    public int update(CfgConsumerAuth cfgConsumerAuth) throws  Exception {
        return cfgConsumerAuthMapper.updateByPrimaryKeySelective(cfgConsumerAuth);
    }

    /**
    * 通过查询对象更新API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuth
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgConsumerAuth cfgConsumerAuth, CfgConsumerAuth_Example example) throws  Exception {
        return cfgConsumerAuthMapper.updateByExampleSelective(cfgConsumerAuth, example);
    }

    /**
    * 删除API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuth
    * @return
    * @throws Exception
    */
    public int delete(CfgConsumerAuth cfgConsumerAuth) throws  Exception {
        return cfgConsumerAuthMapper.deleteByPrimaryKey(cfgConsumerAuth.getId());
    }

    /**
    * 删除API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuthId
    * @return
    * @throws Exception
    */
    public int delete(long cfgConsumerAuthId) throws  Exception {
        return cfgConsumerAuthMapper.deleteByPrimaryKey(cfgConsumerAuthId);
    }

    /**
    * 查找所有API娑堣垂鑰呮巿鏉�
    * @return
    */
    public List<CfgConsumerAuth> getCfgConsumerAuthAll()  throws  Exception {
        return cfgConsumerAuthMapper.selectByExample(new CfgConsumerAuth_Example());
    }

    /**
    * 通过API娑堣垂鑰呮巿鏉�ID查询API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuthId
    * @return
    * @throws Exception
    */
    public CfgConsumerAuth getCfgConsumerAuthByPK(long cfgConsumerAuthId)  throws  Exception {
        return cfgConsumerAuthMapper.selectByPrimaryKey(cfgConsumerAuthId);
    }


    /**
    * 通过MAP参数查询API娑堣垂鑰呮巿鏉�
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgConsumerAuth> getCfgConsumerAuthListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询API娑堣垂鑰呮巿鏉�
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgConsumerAuth> getCfgConsumerAuthListByExample(CfgConsumerAuth_Example example) throws  Exception {
        return cfgConsumerAuthMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询API娑堣垂鑰呮巿鏉�数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgConsumerAuthCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询API娑堣垂鑰呮巿鏉�数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgConsumerAuthCountByExample(CfgConsumerAuth_Example example) throws  Exception {
        return cfgConsumerAuthMapper.countByExample(example);
    }


  	//getter
 	
	public CfgConsumerAuthMapper getCfgConsumerAuthMapper(){
		return cfgConsumerAuthMapper;
	}
	//setter
	public void setCfgConsumerAuthMapper(CfgConsumerAuthMapper cfgConsumerAuthMapper){
    	this.cfgConsumerAuthMapper = cfgConsumerAuthMapper;
    }
}
