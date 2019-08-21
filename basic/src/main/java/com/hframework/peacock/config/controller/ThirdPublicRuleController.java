package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.ThirdPublicRule;
import com.hframework.peacock.config.domain.model.ThirdPublicRule_Example;
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
import com.hframework.peacock.config.service.interfaces.IThirdPublicRuleSV;

@Controller
@RequestMapping(value = "/config/thirdPublicRule")
public class ThirdPublicRuleController   {
    private static final Logger logger = LoggerFactory.getLogger(ThirdPublicRuleController.class);

	@Resource
	private IThirdPublicRuleSV iThirdPublicRuleSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示公共规则列表
     * @param thirdPublicRule
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("thirdPublicRule") ThirdPublicRule thirdPublicRule,
                           @ModelAttribute("example") ThirdPublicRule_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", thirdPublicRule, example, pagination);
        try{
            ExampleUtils.parseExample(thirdPublicRule, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< ThirdPublicRule> list = iThirdPublicRuleSV.getThirdPublicRuleListByExample(example);
            pagination.setTotalCount(iThirdPublicRuleSV.getThirdPublicRuleCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示公共规则明细
     * @param thirdPublicRule
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("thirdPublicRule") ThirdPublicRule thirdPublicRule){
        logger.debug("request : {},{}", thirdPublicRule.getId(), thirdPublicRule);
        try{
            ThirdPublicRule result = null;
            if(thirdPublicRule.getId() != null) {
                result = iThirdPublicRuleSV.getThirdPublicRuleByPK(thirdPublicRule.getId());
            }else {
                ThirdPublicRule_Example example = ExampleUtils.parseExample(thirdPublicRule, ThirdPublicRule_Example.class);
                List<ThirdPublicRule> list = iThirdPublicRuleSV.getThirdPublicRuleListByExample(example);
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
    * 搜索一个公共规则
    * @param  thirdPublicRule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" thirdPublicRule") ThirdPublicRule  thirdPublicRule){
        logger.debug("request : {}",  thirdPublicRule);
        try{
            ThirdPublicRule result = null;
            if(thirdPublicRule.getId() != null) {
                result =  iThirdPublicRuleSV.getThirdPublicRuleByPK(thirdPublicRule.getId());
            }else {
                ThirdPublicRule_Example example = ExampleUtils.parseExample( thirdPublicRule, ThirdPublicRule_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<ThirdPublicRule> list =  iThirdPublicRuleSV.getThirdPublicRuleListByExample(example);
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
    * 创建公共规则
    * @param thirdPublicRule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("thirdPublicRule") ThirdPublicRule thirdPublicRule) {
        logger.debug("request : {}", thirdPublicRule);
        try {
            ControllerHelper.setDefaultValue(thirdPublicRule, "id");
            int result = iThirdPublicRuleSV.create(thirdPublicRule);
            if(result > 0) {
                return ResultData.success(thirdPublicRule);
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
    * 批量维护公共规则
    * @param thirdPublicRules
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody ThirdPublicRule[] thirdPublicRules) {
        logger.debug("request : {}", thirdPublicRules);

        try {
            ControllerHelper.setDefaultValue(thirdPublicRules, "id");
            ControllerHelper.reorderProperty(thirdPublicRules);

            int result = iThirdPublicRuleSV.batchOperate(thirdPublicRules);
            if(result > 0) {
                return ResultData.success(thirdPublicRules);
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
    * 更新公共规则
    * @param thirdPublicRule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("thirdPublicRule") ThirdPublicRule thirdPublicRule) {
        logger.debug("request : {}", thirdPublicRule);
        try {
            ControllerHelper.setDefaultValue(thirdPublicRule, "id");
            int result = iThirdPublicRuleSV.update(thirdPublicRule);
            if(result > 0) {
                return ResultData.success(thirdPublicRule);
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
    * 创建或更新公共规则
    * @param thirdPublicRule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("thirdPublicRule") ThirdPublicRule thirdPublicRule) {
        logger.debug("request : {}", thirdPublicRule);
        try {
            ControllerHelper.setDefaultValue(thirdPublicRule, "id");
            int result = iThirdPublicRuleSV.batchOperate(new ThirdPublicRule[]{thirdPublicRule});
            if(result > 0) {
                return ResultData.success(thirdPublicRule);
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
    * 删除公共规则
    * @param thirdPublicRule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("thirdPublicRule") ThirdPublicRule thirdPublicRule) {
        logger.debug("request : {}", thirdPublicRule);

        try {
            ControllerHelper.setDefaultValue(thirdPublicRule, "id");
            int result = iThirdPublicRuleSV.delete(thirdPublicRule);
            if(result > 0) {
                return ResultData.success(thirdPublicRule);
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
 	
	public IThirdPublicRuleSV getIThirdPublicRuleSV(){
		return iThirdPublicRuleSV;
	}
	//setter
	public void setIThirdPublicRuleSV(IThirdPublicRuleSV iThirdPublicRuleSV){
    	this.iThirdPublicRuleSV = iThirdPublicRuleSV;
    }
}
