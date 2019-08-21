package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.ThirdHelpParameterMapper;
import com.hframework.peacock.config.domain.model.ThirdHelpParameter;
import com.hframework.peacock.config.domain.model.ThirdHelpParameter_Example;
import com.hframework.peacock.config.service.interfaces.IThirdHelpParameterSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iThirdHelpParameterSV")
public class ThirdHelpParameterSVImpl  implements IThirdHelpParameterSV {

	@Resource
	private ThirdHelpParameterMapper thirdHelpParameterMapper;
  


    /**
    * 创建访问域常规参数
    * @param thirdHelpParameter
    * @return
    * @throws Exception
    */
    public int create(ThirdHelpParameter thirdHelpParameter) throws Exception {
        return thirdHelpParameterMapper.insertSelective(thirdHelpParameter);
    }

    /**
    * 批量维护访问域常规参数
    * @param thirdHelpParameters
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdHelpParameter[] thirdHelpParameters) throws  Exception{
        int result = 0;
        if(thirdHelpParameters != null) {
            for (ThirdHelpParameter thirdHelpParameter : thirdHelpParameters) {
                if(thirdHelpParameter.getId() == null) {
                    result += this.create(thirdHelpParameter);
                }else {
                    result += this.update(thirdHelpParameter);
                }
            }
        }
        return result;
    }

    /**
    * 更新访问域常规参数
    * @param thirdHelpParameter
    * @return
    * @throws Exception
    */
    public int update(ThirdHelpParameter thirdHelpParameter) throws  Exception {
        return thirdHelpParameterMapper.updateByPrimaryKeySelective(thirdHelpParameter);
    }

    /**
    * 通过查询对象更新访问域常规参数
    * @param thirdHelpParameter
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdHelpParameter thirdHelpParameter, ThirdHelpParameter_Example example) throws  Exception {
        return thirdHelpParameterMapper.updateByExampleSelective(thirdHelpParameter, example);
    }

    /**
    * 删除访问域常规参数
    * @param thirdHelpParameter
    * @return
    * @throws Exception
    */
    public int delete(ThirdHelpParameter thirdHelpParameter) throws  Exception {
        return thirdHelpParameterMapper.deleteByPrimaryKey(thirdHelpParameter.getId());
    }

    /**
    * 删除访问域常规参数
    * @param thirdHelpParameterId
    * @return
    * @throws Exception
    */
    public int delete(long thirdHelpParameterId) throws  Exception {
        return thirdHelpParameterMapper.deleteByPrimaryKey(thirdHelpParameterId);
    }

    /**
    * 查找所有访问域常规参数
    * @return
    */
    public List<ThirdHelpParameter> getThirdHelpParameterAll()  throws  Exception {
        return thirdHelpParameterMapper.selectByExample(new ThirdHelpParameter_Example());
    }

    /**
    * 通过访问域常规参数ID查询访问域常规参数
    * @param thirdHelpParameterId
    * @return
    * @throws Exception
    */
    public ThirdHelpParameter getThirdHelpParameterByPK(long thirdHelpParameterId)  throws  Exception {
        return thirdHelpParameterMapper.selectByPrimaryKey(thirdHelpParameterId);
    }


    /**
    * 通过MAP参数查询访问域常规参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdHelpParameter> getThirdHelpParameterListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询访问域常规参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdHelpParameter> getThirdHelpParameterListByExample(ThirdHelpParameter_Example example) throws  Exception {
        return thirdHelpParameterMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询访问域常规参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdHelpParameterCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询访问域常规参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdHelpParameterCountByExample(ThirdHelpParameter_Example example) throws  Exception {
        return thirdHelpParameterMapper.countByExample(example);
    }


  	//getter
 	
	public ThirdHelpParameterMapper getThirdHelpParameterMapper(){
		return thirdHelpParameterMapper;
	}
	//setter
	public void setThirdHelpParameterMapper(ThirdHelpParameterMapper thirdHelpParameterMapper){
    	this.thirdHelpParameterMapper = thirdHelpParameterMapper;
    }
}
