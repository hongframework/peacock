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
import com.hframework.peacock.config.domain.model.CfgMgrVersion;
import com.hframework.peacock.config.domain.model.CfgMgrVersion_Example;
import com.hframework.peacock.config.service.interfaces.ICfgMgrVersionSV;

@Controller
@RequestMapping(value = "/config/cfgMgrVersion")
public class CfgMgrVersionController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgMgrVersionController.class);

	@Resource
	private ICfgMgrVersionSV iCfgMgrVersionSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示版本列表
     * @param cfgMgrVersion
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgMgrVersion") CfgMgrVersion cfgMgrVersion,
                                      @ModelAttribute("example") CfgMgrVersion_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgMgrVersion, example, pagination);
        try{
            ExampleUtils.parseExample(cfgMgrVersion, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgMgrVersion> list = iCfgMgrVersionSV.getCfgMgrVersionListByExample(example);
            pagination.setTotalCount(iCfgMgrVersionSV.getCfgMgrVersionCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示版本明细
     * @param cfgMgrVersion
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgMgrVersion") CfgMgrVersion cfgMgrVersion){
        logger.debug("request : {},{}", cfgMgrVersion.getId(), cfgMgrVersion);
        try{
            CfgMgrVersion result = null;
            if(cfgMgrVersion.getId() != null) {
                result = iCfgMgrVersionSV.getCfgMgrVersionByPK(cfgMgrVersion.getId());
            }else {
                CfgMgrVersion_Example example = ExampleUtils.parseExample(cfgMgrVersion, CfgMgrVersion_Example.class);
                List<CfgMgrVersion> list = iCfgMgrVersionSV.getCfgMgrVersionListByExample(example);
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
    * 搜索一个版本
    * @param  cfgMgrVersion
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgMgrVersion") CfgMgrVersion  cfgMgrVersion){
        logger.debug("request : {}",  cfgMgrVersion);
        try{
            CfgMgrVersion result = null;
            if(cfgMgrVersion.getId() != null) {
                result =  iCfgMgrVersionSV.getCfgMgrVersionByPK(cfgMgrVersion.getId());
            }else {
                CfgMgrVersion_Example example = ExampleUtils.parseExample( cfgMgrVersion, CfgMgrVersion_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgMgrVersion> list =  iCfgMgrVersionSV.getCfgMgrVersionListByExample(example);
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
    * 创建版本
    * @param cfgMgrVersion
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgMgrVersion") CfgMgrVersion cfgMgrVersion) {
        logger.debug("request : {}", cfgMgrVersion);
        try {
            ControllerHelper.setDefaultValue(cfgMgrVersion, "id");
            int result = iCfgMgrVersionSV.create(cfgMgrVersion);
            if(result > 0) {
                return ResultData.success(cfgMgrVersion);
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
    * 批量维护版本
    * @param cfgMgrVersions
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgMgrVersion[] cfgMgrVersions) {
        logger.debug("request : {}", cfgMgrVersions);

        try {
            ControllerHelper.setDefaultValue(cfgMgrVersions, "id");
            ControllerHelper.reorderProperty(cfgMgrVersions);

            int result = iCfgMgrVersionSV.batchOperate(cfgMgrVersions);
            if(result > 0) {
                return ResultData.success(cfgMgrVersions);
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
    * 更新版本
    * @param cfgMgrVersion
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgMgrVersion") CfgMgrVersion cfgMgrVersion) {
        logger.debug("request : {}", cfgMgrVersion);
        try {
            ControllerHelper.setDefaultValue(cfgMgrVersion, "id");
            int result = iCfgMgrVersionSV.update(cfgMgrVersion);
            if(result > 0) {
                return ResultData.success(cfgMgrVersion);
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
    * 创建或更新版本
    * @param cfgMgrVersion
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgMgrVersion") CfgMgrVersion cfgMgrVersion) {
        logger.debug("request : {}", cfgMgrVersion);
        try {
            ControllerHelper.setDefaultValue(cfgMgrVersion, "id");
            int result = iCfgMgrVersionSV.batchOperate(new CfgMgrVersion[]{cfgMgrVersion});
            if(result > 0) {
                return ResultData.success(cfgMgrVersion);
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
    * 删除版本
    * @param cfgMgrVersion
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgMgrVersion") CfgMgrVersion cfgMgrVersion) {
        logger.debug("request : {}", cfgMgrVersion);

        try {
            ControllerHelper.setDefaultValue(cfgMgrVersion, "id");
            int result = iCfgMgrVersionSV.delete(cfgMgrVersion);
            if(result > 0) {
                return ResultData.success(cfgMgrVersion);
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
 	
	public ICfgMgrVersionSV getICfgMgrVersionSV(){
		return iCfgMgrVersionSV;
	}
	//setter
	public void setICfgMgrVersionSV(ICfgMgrVersionSV iCfgMgrVersionSV){
    	this.iCfgMgrVersionSV = iCfgMgrVersionSV;
    }
}
