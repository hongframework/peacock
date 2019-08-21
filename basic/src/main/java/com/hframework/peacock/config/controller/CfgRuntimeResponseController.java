package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeResponseSV;
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
import com.hframework.peacock.config.domain.model.CfgRuntimeResponse;
import com.hframework.peacock.config.domain.model.CfgRuntimeResponse_Example;

@Controller
@RequestMapping(value = "/config/cfgRuntimeResponse")
public class CfgRuntimeResponseController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgRuntimeResponseController.class);

	@Resource
	private ICfgRuntimeResponseSV iCfgRuntimeResponseSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示响应参数列表
     * @param cfgRuntimeResponse
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgRuntimeResponse") CfgRuntimeResponse cfgRuntimeResponse,
                                      @ModelAttribute("example") CfgRuntimeResponse_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgRuntimeResponse, example, pagination);
        try{
            ExampleUtils.parseExample(cfgRuntimeResponse, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgRuntimeResponse> list = iCfgRuntimeResponseSV.getCfgRuntimeResponseListByExample(example);
            pagination.setTotalCount(iCfgRuntimeResponseSV.getCfgRuntimeResponseCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示响应参数明细
     * @param cfgRuntimeResponse
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgRuntimeResponse") CfgRuntimeResponse cfgRuntimeResponse){
        logger.debug("request : {},{}", cfgRuntimeResponse.getId(), cfgRuntimeResponse);
        try{
            CfgRuntimeResponse result = null;
            if(cfgRuntimeResponse.getId() != null) {
                result = iCfgRuntimeResponseSV.getCfgRuntimeResponseByPK(cfgRuntimeResponse.getId());
            }else {
                CfgRuntimeResponse_Example example = ExampleUtils.parseExample(cfgRuntimeResponse, CfgRuntimeResponse_Example.class);
                List<CfgRuntimeResponse> list = iCfgRuntimeResponseSV.getCfgRuntimeResponseListByExample(example);
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
    * 搜索一个响应参数
    * @param  cfgRuntimeResponse
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgRuntimeResponse") CfgRuntimeResponse  cfgRuntimeResponse){
        logger.debug("request : {}",  cfgRuntimeResponse);
        try{
            CfgRuntimeResponse result = null;
            if(cfgRuntimeResponse.getId() != null) {
                result =  iCfgRuntimeResponseSV.getCfgRuntimeResponseByPK(cfgRuntimeResponse.getId());
            }else {
                CfgRuntimeResponse_Example example = ExampleUtils.parseExample( cfgRuntimeResponse, CfgRuntimeResponse_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgRuntimeResponse> list =  iCfgRuntimeResponseSV.getCfgRuntimeResponseListByExample(example);
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
    * 创建响应参数
    * @param cfgRuntimeResponse
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgRuntimeResponse") CfgRuntimeResponse cfgRuntimeResponse) {
        logger.debug("request : {}", cfgRuntimeResponse);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeResponse, "id");
            int result = iCfgRuntimeResponseSV.create(cfgRuntimeResponse);
            if(result > 0) {
                return ResultData.success(cfgRuntimeResponse);
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
    * 批量维护响应参数
    * @param cfgRuntimeResponses
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgRuntimeResponse[] cfgRuntimeResponses) {
        logger.debug("request : {}", cfgRuntimeResponses);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeResponses, "id");
            ControllerHelper.reorderProperty(cfgRuntimeResponses);

            int result = iCfgRuntimeResponseSV.batchOperate(cfgRuntimeResponses);
            if(result > 0) {
                return ResultData.success(cfgRuntimeResponses);
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
    * 更新响应参数
    * @param cfgRuntimeResponse
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgRuntimeResponse") CfgRuntimeResponse cfgRuntimeResponse) {
        logger.debug("request : {}", cfgRuntimeResponse);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeResponse, "id");
            int result = iCfgRuntimeResponseSV.update(cfgRuntimeResponse);
            if(result > 0) {
                return ResultData.success(cfgRuntimeResponse);
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
    * 创建或更新响应参数
    * @param cfgRuntimeResponse
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgRuntimeResponse") CfgRuntimeResponse cfgRuntimeResponse) {
        logger.debug("request : {}", cfgRuntimeResponse);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeResponse, "id");
            int result = iCfgRuntimeResponseSV.batchOperate(new CfgRuntimeResponse[]{cfgRuntimeResponse});
            if(result > 0) {
                return ResultData.success(cfgRuntimeResponse);
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
    * 删除响应参数
    * @param cfgRuntimeResponse
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgRuntimeResponse") CfgRuntimeResponse cfgRuntimeResponse) {
        logger.debug("request : {}", cfgRuntimeResponse);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeResponse, "id");
            int result = iCfgRuntimeResponseSV.delete(cfgRuntimeResponse);
            if(result > 0) {
                return ResultData.success(cfgRuntimeResponse);
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
 	
	public ICfgRuntimeResponseSV getICfgRuntimeResponseSV(){
		return iCfgRuntimeResponseSV;
	}
	//setter
	public void setICfgRuntimeResponseSV(ICfgRuntimeResponseSV iCfgRuntimeResponseSV){
    	this.iCfgRuntimeResponseSV = iCfgRuntimeResponseSV;
    }
}
