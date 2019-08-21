package com.hframework.peacock.config.service.impl;

import java.util.*;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.hframework.peacock.config.domain.model.CfgStaticExpander;
import com.hframework.peacock.config.domain.model.CfgStaticExpander_Example;
import com.hframework.peacock.config.dao.CfgStaticExpanderMapper;
import com.hframework.peacock.config.service.interfaces.ICfgStaticExpanderSV;

@Service("iCfgStaticExpanderSV")
public class CfgStaticExpanderSVImpl  implements ICfgStaticExpanderSV {

	@Resource
	private CfgStaticExpanderMapper cfgStaticExpanderMapper;
  


    /**
    * 创建静态扩展器
    * @param cfgStaticExpander
    * @return
    * @throws Exception
    */
    public int create(CfgStaticExpander cfgStaticExpander) throws Exception {
        return cfgStaticExpanderMapper.insertSelective(cfgStaticExpander);
    }

    /**
    * 批量维护静态扩展器
    * @param cfgStaticExpanders
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgStaticExpander[] cfgStaticExpanders) throws  Exception{
        int result = 0;
        if(cfgStaticExpanders != null) {
            for (CfgStaticExpander cfgStaticExpander : cfgStaticExpanders) {
                if(cfgStaticExpander.getId() == null) {
                    result += this.create(cfgStaticExpander);
                }else {
                    result += this.update(cfgStaticExpander);
                }
            }
        }
        return result;
    }

    /**
    * 更新静态扩展器
    * @param cfgStaticExpander
    * @return
    * @throws Exception
    */
    public int update(CfgStaticExpander cfgStaticExpander) throws  Exception {
        return cfgStaticExpanderMapper.updateByPrimaryKeySelective(cfgStaticExpander);
    }

    /**
    * 通过查询对象更新静态扩展器
    * @param cfgStaticExpander
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgStaticExpander cfgStaticExpander, CfgStaticExpander_Example example) throws  Exception {
        return cfgStaticExpanderMapper.updateByExampleSelective(cfgStaticExpander, example);
    }

    /**
    * 删除静态扩展器
    * @param cfgStaticExpander
    * @return
    * @throws Exception
    */
    public int delete(CfgStaticExpander cfgStaticExpander) throws  Exception {
        return cfgStaticExpanderMapper.deleteByPrimaryKey(cfgStaticExpander.getId());
    }

    /**
    * 删除静态扩展器
    * @param cfgStaticExpanderId
    * @return
    * @throws Exception
    */
    public int delete(long cfgStaticExpanderId) throws  Exception {
        return cfgStaticExpanderMapper.deleteByPrimaryKey(cfgStaticExpanderId);
    }

    /**
    * 查找所有静态扩展器
    * @return
    */
    public List<CfgStaticExpander> getCfgStaticExpanderAll()  throws  Exception {
        return cfgStaticExpanderMapper.selectByExample(new CfgStaticExpander_Example());
    }

    /**
    * 通过静态扩展器ID查询静态扩展器
    * @param cfgStaticExpanderId
    * @return
    * @throws Exception
    */
    public CfgStaticExpander getCfgStaticExpanderByPK(long cfgStaticExpanderId)  throws  Exception {
        return cfgStaticExpanderMapper.selectByPrimaryKey(cfgStaticExpanderId);
    }


    /**
    * 通过MAP参数查询静态扩展器
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgStaticExpander> getCfgStaticExpanderListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询静态扩展器
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgStaticExpander> getCfgStaticExpanderListByExample(CfgStaticExpander_Example example) throws  Exception {
        return cfgStaticExpanderMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询静态扩展器数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgStaticExpanderCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询静态扩展器数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgStaticExpanderCountByExample(CfgStaticExpander_Example example) throws  Exception {
        return cfgStaticExpanderMapper.countByExample(example);
    }


  	//getter
 	
	public CfgStaticExpanderMapper getCfgStaticExpanderMapper(){
		return cfgStaticExpanderMapper;
	}
	//setter
	public void setCfgStaticExpanderMapper(CfgStaticExpanderMapper cfgStaticExpanderMapper){
    	this.cfgStaticExpanderMapper = cfgStaticExpanderMapper;
    }
}
