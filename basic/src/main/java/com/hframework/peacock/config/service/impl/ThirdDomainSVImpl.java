package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.ThirdDomainMapper;
import com.hframework.peacock.config.domain.model.ThirdDomain;
import com.hframework.peacock.config.domain.model.ThirdDomain_Example;
import com.hframework.peacock.config.service.interfaces.IThirdDomainSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iThirdDomainSV")
public class ThirdDomainSVImpl  implements IThirdDomainSV {

	@Resource
	private ThirdDomainMapper thirdDomainMapper;
  


    /**
    * 创建访问域
    * @param thirdDomain
    * @return
    * @throws Exception
    */
    public int create(ThirdDomain thirdDomain) throws Exception {
        return thirdDomainMapper.insertSelective(thirdDomain);
    }

    /**
    * 批量维护访问域
    * @param thirdDomains
    * @return
    * @throws Exception
    */
    public int batchOperate(ThirdDomain[] thirdDomains) throws  Exception{
        int result = 0;
        if(thirdDomains != null) {
            for (ThirdDomain thirdDomain : thirdDomains) {
                if(thirdDomain.getId() == null) {
                    result += this.create(thirdDomain);
                }else {
                    result += this.update(thirdDomain);
                }
            }
        }
        return result;
    }

    /**
    * 更新访问域
    * @param thirdDomain
    * @return
    * @throws Exception
    */
    public int update(ThirdDomain thirdDomain) throws  Exception {
        return thirdDomainMapper.updateByPrimaryKeySelective(thirdDomain);
    }

    /**
    * 通过查询对象更新访问域
    * @param thirdDomain
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(ThirdDomain thirdDomain, ThirdDomain_Example example) throws  Exception {
        return thirdDomainMapper.updateByExampleSelective(thirdDomain, example);
    }

    /**
    * 删除访问域
    * @param thirdDomain
    * @return
    * @throws Exception
    */
    public int delete(ThirdDomain thirdDomain) throws  Exception {
        return thirdDomainMapper.deleteByPrimaryKey(thirdDomain.getId());
    }

    /**
    * 删除访问域
    * @param thirdDomainId
    * @return
    * @throws Exception
    */
    public int delete(long thirdDomainId) throws  Exception {
        return thirdDomainMapper.deleteByPrimaryKey(thirdDomainId);
    }

    /**
    * 查找所有访问域
    * @return
    */
    public List<ThirdDomain> getThirdDomainAll()  throws  Exception {
        return thirdDomainMapper.selectByExample(new ThirdDomain_Example());
    }

    /**
    * 通过访问域ID查询访问域
    * @param thirdDomainId
    * @return
    * @throws Exception
    */
    public ThirdDomain getThirdDomainByPK(long thirdDomainId)  throws  Exception {
        return thirdDomainMapper.selectByPrimaryKey(thirdDomainId);
    }


    /**
    * 通过MAP参数查询访问域
    * @param params
    * @return
    * @throws Exception
    */
    public List<ThirdDomain> getThirdDomainListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询访问域
    * @param example
    * @return
    * @throws Exception
    */
    public List<ThirdDomain> getThirdDomainListByExample(ThirdDomain_Example example) throws  Exception {
        return thirdDomainMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询访问域数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getThirdDomainCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询访问域数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getThirdDomainCountByExample(ThirdDomain_Example example) throws  Exception {
        return thirdDomainMapper.countByExample(example);
    }


  	//getter
 	
	public ThirdDomainMapper getThirdDomainMapper(){
		return thirdDomainMapper;
	}
	//setter
	public void setThirdDomainMapper(ThirdDomainMapper thirdDomainMapper){
    	this.thirdDomainMapper = thirdDomainMapper;
    }
}
