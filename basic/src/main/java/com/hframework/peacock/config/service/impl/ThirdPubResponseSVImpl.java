package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.ThirdPubResponseMapper;
import com.hframework.peacock.config.domain.model.ThirdPubResponse;
import com.hframework.peacock.config.domain.model.ThirdPubResponse_Example;
import com.hframework.peacock.config.service.interfaces.IThirdPubResponseSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iThirdPubResponseSV")
public class ThirdPubResponseSVImpl  implements IThirdPubResponseSV {

	@Resource
	private ThirdPubResponseMapper thirdPubResponseMapper;
  


    /**
    * 创建公共响应参数
    * @param thirdPubResponse
    * @return
    * @throws Exception
    */
    public int create(ThirdPubResponse thirdPubResponse) throws Exception {
        return thirdPubResponseMapper.insertSelective(thirdPubResponse);
    }

    /**
    * 批量维护公共响应参数
    * @param thirdPubResponses
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdPubResponse[] thirdPubResponses) throws  Exception{
        int result = 0;
        if(thirdPubResponses != null) {
            for (ThirdPubResponse thirdPubResponse : thirdPubResponses) {
                if(thirdPubResponse.getId() == null) {
                    result += this.create(thirdPubResponse);
                }else {
                    result += this.update(thirdPubResponse);
                }
            }
        }
        return result;
    }

    /**
    * 更新公共响应参数
    * @param thirdPubResponse
    * @return
    * @throws Exception
    */
    public int update(ThirdPubResponse thirdPubResponse) throws  Exception {
        return thirdPubResponseMapper.updateByPrimaryKeySelective(thirdPubResponse);
    }

    /**
    * 通过查询对象更新公共响应参数
    * @param thirdPubResponse
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdPubResponse thirdPubResponse, ThirdPubResponse_Example example) throws  Exception {
        return thirdPubResponseMapper.updateByExampleSelective(thirdPubResponse, example);
    }

    /**
    * 删除公共响应参数
    * @param thirdPubResponse
    * @return
    * @throws Exception
    */
    public int delete(ThirdPubResponse thirdPubResponse) throws  Exception {
        return thirdPubResponseMapper.deleteByPrimaryKey(thirdPubResponse.getId());
    }

    /**
    * 删除公共响应参数
    * @param thirdPubResponseId
    * @return
    * @throws Exception
    */
    public int delete(long thirdPubResponseId) throws  Exception {
        return thirdPubResponseMapper.deleteByPrimaryKey(thirdPubResponseId);
    }

    /**
    * 查找所有公共响应参数
    * @return
    */
    public List<ThirdPubResponse> getThirdPubResponseAll()  throws  Exception {
        return thirdPubResponseMapper.selectByExample(new ThirdPubResponse_Example());
    }

    /**
    * 通过公共响应参数ID查询公共响应参数
    * @param thirdPubResponseId
    * @return
    * @throws Exception
    */
    public ThirdPubResponse getThirdPubResponseByPK(long thirdPubResponseId)  throws  Exception {
        return thirdPubResponseMapper.selectByPrimaryKey(thirdPubResponseId);
    }


    /**
    * 通过MAP参数查询公共响应参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdPubResponse> getThirdPubResponseListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询公共响应参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdPubResponse> getThirdPubResponseListByExample(ThirdPubResponse_Example example) throws  Exception {
        return thirdPubResponseMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询公共响应参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdPubResponseCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询公共响应参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdPubResponseCountByExample(ThirdPubResponse_Example example) throws  Exception {
        return thirdPubResponseMapper.countByExample(example);
    }


  	//getter
 	
	public ThirdPubResponseMapper getThirdPubResponseMapper(){
		return thirdPubResponseMapper;
	}
	//setter
	public void setThirdPubResponseMapper(ThirdPubResponseMapper thirdPubResponseMapper){
    	this.thirdPubResponseMapper = thirdPubResponseMapper;
    }
}
