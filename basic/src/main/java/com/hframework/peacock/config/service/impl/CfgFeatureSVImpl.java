package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgFeatureMapper;
import com.hframework.peacock.config.domain.model.CfgFeature;
import com.hframework.peacock.config.domain.model.CfgFeature_Example;
import com.hframework.peacock.config.service.interfaces.ICfgFeatureSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgFeatureSV")
public class CfgFeatureSVImpl  implements ICfgFeatureSV {

	@Resource
	private CfgFeatureMapper cfgFeatureMapper;
  


    /**
    * 创建鐗瑰緛
    * @param cfgFeature
    * @return
    * @throws Exception
    */
    public int create(CfgFeature cfgFeature) throws Exception {
        return cfgFeatureMapper.insertSelective(cfgFeature);
    }

    /**
    * 批量维护鐗瑰緛
    * @param cfgFeatures
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgFeature[] cfgFeatures) throws  Exception{
        int result = 0;
        if(cfgFeatures != null) {
            for (CfgFeature cfgFeature : cfgFeatures) {
                if(cfgFeature.getId() == null) {
                    result += this.create(cfgFeature);
                }else {
                    result += this.update(cfgFeature);
                }
            }
        }
        return result;
    }

    /**
    * 更新鐗瑰緛
    * @param cfgFeature
    * @return
    * @throws Exception
    */
    public int update(CfgFeature cfgFeature) throws  Exception {
        return cfgFeatureMapper.updateByPrimaryKeySelective(cfgFeature);
    }

    /**
    * 通过查询对象更新鐗瑰緛
    * @param cfgFeature
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgFeature cfgFeature, CfgFeature_Example example) throws  Exception {
        return cfgFeatureMapper.updateByExampleSelective(cfgFeature, example);
    }

    /**
    * 删除鐗瑰緛
    * @param cfgFeature
    * @return
    * @throws Exception
    */
    public int delete(CfgFeature cfgFeature) throws  Exception {
        return cfgFeatureMapper.deleteByPrimaryKey(cfgFeature.getId());
    }

    /**
    * 删除鐗瑰緛
    * @param cfgFeatureId
    * @return
    * @throws Exception
    */
    public int delete(Integer cfgFeatureId) throws  Exception {
        return cfgFeatureMapper.deleteByPrimaryKey(cfgFeatureId);
    }

    /**
    * 查找所有鐗瑰緛
    * @return
    */
    public List<CfgFeature> getCfgFeatureAll()  throws  Exception {
        return cfgFeatureMapper.selectByExample(new CfgFeature_Example());
    }

    /**
    * 通过鐗瑰緛ID查询鐗瑰緛
    * @param cfgFeatureId
    * @return
    * @throws Exception
    */
    public CfgFeature getCfgFeatureByPK(Integer cfgFeatureId)  throws  Exception {
        return cfgFeatureMapper.selectByPrimaryKey(cfgFeatureId);
    }


    /**
    * 通过MAP参数查询鐗瑰緛
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgFeature> getCfgFeatureListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询鐗瑰緛
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgFeature> getCfgFeatureListByExample(CfgFeature_Example example) throws  Exception {
        return cfgFeatureMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询鐗瑰緛数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgFeatureCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询鐗瑰緛数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgFeatureCountByExample(CfgFeature_Example example) throws  Exception {
        return cfgFeatureMapper.countByExample(example);
    }


  	//getter
 	
	public CfgFeatureMapper getCfgFeatureMapper(){
		return cfgFeatureMapper;
	}
	//setter
	public void setCfgFeatureMapper(CfgFeatureMapper cfgFeatureMapper){
    	this.cfgFeatureMapper = cfgFeatureMapper;
    }
}
