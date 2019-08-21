package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.ThirdApiMapper;
import com.hframework.peacock.config.domain.model.ThirdApi;
import com.hframework.peacock.config.domain.model.ThirdApi_Example;
import com.hframework.peacock.config.service.interfaces.IThirdApiSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iThirdApiSV")
public class ThirdApiSVImpl  implements IThirdApiSV {

	@Resource
	private ThirdApiMapper thirdApiMapper;
  


    /**
    * 创建访问API
    * @param thirdApi
    * @return
    * @throws Exception
    */
    public int create(ThirdApi thirdApi) throws Exception {
        return thirdApiMapper.insertSelective(thirdApi);
    }

    /**
    * 批量维护访问API
    * @param thirdApis
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdApi[] thirdApis) throws  Exception{
        int result = 0;
        if(thirdApis != null) {
            for (ThirdApi thirdApi : thirdApis) {
                if(thirdApi.getId() == null) {
                    result += this.create(thirdApi);
                }else {
                    result += this.update(thirdApi);
                }
            }
        }
        return result;
    }

    /**
    * 更新访问API
    * @param thirdApi
    * @return
    * @throws Exception
    */
    public int update(ThirdApi thirdApi) throws  Exception {
        return thirdApiMapper.updateByPrimaryKeySelective(thirdApi);
    }

    /**
    * 通过查询对象更新访问API
    * @param thirdApi
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdApi thirdApi, ThirdApi_Example example) throws  Exception {
        return thirdApiMapper.updateByExampleSelective(thirdApi, example);
    }

    /**
    * 删除访问API
    * @param thirdApi
    * @return
    * @throws Exception
    */
    public int delete(ThirdApi thirdApi) throws  Exception {
        return thirdApiMapper.deleteByPrimaryKey(thirdApi.getId());
    }

    /**
    * 删除访问API
    * @param thirdApiId
    * @return
    * @throws Exception
    */
    public int delete(long thirdApiId) throws  Exception {
        return thirdApiMapper.deleteByPrimaryKey(thirdApiId);
    }

    /**
    * 查找所有访问API
    * @return
    */
    public List<ThirdApi> getThirdApiAll()  throws  Exception {
        return thirdApiMapper.selectByExample(new ThirdApi_Example());
    }

    /**
    * 通过访问APIID查询访问API
    * @param thirdApiId
    * @return
    * @throws Exception
    */
    public ThirdApi getThirdApiByPK(long thirdApiId)  throws  Exception {
        return thirdApiMapper.selectByPrimaryKey(thirdApiId);
    }


    /**
    * 通过MAP参数查询访问API
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdApi> getThirdApiListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询访问API
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdApi> getThirdApiListByExample(ThirdApi_Example example) throws  Exception {
        return thirdApiMapper.selectByExampleWithBLOBs(example);
    }

    /**
    * 通过MAP参数查询访问API数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdApiCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询访问API数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdApiCountByExample(ThirdApi_Example example) throws  Exception {
        return thirdApiMapper.countByExample(example);
    }


  	//getter
 	
	public ThirdApiMapper getThirdApiMapper(){
		return thirdApiMapper;
	}
	//setter
	public void setThirdApiMapper(ThirdApiMapper thirdApiMapper){
    	this.thirdApiMapper = thirdApiMapper;
    }
}
