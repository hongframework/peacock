package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeTraceSV;
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
import com.hframework.peacock.config.domain.model.CfgRuntimeTrace;
import com.hframework.peacock.config.domain.model.CfgRuntimeTrace_Example;

@Controller
@RequestMapping(value = "/config/cfgRuntimeTrace")
public class CfgRuntimeTraceController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgRuntimeTraceController.class);

	@Resource
	private ICfgRuntimeTraceSV iCfgRuntimeTraceSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示跟踪配置列表
     * @param cfgRuntimeTrace
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgRuntimeTrace") CfgRuntimeTrace cfgRuntimeTrace,
                                      @ModelAttribute("example") CfgRuntimeTrace_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgRuntimeTrace, example, pagination);
        try{
            ExampleUtils.parseExample(cfgRuntimeTrace, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgRuntimeTrace> list = iCfgRuntimeTraceSV.getCfgRuntimeTraceListByExample(example);
            pagination.setTotalCount(iCfgRuntimeTraceSV.getCfgRuntimeTraceCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示跟踪配置明细
     * @param cfgRuntimeTrace
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgRuntimeTrace") CfgRuntimeTrace cfgRuntimeTrace){
        logger.debug("request : {},{}", cfgRuntimeTrace.getId(), cfgRuntimeTrace);
        try{
            CfgRuntimeTrace result = null;
            if(cfgRuntimeTrace.getId() != null) {
                result = iCfgRuntimeTraceSV.getCfgRuntimeTraceByPK(cfgRuntimeTrace.getId());
            }else {
                CfgRuntimeTrace_Example example = ExampleUtils.parseExample(cfgRuntimeTrace, CfgRuntimeTrace_Example.class);
                List<CfgRuntimeTrace> list = iCfgRuntimeTraceSV.getCfgRuntimeTraceListByExample(example);
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
    * 搜索一个跟踪配置
    * @param  cfgRuntimeTrace
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgRuntimeTrace") CfgRuntimeTrace  cfgRuntimeTrace){
        logger.debug("request : {}",  cfgRuntimeTrace);
        try{
            CfgRuntimeTrace result = null;
            if(cfgRuntimeTrace.getId() != null) {
                result =  iCfgRuntimeTraceSV.getCfgRuntimeTraceByPK(cfgRuntimeTrace.getId());
            }else {
                CfgRuntimeTrace_Example example = ExampleUtils.parseExample( cfgRuntimeTrace, CfgRuntimeTrace_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgRuntimeTrace> list =  iCfgRuntimeTraceSV.getCfgRuntimeTraceListByExample(example);
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
    * 创建跟踪配置
    * @param cfgRuntimeTrace
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgRuntimeTrace") CfgRuntimeTrace cfgRuntimeTrace) {
        logger.debug("request : {}", cfgRuntimeTrace);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeTrace, "id");
            int result = iCfgRuntimeTraceSV.create(cfgRuntimeTrace);
            if(result > 0) {
                return ResultData.success(cfgRuntimeTrace);
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
    * 批量维护跟踪配置
    * @param cfgRuntimeTraces
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgRuntimeTrace[] cfgRuntimeTraces) {
        logger.debug("request : {}", cfgRuntimeTraces);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeTraces, "id");
            ControllerHelper.reorderProperty(cfgRuntimeTraces);

            int result = iCfgRuntimeTraceSV.batchOperate(cfgRuntimeTraces);
            if(result > 0) {
                return ResultData.success(cfgRuntimeTraces);
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
    * 更新跟踪配置
    * @param cfgRuntimeTrace
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgRuntimeTrace") CfgRuntimeTrace cfgRuntimeTrace) {
        logger.debug("request : {}", cfgRuntimeTrace);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeTrace, "id");
            int result = iCfgRuntimeTraceSV.update(cfgRuntimeTrace);
            if(result > 0) {
                return ResultData.success(cfgRuntimeTrace);
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
    * 创建或更新跟踪配置
    * @param cfgRuntimeTrace
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgRuntimeTrace") CfgRuntimeTrace cfgRuntimeTrace) {
        logger.debug("request : {}", cfgRuntimeTrace);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeTrace, "id");
            int result = iCfgRuntimeTraceSV.batchOperate(new CfgRuntimeTrace[]{cfgRuntimeTrace});
            if(result > 0) {
                return ResultData.success(cfgRuntimeTrace);
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
    * 删除跟踪配置
    * @param cfgRuntimeTrace
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgRuntimeTrace") CfgRuntimeTrace cfgRuntimeTrace) {
        logger.debug("request : {}", cfgRuntimeTrace);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeTrace, "id");
            int result = iCfgRuntimeTraceSV.delete(cfgRuntimeTrace);
            if(result > 0) {
                return ResultData.success(cfgRuntimeTrace);
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
 	
	public ICfgRuntimeTraceSV getICfgRuntimeTraceSV(){
		return iCfgRuntimeTraceSV;
	}
	//setter
	public void setICfgRuntimeTraceSV(ICfgRuntimeTraceSV iCfgRuntimeTraceSV){
    	this.iCfgRuntimeTraceSV = iCfgRuntimeTraceSV;
    }
}
