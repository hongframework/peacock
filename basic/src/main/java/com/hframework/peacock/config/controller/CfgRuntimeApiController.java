package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgRuntimeApi;
import com.hframework.peacock.config.domain.model.CfgRuntimeApi_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeApiSV;

@Controller
@RequestMapping(value = "/config/cfgRuntimeApi")
public class CfgRuntimeApiController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgRuntimeApiController.class);

	@Resource
	private ICfgRuntimeApiSV iCfgRuntimeApiSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示动态API列表
     * @param cfgRuntimeApi
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgRuntimeApi") CfgRuntimeApi cfgRuntimeApi,
                           @ModelAttribute("example") CfgRuntimeApi_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgRuntimeApi, example, pagination);
        try{
            ExampleUtils.parseExample(cfgRuntimeApi, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgRuntimeApi> list = iCfgRuntimeApiSV.getCfgRuntimeApiListByExample(example);
            pagination.setTotalCount(iCfgRuntimeApiSV.getCfgRuntimeApiCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示动态API明细
     * @param cfgRuntimeApi
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgRuntimeApi") CfgRuntimeApi cfgRuntimeApi){
        logger.debug("request : {},{}", cfgRuntimeApi.getId(), cfgRuntimeApi);
        try{
            CfgRuntimeApi result = null;
            if(cfgRuntimeApi.getId() != null) {
                result = iCfgRuntimeApiSV.getCfgRuntimeApiByPK(cfgRuntimeApi.getId());
            }else {
                CfgRuntimeApi_Example example = ExampleUtils.parseExample(cfgRuntimeApi, CfgRuntimeApi_Example.class);
                List<CfgRuntimeApi> list = iCfgRuntimeApiSV.getCfgRuntimeApiListByExample(example);
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
    * 搜索一个动态API
    * @param  cfgRuntimeApi
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgRuntimeApi") CfgRuntimeApi  cfgRuntimeApi){
        logger.debug("request : {}",  cfgRuntimeApi);
        try{
            CfgRuntimeApi result = null;
            if(cfgRuntimeApi.getId() != null) {
                result =  iCfgRuntimeApiSV.getCfgRuntimeApiByPK(cfgRuntimeApi.getId());
            }else {
                CfgRuntimeApi_Example example = ExampleUtils.parseExample( cfgRuntimeApi, CfgRuntimeApi_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgRuntimeApi> list =  iCfgRuntimeApiSV.getCfgRuntimeApiListByExample(example);
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
    * 创建动态API
    * @param cfgRuntimeApi
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgRuntimeApi") CfgRuntimeApi cfgRuntimeApi) {
        logger.debug("request : {}", cfgRuntimeApi);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeApi, "id");
            int result = iCfgRuntimeApiSV.create(cfgRuntimeApi);
            if(result > 0) {
                return ResultData.success(cfgRuntimeApi);
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
    * 批量维护动态API
    * @param cfgRuntimeApis
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgRuntimeApi[] cfgRuntimeApis) {
        logger.debug("request : {}", cfgRuntimeApis);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeApis, "id");
            ControllerHelper.reorderProperty(cfgRuntimeApis);

            int result = iCfgRuntimeApiSV.batchOperate(cfgRuntimeApis);
            if(result > 0) {
                return ResultData.success(cfgRuntimeApis);
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
    * 更新动态API
    * @param cfgRuntimeApi
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgRuntimeApi") CfgRuntimeApi cfgRuntimeApi) {
        logger.debug("request : {}", cfgRuntimeApi);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeApi, "id");
            int result = iCfgRuntimeApiSV.update(cfgRuntimeApi);
            if(result > 0) {
                return ResultData.success(cfgRuntimeApi);
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
    * 创建或更新动态API
    * @param cfgRuntimeApi
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgRuntimeApi") CfgRuntimeApi cfgRuntimeApi) {
        logger.debug("request : {}", cfgRuntimeApi);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeApi, "id");
            int result = iCfgRuntimeApiSV.batchOperate(new CfgRuntimeApi[]{cfgRuntimeApi});
            if(result > 0) {
                return ResultData.success(cfgRuntimeApi);
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
    * 删除动态API
    * @param cfgRuntimeApi
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgRuntimeApi") CfgRuntimeApi cfgRuntimeApi) {
        logger.debug("request : {}", cfgRuntimeApi);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeApi, "id");
            int result = iCfgRuntimeApiSV.delete(cfgRuntimeApi);
            if(result > 0) {
                return ResultData.success(cfgRuntimeApi);
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
 	
	public ICfgRuntimeApiSV getICfgRuntimeApiSV(){
		return iCfgRuntimeApiSV;
	}
	//setter
	public void setICfgRuntimeApiSV(ICfgRuntimeApiSV iCfgRuntimeApiSV){
    	this.iCfgRuntimeApiSV = iCfgRuntimeApiSV;
    }
}
