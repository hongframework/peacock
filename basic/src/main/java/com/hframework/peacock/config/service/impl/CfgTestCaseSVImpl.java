package com.hframework.peacock.config.service.impl;

import java.util.*;

import com.hframework.peacock.config.dao.CfgTestCaseMapper;
import com.hframework.peacock.config.domain.model.CfgTestCase;
import com.hframework.peacock.config.domain.model.CfgTestCase_Example;
import com.hframework.peacock.config.service.interfaces.ICfgTestCaseSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("iCfgTestCaseSV")
public class CfgTestCaseSVImpl  implements ICfgTestCaseSV {

	@Resource
	private CfgTestCaseMapper cfgTestCaseMapper;
  


    /**
    * 创建测试CASE
    * @param cfgTestCase
    * @return
    * @throws Exception
    */
    public int create(CfgTestCase cfgTestCase) throws Exception {
        return cfgTestCaseMapper.insertSelective(cfgTestCase);
    }

    /**
    * 批量维护测试CASE
    * @param cfgTestCases
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgTestCase[] cfgTestCases) throws  Exception{
        int result = 0;
        if(cfgTestCases != null) {
            for (CfgTestCase cfgTestCase : cfgTestCases) {
                if(cfgTestCase.getId() == null) {
                    result += this.create(cfgTestCase);
                }else {
                    result += this.update(cfgTestCase);
                }
            }
        }
        return result;
    }

    /**
    * 更新测试CASE
    * @param cfgTestCase
    * @return
    * @throws Exception
    */
    public int update(CfgTestCase cfgTestCase) throws  Exception {
        return cfgTestCaseMapper.updateByPrimaryKeySelective(cfgTestCase);
    }

    /**
    * 通过查询对象更新测试CASE
    * @param cfgTestCase
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgTestCase cfgTestCase, CfgTestCase_Example example) throws  Exception {
        return cfgTestCaseMapper.updateByExampleSelective(cfgTestCase, example);
    }

    /**
    * 删除测试CASE
    * @param cfgTestCase
    * @return
    * @throws Exception
    */
    public int delete(CfgTestCase cfgTestCase) throws  Exception {
        return cfgTestCaseMapper.deleteByPrimaryKey(cfgTestCase.getId());
    }

    /**
    * 删除测试CASE
    * @param cfgTestCaseId
    * @return
    * @throws Exception
    */
    public int delete(long cfgTestCaseId) throws  Exception {
        return cfgTestCaseMapper.deleteByPrimaryKey(cfgTestCaseId);
    }

    /**
    * 查找所有测试CASE
    * @return
    */
    public List<CfgTestCase> getCfgTestCaseAll()  throws  Exception {
        return cfgTestCaseMapper.selectByExample(new CfgTestCase_Example());
    }

    /**
    * 通过测试CASEID查询测试CASE
    * @param cfgTestCaseId
    * @return
    * @throws Exception
    */
    public CfgTestCase getCfgTestCaseByPK(long cfgTestCaseId)  throws  Exception {
        return cfgTestCaseMapper.selectByPrimaryKey(cfgTestCaseId);
    }


    /**
    * 通过MAP参数查询测试CASE
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgTestCase> getCfgTestCaseListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询测试CASE
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgTestCase> getCfgTestCaseListByExample(CfgTestCase_Example example) throws  Exception {
        return cfgTestCaseMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询测试CASE数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgTestCaseCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询测试CASE数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgTestCaseCountByExample(CfgTestCase_Example example) throws  Exception {
        return cfgTestCaseMapper.countByExample(example);
    }


  	//getter
 	
	public CfgTestCaseMapper getCfgTestCaseMapper(){
		return cfgTestCaseMapper;
	}
	//setter
	public void setCfgTestCaseMapper(CfgTestCaseMapper cfgTestCaseMapper){
    	this.cfgTestCaseMapper = cfgTestCaseMapper;
    }
}
