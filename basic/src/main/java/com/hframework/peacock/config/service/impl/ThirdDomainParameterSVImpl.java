package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.ThirdDomainParameterMapper;
import com.hframework.peacock.config.domain.model.ThirdDomainParameter;
import com.hframework.peacock.config.domain.model.ThirdDomainParameter_Example;
import com.hframework.peacock.config.service.interfaces.IThirdDomainParameterSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iThirdDomainParameterSV")
public class ThirdDomainParameterSVImpl  implements IThirdDomainParameterSV {

	@Resource
	private ThirdDomainParameterMapper thirdDomainParameterMapper;
  


    /**
    * 创建访问域参数
    * @param thirdDomainParameter
    * @return
    * @throws Exception
    */
    public int create(ThirdDomainParameter thirdDomainParameter) throws Exception {
        return thirdDomainParameterMapper.insertSelective(thirdDomainParameter);
    }

    /**
    * 批量维护访问域参数
    * @param thirdDomainParameters
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdDomainParameter[] thirdDomainParameters) throws  Exception{
        int result = 0;
        if(thirdDomainParameters != null) {
            for (ThirdDomainParameter thirdDomainParameter : thirdDomainParameters) {
                if(thirdDomainParameter.getId() == null) {
                    result += this.create(thirdDomainParameter);
                }else {
                    result += this.update(thirdDomainParameter);
                }
            }
        }
        return result;
    }

    /**
    * 更新访问域参数
    * @param thirdDomainParameter
    * @return
    * @throws Exception
    */
    public int update(ThirdDomainParameter thirdDomainParameter) throws  Exception {
        return thirdDomainParameterMapper.updateByPrimaryKeySelective(thirdDomainParameter);
    }

    /**
    * 通过查询对象更新访问域参数
    * @param thirdDomainParameter
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdDomainParameter thirdDomainParameter, ThirdDomainParameter_Example example) throws  Exception {
        return thirdDomainParameterMapper.updateByExampleSelective(thirdDomainParameter, example);
    }

    /**
    * 删除访问域参数
    * @param thirdDomainParameter
    * @return
    * @throws Exception
    */
    public int delete(ThirdDomainParameter thirdDomainParameter) throws  Exception {
        return thirdDomainParameterMapper.deleteByPrimaryKey(thirdDomainParameter.getId());
    }

    /**
    * 删除访问域参数
    * @param thirdDomainParameterId
    * @return
    * @throws Exception
    */
    public int delete(long thirdDomainParameterId) throws  Exception {
        return thirdDomainParameterMapper.deleteByPrimaryKey(thirdDomainParameterId);
    }

    /**
    * 查找所有访问域参数
    * @return
    */
    public List<ThirdDomainParameter> getThirdDomainParameterAll()  throws  Exception {
        return thirdDomainParameterMapper.selectByExample(new ThirdDomainParameter_Example());
    }

    /**
    * 通过访问域参数ID查询访问域参数
    * @param thirdDomainParameterId
    * @return
    * @throws Exception
    */
    public ThirdDomainParameter getThirdDomainParameterByPK(long thirdDomainParameterId)  throws  Exception {
        return thirdDomainParameterMapper.selectByPrimaryKey(thirdDomainParameterId);
    }


    /**
    * 通过MAP参数查询访问域参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdDomainParameter> getThirdDomainParameterListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询访问域参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdDomainParameter> getThirdDomainParameterListByExample(ThirdDomainParameter_Example example) throws  Exception {
        return thirdDomainParameterMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询访问域参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdDomainParameterCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询访问域参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdDomainParameterCountByExample(ThirdDomainParameter_Example example) throws  Exception {
        return thirdDomainParameterMapper.countByExample(example);
    }


  	//getter
 	
	public ThirdDomainParameterMapper getThirdDomainParameterMapper(){
		return thirdDomainParameterMapper;
	}
	//setter
	public void setThirdDomainParameterMapper(ThirdDomainParameterMapper thirdDomainParameterMapper){
    	this.thirdDomainParameterMapper = thirdDomainParameterMapper;
    }
}
