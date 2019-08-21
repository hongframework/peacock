package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.service.interfaces.ICfgStaticExpanderParameterSV;
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
import com.hframework.peacock.config.domain.model.CfgStaticExpanderParameter;
import com.hframework.peacock.config.domain.model.CfgStaticExpanderParameter_Example;

@Controller
@RequestMapping(value = "/config/cfgStaticExpanderParameter")
public class CfgStaticExpanderParameterController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgStaticExpanderParameterController.class);

	@Resource
	private ICfgStaticExpanderParameterSV iCfgStaticExpanderParameterSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示静态扩展器参数列表
     * @param cfgStaticExpanderParameter
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgStaticExpanderParameter") CfgStaticExpanderParameter cfgStaticExpanderParameter,
                                      @ModelAttribute("example") CfgStaticExpanderParameter_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgStaticExpanderParameter, example, pagination);
        try{
            ExampleUtils.parseExample(cfgStaticExpanderParameter, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgStaticExpanderParameter> list = iCfgStaticExpanderParameterSV.getCfgStaticExpanderParameterListByExample(example);
            pagination.setTotalCount(iCfgStaticExpanderParameterSV.getCfgStaticExpanderParameterCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示静态扩展器参数明细
     * @param cfgStaticExpanderParameter
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgStaticExpanderParameter") CfgStaticExpanderParameter cfgStaticExpanderParameter){
        logger.debug("request : {},{}", cfgStaticExpanderParameter.getId(), cfgStaticExpanderParameter);
        try{
            CfgStaticExpanderParameter result = null;
            if(cfgStaticExpanderParameter.getId() != null) {
                result = iCfgStaticExpanderParameterSV.getCfgStaticExpanderParameterByPK(cfgStaticExpanderParameter.getId());
            }else {
                CfgStaticExpanderParameter_Example example = ExampleUtils.parseExample(cfgStaticExpanderParameter, CfgStaticExpanderParameter_Example.class);
                List<CfgStaticExpanderParameter> list = iCfgStaticExpanderParameterSV.getCfgStaticExpanderParameterListByExample(example);
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
    * 搜索一个静态扩展器参数
    * @param  cfgStaticExpanderParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgStaticExpanderParameter") CfgStaticExpanderParameter  cfgStaticExpanderParameter){
        logger.debug("request : {}",  cfgStaticExpanderParameter);
        try{
            CfgStaticExpanderParameter result = null;
            if(cfgStaticExpanderParameter.getId() != null) {
                result =  iCfgStaticExpanderParameterSV.getCfgStaticExpanderParameterByPK(cfgStaticExpanderParameter.getId());
            }else {
                CfgStaticExpanderParameter_Example example = ExampleUtils.parseExample( cfgStaticExpanderParameter, CfgStaticExpanderParameter_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgStaticExpanderParameter> list =  iCfgStaticExpanderParameterSV.getCfgStaticExpanderParameterListByExample(example);
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
    * 创建静态扩展器参数
    * @param cfgStaticExpanderParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgStaticExpanderParameter") CfgStaticExpanderParameter cfgStaticExpanderParameter) {
        logger.debug("request : {}", cfgStaticExpanderParameter);
        try {
            ControllerHelper.setDefaultValue(cfgStaticExpanderParameter, "id");
            int result = iCfgStaticExpanderParameterSV.create(cfgStaticExpanderParameter);
            if(result > 0) {
                return ResultData.success(cfgStaticExpanderParameter);
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
    * 批量维护静态扩展器参数
    * @param cfgStaticExpanderParameters
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgStaticExpanderParameter[] cfgStaticExpanderParameters) {
        logger.debug("request : {}", cfgStaticExpanderParameters);

        try {
            ControllerHelper.setDefaultValue(cfgStaticExpanderParameters, "id");
            ControllerHelper.reorderProperty(cfgStaticExpanderParameters);

            int result = iCfgStaticExpanderParameterSV.batchOperate(cfgStaticExpanderParameters);
            if(result > 0) {
                return ResultData.success(cfgStaticExpanderParameters);
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
    * 更新静态扩展器参数
    * @param cfgStaticExpanderParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgStaticExpanderParameter") CfgStaticExpanderParameter cfgStaticExpanderParameter) {
        logger.debug("request : {}", cfgStaticExpanderParameter);
        try {
            ControllerHelper.setDefaultValue(cfgStaticExpanderParameter, "id");
            int result = iCfgStaticExpanderParameterSV.update(cfgStaticExpanderParameter);
            if(result > 0) {
                return ResultData.success(cfgStaticExpanderParameter);
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
    * 创建或更新静态扩展器参数
    * @param cfgStaticExpanderParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgStaticExpanderParameter") CfgStaticExpanderParameter cfgStaticExpanderParameter) {
        logger.debug("request : {}", cfgStaticExpanderParameter);
        try {
            ControllerHelper.setDefaultValue(cfgStaticExpanderParameter, "id");
            int result = iCfgStaticExpanderParameterSV.batchOperate(new CfgStaticExpanderParameter[]{cfgStaticExpanderParameter});
            if(result > 0) {
                return ResultData.success(cfgStaticExpanderParameter);
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
    * 删除静态扩展器参数
    * @param cfgStaticExpanderParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgStaticExpanderParameter") CfgStaticExpanderParameter cfgStaticExpanderParameter) {
        logger.debug("request : {}", cfgStaticExpanderParameter);

        try {
            ControllerHelper.setDefaultValue(cfgStaticExpanderParameter, "id");
            int result = iCfgStaticExpanderParameterSV.delete(cfgStaticExpanderParameter);
            if(result > 0) {
                return ResultData.success(cfgStaticExpanderParameter);
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
 	
	public ICfgStaticExpanderParameterSV getICfgStaticExpanderParameterSV(){
		return iCfgStaticExpanderParameterSV;
	}
	//setter
	public void setICfgStaticExpanderParameterSV(ICfgStaticExpanderParameterSV iCfgStaticExpanderParameterSV){
    	this.iCfgStaticExpanderParameterSV = iCfgStaticExpanderParameterSV;
    }
}
