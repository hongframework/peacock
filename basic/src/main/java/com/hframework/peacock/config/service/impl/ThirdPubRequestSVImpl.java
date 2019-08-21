package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.ThirdPubRequestMapper;
import com.hframework.peacock.config.domain.model.ThirdPubRequest;
import com.hframework.peacock.config.domain.model.ThirdPubRequest_Example;
import com.hframework.peacock.config.service.interfaces.IThirdPubRequestSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iThirdPubRequestSV")
public class ThirdPubRequestSVImpl  implements IThirdPubRequestSV {

	@Resource
	private ThirdPubRequestMapper thirdPubRequestMapper;
  


    /**
    * 创建公共请求参数
    * @param thirdPubRequest
    * @return
    * @throws Exception
    */
    public int create(ThirdPubRequest thirdPubRequest) throws Exception {
        return thirdPubRequestMapper.insertSelective(thirdPubRequest);
    }

    /**
    * 批量维护公共请求参数
    * @param thirdPubRequests
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdPubRequest[] thirdPubRequests) throws  Exception{
        int result = 0;
        if(thirdPubRequests != null) {
            for (ThirdPubRequest thirdPubRequest : thirdPubRequests) {
                if(thirdPubRequest.getId() == null) {
                    result += this.create(thirdPubRequest);
                }else {
                    result += this.update(thirdPubRequest);
                }
            }
        }
        return result;
    }

    /**
    * 更新公共请求参数
    * @param thirdPubRequest
    * @return
    * @throws Exception
    */
    public int update(ThirdPubRequest thirdPubRequest) throws  Exception {
        return thirdPubRequestMapper.updateByPrimaryKeySelective(thirdPubRequest);
    }

    /**
    * 通过查询对象更新公共请求参数
    * @param thirdPubRequest
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdPubRequest thirdPubRequest, ThirdPubRequest_Example example) throws  Exception {
        return thirdPubRequestMapper.updateByExampleSelective(thirdPubRequest, example);
    }

    /**
    * 删除公共请求参数
    * @param thirdPubRequest
    * @return
    * @throws Exception
    */
    public int delete(ThirdPubRequest thirdPubRequest) throws  Exception {
        return thirdPubRequestMapper.deleteByPrimaryKey(thirdPubRequest.getId());
    }

    /**
    * 删除公共请求参数
    * @param thirdPubRequestId
    * @return
    * @throws Exception
    */
    public int delete(long thirdPubRequestId) throws  Exception {
        return thirdPubRequestMapper.deleteByPrimaryKey(thirdPubRequestId);
    }

    /**
    * 查找所有公共请求参数
    * @return
    */
    public List<ThirdPubRequest> getThirdPubRequestAll()  throws  Exception {
        return thirdPubRequestMapper.selectByExample(new ThirdPubRequest_Example());
    }

    /**
    * 通过公共请求参数ID查询公共请求参数
    * @param thirdPubRequestId
    * @return
    * @throws Exception
    */
    public ThirdPubRequest getThirdPubRequestByPK(long thirdPubRequestId)  throws  Exception {
        return thirdPubRequestMapper.selectByPrimaryKey(thirdPubRequestId);
    }


    /**
    * 通过MAP参数查询公共请求参数
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdPubRequest> getThirdPubRequestListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询公共请求参数
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdPubRequest> getThirdPubRequestListByExample(ThirdPubRequest_Example example) throws  Exception {
        return thirdPubRequestMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询公共请求参数数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdPubRequestCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询公共请求参数数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdPubRequestCountByExample(ThirdPubRequest_Example example) throws  Exception {
        return thirdPubRequestMapper.countByExample(example);
    }


  	//getter
 	
	public ThirdPubRequestMapper getThirdPubRequestMapper(){
		return thirdPubRequestMapper;
	}
	//setter
	public void setThirdPubRequestMapper(ThirdPubRequestMapper thirdPubRequestMapper){
    	this.thirdPubRequestMapper = thirdPubRequestMapper;
    }
}
