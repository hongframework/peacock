package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.ThirdCommonParameterMapper;
import com.hframework.peacock.config.domain.model.ThirdCommonParameter;
import com.hframework.peacock.config.domain.model.ThirdCommonParameter_Example;
import com.hframework.peacock.config.service.interfaces.IThirdCommonParameterSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iThirdCommonParameterSV")
public class ThirdCommonParameterSVImpl  implements IThirdCommonParameterSV {

	@Resource
	private ThirdCommonParameterMapper thirdCommonParameterMapper;
  


    /**
    * 创建域通用参数
    * @param thirdCommonParameter
    * @return
    * @throws Exception
    */
    public int create(ThirdCommonParameter thirdCommonParameter) throws Exception {
        return thirdCommonParameterMapper.insertSelective(thirdCommonParameter);
    }

    /**
    * 批量维护域通用参数
    * @param thirdCommonParameters
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdCommonParameter[] thirdCommonParameters) throws  Exception{
        int result = 0;
        if(thirdCommonParameters != null) {
            for (ThirdCommonParameter thirdCommonParameter : thirdCommonParameters) {
                if(thirdCommonParameter.getId() == null) {
                    result += this.create(thirdCommonParameter);
                }else {
                    result += this.update(thirdCommonParameter);
                }
            }
        }
        return result;
    }

    /**
    * 更新域通用参数
    * @param thirdCommonParameter
    * @return
    * @throws Exception
    */
    public int update(ThirdCommonParameter thirdCommonParameter) throws  Exception {
        return thirdCommonParameterMapper.updateByPrimaryKeySelective(thirdCommonParameter);
    }

    /**
    * 通过查询对象更新域通用参数
    * @param thirdCommonParameter
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdCommonParameter thirdCommonParameter, ThirdCommonParameter_Example example) throws  Exception {
        return thirdCommonParameterMapper.updateByExampleSelective(thirdCommonParameter, example);
    }

    /**
    * 删除域通用参数
    * @param thirdCommonParameter
    * @return
    * @throws Exception
    */
    public int delete(ThirdCommonParameter thirdCommonParameter) throws  Exception {
        return thirdCommonParameterMapper.deleteByPrimaryKey(thirdCommonParameter.getId());
    }

    /**
    * 删除域通用参数
    * @param thirdCommonParameterId
    * @return
    * @throws Exception
    */
    public int delete(long thirdCommonParameterId) throws  Exception {
        return thirdCommonParameterMapper.deleteByPrimaryKey(thirdCommonParameterId);
    }

    /**
    * 查找所有域通用参数
    * @return
    */
    public List<ThirdCommonParameter> getThirdCommonParameterAll()  throws  Exception {
        return thirdCommonParameterMapper.selectByExample(new ThirdCommonParameter_Example());
    }

    /**
    * 通过域通用参数ID查询域通用参数
    * @param thirdCommonParameterId
    * @return
    * @throws Exception
    */
    public ThirdCommonParameter getThirdCommonParameterByPK(long thirdCommonParameterId)  throws  Exception {
        return thirdCommonParameterMapper.selectByPrimaryKey(thirdCommonParameterId);
    }


    /**
    * 通过MAP参数查询域通用参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdCommonParameter> getThirdCommonParameterListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询域通用参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdCommonParameter> getThirdCommonParameterListByExample(ThirdCommonParameter_Example example) throws  Exception {
        return thirdCommonParameterMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询域通用参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdCommonParameterCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询域通用参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdCommonParameterCountByExample(ThirdCommonParameter_Example example) throws  Exception {
        return thirdCommonParameterMapper.countByExample(example);
    }


  	//getter
 	
	public ThirdCommonParameterMapper getThirdCommonParameterMapper(){
		return thirdCommonParameterMapper;
	}
	//setter
	public void setThirdCommonParameterMapper(ThirdCommonParameterMapper thirdCommonParameterMapper){
    	this.thirdCommonParameterMapper = thirdCommonParameterMapper;
    }
}
