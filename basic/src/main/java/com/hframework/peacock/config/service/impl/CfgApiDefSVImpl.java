package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgApiDefMapper;
import com.hframework.peacock.config.domain.model.CfgApiDef;
import com.hframework.peacock.config.domain.model.CfgApiDef_Example;
import com.hframework.peacock.config.service.interfaces.ICfgApiDefSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgApiDefSV")
public class CfgApiDefSVImpl  implements ICfgApiDefSV {

	@Resource
	private CfgApiDefMapper cfgApiDefMapper;
  


    /**
    * 创建API瀹氫箟琛�
    * @param cfgApiDef
    * @return
    * @throws Exception
    */
    public int create(CfgApiDef cfgApiDef) throws Exception {
        return cfgApiDefMapper.insertSelective(cfgApiDef);
    }

    /**
    * 批量维护API瀹氫箟琛�
    * @param cfgApiDefs
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgApiDef[] cfgApiDefs) throws  Exception{
        int result = 0;
        if(cfgApiDefs != null) {
            for (CfgApiDef cfgApiDef : cfgApiDefs) {
                if(cfgApiDef.getId() == null) {
                    result += this.create(cfgApiDef);
                }else {
                    result += this.update(cfgApiDef);
                }
            }
        }
        return result;
    }

    /**
    * 更新API瀹氫箟琛�
    * @param cfgApiDef
    * @return
    * @throws Exception
    */
    public int update(CfgApiDef cfgApiDef) throws  Exception {
        return cfgApiDefMapper.updateByPrimaryKeySelective(cfgApiDef);
    }

    /**
    * 通过查询对象更新API瀹氫箟琛�
    * @param cfgApiDef
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgApiDef cfgApiDef, CfgApiDef_Example example) throws  Exception {
        return cfgApiDefMapper.updateByExampleSelective(cfgApiDef, example);
    }

    /**
    * 删除API瀹氫箟琛�
    * @param cfgApiDef
    * @return
    * @throws Exception
    */
    public int delete(CfgApiDef cfgApiDef) throws  Exception {
        return cfgApiDefMapper.deleteByPrimaryKey(cfgApiDef.getId());
    }

    /**
    * 删除API瀹氫箟琛�
    * @param cfgApiDefId
    * @return
    * @throws Exception
    */
    public int delete(long cfgApiDefId) throws  Exception {
        return cfgApiDefMapper.deleteByPrimaryKey(cfgApiDefId);
    }

    /**
    * 查找所有API瀹氫箟琛�
    * @return
    */
    public List<CfgApiDef> getCfgApiDefAll()  throws  Exception {
        return cfgApiDefMapper.selectByExample(new CfgApiDef_Example());
    }

    /**
    * 通过API瀹氫箟琛�ID查询API瀹氫箟琛�
    * @param cfgApiDefId
    * @return
    * @throws Exception
    */
    public CfgApiDef getCfgApiDefByPK(long cfgApiDefId)  throws  Exception {
        return cfgApiDefMapper.selectByPrimaryKey(cfgApiDefId);
    }


    /**
    * 通过MAP参数查询API瀹氫箟琛�
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgApiDef> getCfgApiDefListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询API瀹氫箟琛�
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgApiDef> getCfgApiDefListByExample(CfgApiDef_Example example) throws  Exception {
        return cfgApiDefMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询API瀹氫箟琛�数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgApiDefCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询API瀹氫箟琛�数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgApiDefCountByExample(CfgApiDef_Example example) throws  Exception {
        return cfgApiDefMapper.countByExample(example);
    }


  	//getter
 	
	public CfgApiDefMapper getCfgApiDefMapper(){
		return cfgApiDefMapper;
	}
	//setter
	public void setCfgApiDefMapper(CfgApiDefMapper cfgApiDefMapper){
    	this.cfgApiDefMapper = cfgApiDefMapper;
    }
}
