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
import com.hframework.peacock.config.domain.model.CfgRuntimeParameter;
import com.hframework.peacock.config.domain.model.CfgRuntimeParameter_Example;
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeParameterSV;

@Controller
@RequestMapping(value = "/config/cfgRuntimeParameter")
public class CfgRuntimeParameterController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgRuntimeParameterController.class);

	@Resource
	private ICfgRuntimeParameterSV iCfgRuntimeParameterSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示参数列表
     * @param cfgRuntimeParameter
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgRuntimeParameter") CfgRuntimeParameter cfgRuntimeParameter,
                                      @ModelAttribute("example") CfgRuntimeParameter_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgRuntimeParameter, example, pagination);
        try{
            ExampleUtils.parseExample(cfgRuntimeParameter, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgRuntimeParameter> list = iCfgRuntimeParameterSV.getCfgRuntimeParameterListByExample(example);
            pagination.setTotalCount(iCfgRuntimeParameterSV.getCfgRuntimeParameterCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示参数明细
     * @param cfgRuntimeParameter
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgRuntimeParameter") CfgRuntimeParameter cfgRuntimeParameter){
        logger.debug("request : {},{}", cfgRuntimeParameter.getId(), cfgRuntimeParameter);
        try{
            CfgRuntimeParameter result = null;
            if(cfgRuntimeParameter.getId() != null) {
                result = iCfgRuntimeParameterSV.getCfgRuntimeParameterByPK(cfgRuntimeParameter.getId());
            }else {
                CfgRuntimeParameter_Example example = ExampleUtils.parseExample(cfgRuntimeParameter, CfgRuntimeParameter_Example.class);
                List<CfgRuntimeParameter> list = iCfgRuntimeParameterSV.getCfgRuntimeParameterListByExample(example);
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
    * 搜索一个参数
    * @param  cfgRuntimeParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgRuntimeParameter") CfgRuntimeParameter  cfgRuntimeParameter){
        logger.debug("request : {}",  cfgRuntimeParameter);
        try{
            CfgRuntimeParameter result = null;
            if(cfgRuntimeParameter.getId() != null) {
                result =  iCfgRuntimeParameterSV.getCfgRuntimeParameterByPK(cfgRuntimeParameter.getId());
            }else {
                CfgRuntimeParameter_Example example = ExampleUtils.parseExample( cfgRuntimeParameter, CfgRuntimeParameter_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgRuntimeParameter> list =  iCfgRuntimeParameterSV.getCfgRuntimeParameterListByExample(example);
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
    * 创建参数
    * @param cfgRuntimeParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgRuntimeParameter") CfgRuntimeParameter cfgRuntimeParameter) {
        logger.debug("request : {}", cfgRuntimeParameter);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeParameter, "id");
            int result = iCfgRuntimeParameterSV.create(cfgRuntimeParameter);
            if(result > 0) {
                return ResultData.success(cfgRuntimeParameter);
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
    * 批量维护参数
    * @param cfgRuntimeParameters
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgRuntimeParameter[] cfgRuntimeParameters) {
        logger.debug("request : {}", cfgRuntimeParameters);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeParameters, "id");
            ControllerHelper.reorderProperty(cfgRuntimeParameters);

            int result = iCfgRuntimeParameterSV.batchOperate(cfgRuntimeParameters);
            if(result > 0) {
                return ResultData.success(cfgRuntimeParameters);
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
    * 更新参数
    * @param cfgRuntimeParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgRuntimeParameter") CfgRuntimeParameter cfgRuntimeParameter) {
        logger.debug("request : {}", cfgRuntimeParameter);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeParameter, "id");
            int result = iCfgRuntimeParameterSV.update(cfgRuntimeParameter);
            if(result > 0) {
                return ResultData.success(cfgRuntimeParameter);
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
    * 创建或更新参数
    * @param cfgRuntimeParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgRuntimeParameter") CfgRuntimeParameter cfgRuntimeParameter) {
        logger.debug("request : {}", cfgRuntimeParameter);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeParameter, "id");
            int result = iCfgRuntimeParameterSV.batchOperate(new CfgRuntimeParameter[]{cfgRuntimeParameter});
            if(result > 0) {
                return ResultData.success(cfgRuntimeParameter);
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
    * 删除参数
    * @param cfgRuntimeParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgRuntimeParameter") CfgRuntimeParameter cfgRuntimeParameter) {
        logger.debug("request : {}", cfgRuntimeParameter);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeParameter, "id");
            int result = iCfgRuntimeParameterSV.delete(cfgRuntimeParameter);
            if(result > 0) {
                return ResultData.success(cfgRuntimeParameter);
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
 	
	public ICfgRuntimeParameterSV getICfgRuntimeParameterSV(){
		return iCfgRuntimeParameterSV;
	}
	//setter
	public void setICfgRuntimeParameterSV(ICfgRuntimeParameterSV iCfgRuntimeParameterSV){
    	this.iCfgRuntimeParameterSV = iCfgRuntimeParameterSV;
    }
}
