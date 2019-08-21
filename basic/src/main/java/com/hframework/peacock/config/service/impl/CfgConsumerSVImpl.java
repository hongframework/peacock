package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgConsumerMapper;
import com.hframework.peacock.config.domain.model.CfgConsumer;
import com.hframework.peacock.config.domain.model.CfgConsumer_Example;
import com.hframework.peacock.config.service.interfaces.ICfgConsumerSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgConsumerSV")
public class CfgConsumerSVImpl  implements ICfgConsumerSV {

	@Resource
	private CfgConsumerMapper cfgConsumerMapper;
  


    /**
    * 创建API娑堣垂鑰�
    * @param cfgConsumer
    * @return
    * @throws Exception
    */
    public int create(CfgConsumer cfgConsumer) throws Exception {
        return cfgConsumerMapper.insertSelective(cfgConsumer);
    }

    /**
    * 批量维护API娑堣垂鑰�
    * @param cfgConsumers
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgConsumer[] cfgConsumers) throws  Exception{
        int result = 0;
        if(cfgConsumers != null) {
            for (CfgConsumer cfgConsumer : cfgConsumers) {
                if(cfgConsumer.getId() == null) {
                    result += this.create(cfgConsumer);
                }else {
                    result += this.update(cfgConsumer);
                }
            }
        }
        return result;
    }

    /**
    * 更新API娑堣垂鑰�
    * @param cfgConsumer
    * @return
    * @throws Exception
    */
    public int update(CfgConsumer cfgConsumer) throws  Exception {
        return cfgConsumerMapper.updateByPrimaryKeySelective(cfgConsumer);
    }

    /**
    * 通过查询对象更新API娑堣垂鑰�
    * @param cfgConsumer
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgConsumer cfgConsumer, CfgConsumer_Example example) throws  Exception {
        return cfgConsumerMapper.updateByExampleSelective(cfgConsumer, example);
    }

    /**
    * 删除API娑堣垂鑰�
    * @param cfgConsumer
    * @return
    * @throws Exception
    */
    public int delete(CfgConsumer cfgConsumer) throws  Exception {
        return cfgConsumerMapper.deleteByPrimaryKey(cfgConsumer.getId());
    }

    /**
    * 删除API娑堣垂鑰�
    * @param cfgConsumerId
    * @return
    * @throws Exception
    */
    public int delete(long cfgConsumerId) throws  Exception {
        return cfgConsumerMapper.deleteByPrimaryKey(cfgConsumerId);
    }

    /**
    * 查找所有API娑堣垂鑰�
    * @return
    */
    public List<CfgConsumer> getCfgConsumerAll()  throws  Exception {
        return cfgConsumerMapper.selectByExample(new CfgConsumer_Example());
    }

    /**
    * 通过API娑堣垂鑰�ID查询API娑堣垂鑰�
    * @param cfgConsumerId
    * @return
    * @throws Exception
    */
    public CfgConsumer getCfgConsumerByPK(long cfgConsumerId)  throws  Exception {
        return cfgConsumerMapper.selectByPrimaryKey(cfgConsumerId);
    }


    /**
    * 通过MAP参数查询API娑堣垂鑰�
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgConsumer> getCfgConsumerListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询API娑堣垂鑰�
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgConsumer> getCfgConsumerListByExample(CfgConsumer_Example example) throws  Exception {
        return cfgConsumerMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询API娑堣垂鑰�数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgConsumerCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询API娑堣垂鑰�数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgConsumerCountByExample(CfgConsumer_Example example) throws  Exception {
        return cfgConsumerMapper.countByExample(example);
    }


  	//getter
 	
	public CfgConsumerMapper getCfgConsumerMapper(){
		return cfgConsumerMapper;
	}
	//setter
	public void setCfgConsumerMapper(CfgConsumerMapper cfgConsumerMapper){
    	this.cfgConsumerMapper = cfgConsumerMapper;
    }
}
