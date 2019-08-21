package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgRuntimeRule;
import com.hframework.peacock.config.domain.model.CfgRuntimeRule_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeRuleSV;

@Controller
@RequestMapping(value = "/config/cfgRuntimeRule")
public class CfgRuntimeRuleController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgRuntimeRuleController.class);

	@Resource
	private ICfgRuntimeRuleSV iCfgRuntimeRuleSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示鍔ㄦ�佽鍒�列表
     * @param cfgRuntimeRule
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgRuntimeRule") CfgRuntimeRule cfgRuntimeRule,
                           @ModelAttribute("example") CfgRuntimeRule_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgRuntimeRule, example, pagination);
        try{
            ExampleUtils.parseExample(cfgRuntimeRule, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgRuntimeRule> list = iCfgRuntimeRuleSV.getCfgRuntimeRuleListByExample(example);
            pagination.setTotalCount(iCfgRuntimeRuleSV.getCfgRuntimeRuleCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示鍔ㄦ�佽鍒�明细
     * @param cfgRuntimeRule
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgRuntimeRule") CfgRuntimeRule cfgRuntimeRule){
        logger.debug("request : {},{}", cfgRuntimeRule.getId(), cfgRuntimeRule);
        try{
            CfgRuntimeRule result = null;
            if(cfgRuntimeRule.getId() != null) {
                result = iCfgRuntimeRuleSV.getCfgRuntimeRuleByPK(cfgRuntimeRule.getId());
            }else {
                CfgRuntimeRule_Example example = ExampleUtils.parseExample(cfgRuntimeRule, CfgRuntimeRule_Example.class);
                List<CfgRuntimeRule> list = iCfgRuntimeRuleSV.getCfgRuntimeRuleListByExample(example);
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
    * 搜索一个鍔ㄦ�佽鍒�
    * @param  cfgRuntimeRule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgRuntimeRule") CfgRuntimeRule  cfgRuntimeRule){
        logger.debug("request : {}",  cfgRuntimeRule);
        try{
            CfgRuntimeRule result = null;
            if(cfgRuntimeRule.getId() != null) {
                result =  iCfgRuntimeRuleSV.getCfgRuntimeRuleByPK(cfgRuntimeRule.getId());
            }else {
                CfgRuntimeRule_Example example = ExampleUtils.parseExample( cfgRuntimeRule, CfgRuntimeRule_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgRuntimeRule> list =  iCfgRuntimeRuleSV.getCfgRuntimeRuleListByExample(example);
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
    * 创建鍔ㄦ�佽鍒�
    * @param cfgRuntimeRule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgRuntimeRule") CfgRuntimeRule cfgRuntimeRule) {
        logger.debug("request : {}", cfgRuntimeRule);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeRule, "cfgRuntimeRuleId");
            int result = iCfgRuntimeRuleSV.create(cfgRuntimeRule);
            if(result > 0) {
                return ResultData.success(cfgRuntimeRule);
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
    * 批量维护鍔ㄦ�佽鍒�
    * @param cfgRuntimeRules
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgRuntimeRule[] cfgRuntimeRules) {
        logger.debug("request : {}", cfgRuntimeRules);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeRules, "cfgRuntimeRuleId");
            ControllerHelper.reorderProperty(cfgRuntimeRules);

            int result = iCfgRuntimeRuleSV.batchOperate(cfgRuntimeRules);
            if(result > 0) {
                return ResultData.success(cfgRuntimeRules);
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
    * 更新鍔ㄦ�佽鍒�
    * @param cfgRuntimeRule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgRuntimeRule") CfgRuntimeRule cfgRuntimeRule) {
        logger.debug("request : {}", cfgRuntimeRule);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeRule, "cfgRuntimeRuleId");
            int result = iCfgRuntimeRuleSV.update(cfgRuntimeRule);
            if(result > 0) {
                return ResultData.success(cfgRuntimeRule);
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
    * 创建或更新鍔ㄦ�佽鍒�
    * @param cfgRuntimeRule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgRuntimeRule") CfgRuntimeRule cfgRuntimeRule) {
        logger.debug("request : {}", cfgRuntimeRule);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeRule, "cfgRuntimeRuleId");
            int result = iCfgRuntimeRuleSV.batchOperate(new CfgRuntimeRule[]{cfgRuntimeRule});
            if(result > 0) {
                return ResultData.success(cfgRuntimeRule);
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
    * 删除鍔ㄦ�佽鍒�
    * @param cfgRuntimeRule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgRuntimeRule") CfgRuntimeRule cfgRuntimeRule) {
        logger.debug("request : {}", cfgRuntimeRule);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeRule, "cfgRuntimeRuleId");
            int result = iCfgRuntimeRuleSV.delete(cfgRuntimeRule);
            if(result > 0) {
                return ResultData.success(cfgRuntimeRule);
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
 	
	public ICfgRuntimeRuleSV getICfgRuntimeRuleSV(){
		return iCfgRuntimeRuleSV;
	}
	//setter
	public void setICfgRuntimeRuleSV(ICfgRuntimeRuleSV iCfgRuntimeRuleSV){
    	this.iCfgRuntimeRuleSV = iCfgRuntimeRuleSV;
    }
}
