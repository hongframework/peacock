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
import com.hframework.peacock.config.domain.model.CfgMgrModule;
import com.hframework.peacock.config.domain.model.CfgMgrModule_Example;
import com.hframework.peacock.config.service.interfaces.ICfgMgrModuleSV;

@Controller
@RequestMapping(value = "/config/cfgMgrModule")
public class CfgMgrModuleController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgMgrModuleController.class);

	@Resource
	private ICfgMgrModuleSV iCfgMgrModuleSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示模块列表
     * @param cfgMgrModule
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgMgrModule") CfgMgrModule cfgMgrModule,
                                      @ModelAttribute("example") CfgMgrModule_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgMgrModule, example, pagination);
        try{
            ExampleUtils.parseExample(cfgMgrModule, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgMgrModule> list = iCfgMgrModuleSV.getCfgMgrModuleListByExample(example);
            pagination.setTotalCount(iCfgMgrModuleSV.getCfgMgrModuleCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示模块明细
     * @param cfgMgrModule
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgMgrModule") CfgMgrModule cfgMgrModule){
        logger.debug("request : {},{}", cfgMgrModule.getId(), cfgMgrModule);
        try{
            CfgMgrModule result = null;
            if(cfgMgrModule.getId() != null) {
                result = iCfgMgrModuleSV.getCfgMgrModuleByPK(cfgMgrModule.getId());
            }else {
                CfgMgrModule_Example example = ExampleUtils.parseExample(cfgMgrModule, CfgMgrModule_Example.class);
                List<CfgMgrModule> list = iCfgMgrModuleSV.getCfgMgrModuleListByExample(example);
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
    * 搜索一个模块
    * @param  cfgMgrModule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgMgrModule") CfgMgrModule  cfgMgrModule){
        logger.debug("request : {}",  cfgMgrModule);
        try{
            CfgMgrModule result = null;
            if(cfgMgrModule.getId() != null) {
                result =  iCfgMgrModuleSV.getCfgMgrModuleByPK(cfgMgrModule.getId());
            }else {
                CfgMgrModule_Example example = ExampleUtils.parseExample( cfgMgrModule, CfgMgrModule_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgMgrModule> list =  iCfgMgrModuleSV.getCfgMgrModuleListByExample(example);
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
    * 创建模块
    * @param cfgMgrModule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgMgrModule") CfgMgrModule cfgMgrModule) {
        logger.debug("request : {}", cfgMgrModule);
        try {
            ControllerHelper.setDefaultValue(cfgMgrModule, "id");
            int result = iCfgMgrModuleSV.create(cfgMgrModule);
            if(result > 0) {
                return ResultData.success(cfgMgrModule);
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
    * 批量维护模块
    * @param cfgMgrModules
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgMgrModule[] cfgMgrModules) {
        logger.debug("request : {}", cfgMgrModules);

        try {
            ControllerHelper.setDefaultValue(cfgMgrModules, "id");
            ControllerHelper.reorderProperty(cfgMgrModules);

            int result = iCfgMgrModuleSV.batchOperate(cfgMgrModules);
            if(result > 0) {
                return ResultData.success(cfgMgrModules);
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
    * 更新模块
    * @param cfgMgrModule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgMgrModule") CfgMgrModule cfgMgrModule) {
        logger.debug("request : {}", cfgMgrModule);
        try {
            ControllerHelper.setDefaultValue(cfgMgrModule, "id");
            int result = iCfgMgrModuleSV.update(cfgMgrModule);
            if(result > 0) {
                return ResultData.success(cfgMgrModule);
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
    * 创建或更新模块
    * @param cfgMgrModule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgMgrModule") CfgMgrModule cfgMgrModule) {
        logger.debug("request : {}", cfgMgrModule);
        try {
            ControllerHelper.setDefaultValue(cfgMgrModule, "id");
            int result = iCfgMgrModuleSV.batchOperate(new CfgMgrModule[]{cfgMgrModule});
            if(result > 0) {
                return ResultData.success(cfgMgrModule);
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
    * 删除模块
    * @param cfgMgrModule
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgMgrModule") CfgMgrModule cfgMgrModule) {
        logger.debug("request : {}", cfgMgrModule);

        try {
            ControllerHelper.setDefaultValue(cfgMgrModule, "id");
            int result = iCfgMgrModuleSV.delete(cfgMgrModule);
            if(result > 0) {
                return ResultData.success(cfgMgrModule);
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
 	
	public ICfgMgrModuleSV getICfgMgrModuleSV(){
		return iCfgMgrModuleSV;
	}
	//setter
	public void setICfgMgrModuleSV(ICfgMgrModuleSV iCfgMgrModuleSV){
    	this.iCfgMgrModuleSV = iCfgMgrModuleSV;
    }
}
