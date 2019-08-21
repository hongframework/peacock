package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgRuntimeHandlerMapper;
import com.hframework.peacock.config.domain.model.CfgRuntimeHandler;
import com.hframework.peacock.config.domain.model.CfgRuntimeHandler_Example;
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeHandlerSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgRuntimeHandlerSV")
public class CfgRuntimeHandlerSVImpl  implements ICfgRuntimeHandlerSV {

	@Resource
	private CfgRuntimeHandlerMapper cfgRuntimeHandlerMapper;
  


    /**
    * 创建动态Handler
    * @param cfgRuntimeHandler
    * @return
    * @throws Exception
    */
    public int create(CfgRuntimeHandler cfgRuntimeHandler) throws Exception {
        return cfgRuntimeHandlerMapper.insertSelective(cfgRuntimeHandler);
    }

    /**
    * 批量维护动态Handler
    * @param cfgRuntimeHandlers
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgRuntimeHandler[] cfgRuntimeHandlers) throws  Exception{
        int result = 0;
        if(cfgRuntimeHandlers != null) {
            for (CfgRuntimeHandler cfgRuntimeHandler : cfgRuntimeHandlers) {
                if(cfgRuntimeHandler.getId() == null) {
                    result += this.create(cfgRuntimeHandler);
                }else {
                    result += this.update(cfgRuntimeHandler);
                }
            }
        }
        return result;
    }

    /**
    * 更新动态Handler
    * @param cfgRuntimeHandler
    * @return
    * @throws Exception
    */
    public int update(CfgRuntimeHandler cfgRuntimeHandler) throws  Exception {
        return cfgRuntimeHandlerMapper.updateByPrimaryKeySelective(cfgRuntimeHandler);
    }

    /**
    * 通过查询对象更新动态Handler
    * @param cfgRuntimeHandler
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgRuntimeHandler cfgRuntimeHandler, CfgRuntimeHandler_Example example) throws  Exception {
        return cfgRuntimeHandlerMapper.updateByExampleSelective(cfgRuntimeHandler, example);
    }

    /**
    * 删除动态Handler
    * @param cfgRuntimeHandler
    * @return
    * @throws Exception
    */
    public int delete(CfgRuntimeHandler cfgRuntimeHandler) throws  Exception {
        return cfgRuntimeHandlerMapper.deleteByPrimaryKey(cfgRuntimeHandler.getId());
    }

    /**
    * 删除动态Handler
    * @param cfgRuntimeHandlerId
    * @return
    * @throws Exception
    */
    public int delete(long cfgRuntimeHandlerId) throws  Exception {
        return cfgRuntimeHandlerMapper.deleteByPrimaryKey(cfgRuntimeHandlerId);
    }

    /**
    * 查找所有动态Handler
    * @return
    */
    public List<CfgRuntimeHandler> getCfgRuntimeHandlerAll()  throws  Exception {
        return cfgRuntimeHandlerMapper.selectByExampleWithBLOBs(new CfgRuntimeHandler_Example());
    }

    /**
    * 通过动态HandlerID查询动态Handler
    * @param cfgRuntimeHandlerId
    * @return
    * @throws Exception
    */
    public CfgRuntimeHandler getCfgRuntimeHandlerByPK(long cfgRuntimeHandlerId)  throws  Exception {
        return cfgRuntimeHandlerMapper.selectByPrimaryKey(cfgRuntimeHandlerId);
    }


    /**
    * 通过MAP参数查询动态Handler
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeHandler> getCfgRuntimeHandlerListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询动态Handler
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgRuntimeHandler> getCfgRuntimeHandlerListByExample(CfgRuntimeHandler_Example example) throws  Exception {
        return cfgRuntimeHandlerMapper.selectByExampleWithBLOBs(example);
    }

    /**
    * 通过MAP参数查询动态Handler数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeHandlerCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询动态Handler数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgRuntimeHandlerCountByExample(CfgRuntimeHandler_Example example) throws  Exception {
        return cfgRuntimeHandlerMapper.countByExample(example);
    }


  	//getter
 	
	public CfgRuntimeHandlerMapper getCfgRuntimeHandlerMapper(){
		return cfgRuntimeHandlerMapper;
	}
	//setter
	public void setCfgRuntimeHandlerMapper(CfgRuntimeHandlerMapper cfgRuntimeHandlerMapper){
    	this.cfgRuntimeHandlerMapper = cfgRuntimeHandlerMapper;
    }
}
