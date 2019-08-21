package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgRuntimeHandler;
import com.hframework.peacock.config.domain.model.CfgRuntimeHandler_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeHandlerSV;

@Controller
@RequestMapping(value = "/config/cfgRuntimeHandler")
public class CfgRuntimeHandlerController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgRuntimeHandlerController.class);

	@Resource
	private ICfgRuntimeHandlerSV iCfgRuntimeHandlerSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示动态Handler列表
     * @param cfgRuntimeHandler
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgRuntimeHandler") CfgRuntimeHandler cfgRuntimeHandler,
                           @ModelAttribute("example") CfgRuntimeHandler_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgRuntimeHandler, example, pagination);
        try{
            ExampleUtils.parseExample(cfgRuntimeHandler, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgRuntimeHandler> list = iCfgRuntimeHandlerSV.getCfgRuntimeHandlerListByExample(example);
            pagination.setTotalCount(iCfgRuntimeHandlerSV.getCfgRuntimeHandlerCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示动态Handler明细
     * @param cfgRuntimeHandler
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgRuntimeHandler") CfgRuntimeHandler cfgRuntimeHandler){
        logger.debug("request : {},{}", cfgRuntimeHandler.getId(), cfgRuntimeHandler);
        try{
            CfgRuntimeHandler result = null;
            if(cfgRuntimeHandler.getId() != null) {
                result = iCfgRuntimeHandlerSV.getCfgRuntimeHandlerByPK(cfgRuntimeHandler.getId());
            }else {
                CfgRuntimeHandler_Example example = ExampleUtils.parseExample(cfgRuntimeHandler, CfgRuntimeHandler_Example.class);
                List<CfgRuntimeHandler> list = iCfgRuntimeHandlerSV.getCfgRuntimeHandlerListByExample(example);
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
    * 搜索一个动态Handler
    * @param  cfgRuntimeHandler
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgRuntimeHandler") CfgRuntimeHandler  cfgRuntimeHandler){
        logger.debug("request : {}",  cfgRuntimeHandler);
        try{
            CfgRuntimeHandler result = null;
            if(cfgRuntimeHandler.getId() != null) {
                result =  iCfgRuntimeHandlerSV.getCfgRuntimeHandlerByPK(cfgRuntimeHandler.getId());
            }else {
                CfgRuntimeHandler_Example example = ExampleUtils.parseExample( cfgRuntimeHandler, CfgRuntimeHandler_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgRuntimeHandler> list =  iCfgRuntimeHandlerSV.getCfgRuntimeHandlerListByExample(example);
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
    * 创建动态Handler
    * @param cfgRuntimeHandler
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgRuntimeHandler") CfgRuntimeHandler cfgRuntimeHandler) {
        logger.debug("request : {}", cfgRuntimeHandler);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeHandler, "id");
            int result = iCfgRuntimeHandlerSV.create(cfgRuntimeHandler);
            if(result > 0) {
                return ResultData.success(cfgRuntimeHandler);
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
    * 批量维护动态Handler
    * @param cfgRuntimeHandlers
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgRuntimeHandler[] cfgRuntimeHandlers) {
        logger.debug("request : {}", cfgRuntimeHandlers);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeHandlers, "id");
            ControllerHelper.reorderProperty(cfgRuntimeHandlers);

            int result = iCfgRuntimeHandlerSV.batchOperate(cfgRuntimeHandlers);
            if(result > 0) {
                return ResultData.success(cfgRuntimeHandlers);
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
    * 更新动态Handler
    * @param cfgRuntimeHandler
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgRuntimeHandler") CfgRuntimeHandler cfgRuntimeHandler) {
        logger.debug("request : {}", cfgRuntimeHandler);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeHandler, "id");
            int result = iCfgRuntimeHandlerSV.update(cfgRuntimeHandler);
            if(result > 0) {
                return ResultData.success(cfgRuntimeHandler);
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
    * 创建或更新动态Handler
    * @param cfgRuntimeHandler
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgRuntimeHandler") CfgRuntimeHandler cfgRuntimeHandler) {
        logger.debug("request : {}", cfgRuntimeHandler);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeHandler, "id");
            int result = iCfgRuntimeHandlerSV.batchOperate(new CfgRuntimeHandler[]{cfgRuntimeHandler});
            if(result > 0) {
                return ResultData.success(cfgRuntimeHandler);
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
    * 删除动态Handler
    * @param cfgRuntimeHandler
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgRuntimeHandler") CfgRuntimeHandler cfgRuntimeHandler) {
        logger.debug("request : {}", cfgRuntimeHandler);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeHandler, "id");
            int result = iCfgRuntimeHandlerSV.delete(cfgRuntimeHandler);
            if(result > 0) {
                return ResultData.success(cfgRuntimeHandler);
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
 	
	public ICfgRuntimeHandlerSV getICfgRuntimeHandlerSV(){
		return iCfgRuntimeHandlerSV;
	}
	//setter
	public void setICfgRuntimeHandlerSV(ICfgRuntimeHandlerSV iCfgRuntimeHandlerSV){
    	this.iCfgRuntimeHandlerSV = iCfgRuntimeHandlerSV;
    }
}
