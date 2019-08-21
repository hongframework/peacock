package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import com.hframework.web.ControllerHelper;
import com.hframework.peacock.config.domain.model.CfgTestCase;
import com.hframework.peacock.config.domain.model.CfgTestCase_Example;
import com.hframework.peacock.config.service.interfaces.ICfgTestCaseSV;

@Controller
@RequestMapping(value = "/config/cfgTestCase")
public class CfgTestCaseController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgTestCaseController.class);

	@Resource
	private ICfgTestCaseSV iCfgTestCaseSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示测试CASE列表
     * @param cfgTestCase
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgTestCase") CfgTestCase cfgTestCase,
                                      @ModelAttribute("example") CfgTestCase_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgTestCase, example, pagination);
        try{
            ExampleUtils.parseExample(cfgTestCase, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgTestCase> list = iCfgTestCaseSV.getCfgTestCaseListByExample(example);
            pagination.setTotalCount(iCfgTestCaseSV.getCfgTestCaseCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示测试CASE明细
     * @param cfgTestCase
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgTestCase") CfgTestCase cfgTestCase){
        logger.debug("request : {},{}", cfgTestCase.getId(), cfgTestCase);
        try{
            CfgTestCase result = null;
            if(cfgTestCase.getId() != null) {
                result = iCfgTestCaseSV.getCfgTestCaseByPK(cfgTestCase.getId());
            }else {
                CfgTestCase_Example example = ExampleUtils.parseExample(cfgTestCase, CfgTestCase_Example.class);
                List<CfgTestCase> list = iCfgTestCaseSV.getCfgTestCaseListByExample(example);
                if(list != null && list.size() == 1) {
                    result = list.get(0);
                }
            }

            if(result != null) {
                return ResultData.success(result);
            }else {
                return ResultData.error(ResultCode.RECODE_IS_NOT_EXISTS);
            }
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }

    /**
    * 搜索一个测试CASE
    * @param  cfgTestCase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgTestCase") CfgTestCase  cfgTestCase){
        logger.debug("request : {}",  cfgTestCase);
        try{
            CfgTestCase result = null;
            if(cfgTestCase.getId() != null) {
                result =  iCfgTestCaseSV.getCfgTestCaseByPK(cfgTestCase.getId());
            }else {
                CfgTestCase_Example example = ExampleUtils.parseExample( cfgTestCase, CfgTestCase_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgTestCase> list =  iCfgTestCaseSV.getCfgTestCaseListByExample(example);
                if(list != null && list.size() > 0) {
                    result = list.get(0);
                }
            }

            if(result != null) {
                return ResultData.success(result);
            }else {
                return ResultData.error(ResultCode.RECODE_IS_NOT_EXISTS);
            }
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }

    /**
    * 创建测试CASE
    * @param cfgTestCase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgTestCase") CfgTestCase cfgTestCase) {
        logger.debug("request : {}", cfgTestCase);
        try {
            ControllerHelper.setDefaultValue(cfgTestCase, "id");
            int result = iCfgTestCaseSV.create(cfgTestCase);
            if(result > 0) {
                return ResultData.success(cfgTestCase);
            }
        } catch (BusinessException e ){
            return e.result();
        } catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
        return ResultData.error(ResultCode.UNKNOW);
    }

    /**
    * 批量维护测试CASE
    * @param cfgTestCases
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgTestCase[] cfgTestCases) {
        logger.debug("request : {}", cfgTestCases);

        try {
            ControllerHelper.setDefaultValue(cfgTestCases, "id");
            ControllerHelper.reorderProperty(cfgTestCases);

            int result = iCfgTestCaseSV.batchOperate(cfgTestCases);
            if(result > 0) {
                return ResultData.success(cfgTestCases);
            }
        } catch (BusinessException e ){
            return e.result();
        } catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
        return ResultData.error(ResultCode.UNKNOW);
    }

    /**
    * 更新测试CASE
    * @param cfgTestCase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgTestCase") CfgTestCase cfgTestCase) {
        logger.debug("request : {}", cfgTestCase);
        try {
            ControllerHelper.setDefaultValue(cfgTestCase, "id");
            int result = iCfgTestCaseSV.update(cfgTestCase);
            if(result > 0) {
                return ResultData.success(cfgTestCase);
            }
        } catch (BusinessException e ){
            return e.result();
        } catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
        return ResultData.error(ResultCode.UNKNOW);
    }

    /**
    * 创建或更新测试CASE
    * @param cfgTestCase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgTestCase") CfgTestCase cfgTestCase) {
        logger.debug("request : {}", cfgTestCase);
        try {
            ControllerHelper.setDefaultValue(cfgTestCase, "id");
            int result = iCfgTestCaseSV.batchOperate(new CfgTestCase[]{cfgTestCase});
            if(result > 0) {
                return ResultData.success(cfgTestCase);
            }
        } catch (BusinessException e ){
            return e.result();
        } catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
        return ResultData.error(ResultCode.UNKNOW);
    }

    /**
    * 删除测试CASE
    * @param cfgTestCase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgTestCase") CfgTestCase cfgTestCase) {
        logger.debug("request : {}", cfgTestCase);

        try {
            ControllerHelper.setDefaultValue(cfgTestCase, "id");
            int result = iCfgTestCaseSV.delete(cfgTestCase);
            if(result > 0) {
                return ResultData.success(cfgTestCase);
            }else {
                return ResultData.error(ResultCode.RECODE_IS_NOT_EXISTS);
            }
        } catch (BusinessException e ){
            return e.result();
        } catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }
  	//getter
 	
	public ICfgTestCaseSV getICfgTestCaseSV(){
		return iCfgTestCaseSV;
	}
	//setter
	public void setICfgTestCaseSV(ICfgTestCaseSV iCfgTestCaseSV){
    	this.iCfgTestCaseSV = iCfgTestCaseSV;
    }
}
