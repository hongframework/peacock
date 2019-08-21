package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgApiConf;
import com.hframework.peacock.config.domain.model.CfgApiConf_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgApiConfSV;

@Controller
@RequestMapping(value = "/config/cfgApiConf")
public class CfgApiConfController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgApiConfController.class);

	@Resource
	private ICfgApiConfSV iCfgApiConfSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示API配置表列表
     * @param cfgApiConf
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgApiConf") CfgApiConf cfgApiConf,
                           @ModelAttribute("example") CfgApiConf_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgApiConf, example, pagination);
        try{
            ExampleUtils.parseExample(cfgApiConf, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgApiConf> list = iCfgApiConfSV.getCfgApiConfListByExample(example);
            pagination.setTotalCount(iCfgApiConfSV.getCfgApiConfCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示API配置表明细
     * @param cfgApiConf
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgApiConf") CfgApiConf cfgApiConf){
        logger.debug("request : {},{}", cfgApiConf.getId(), cfgApiConf);
        try{
            CfgApiConf result = null;
            if(cfgApiConf.getId() != null) {
                result = iCfgApiConfSV.getCfgApiConfByPK(cfgApiConf.getId());
            }else {
                CfgApiConf_Example example = ExampleUtils.parseExample(cfgApiConf, CfgApiConf_Example.class);
                List<CfgApiConf> list = iCfgApiConfSV.getCfgApiConfListByExample(example);
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
    * 搜索一个API配置表
    * @param  cfgApiConf
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgApiConf") CfgApiConf  cfgApiConf){
        logger.debug("request : {}",  cfgApiConf);
        try{
            CfgApiConf result = null;
            if(cfgApiConf.getId() != null) {
                result =  iCfgApiConfSV.getCfgApiConfByPK(cfgApiConf.getId());
            }else {
                CfgApiConf_Example example = ExampleUtils.parseExample( cfgApiConf, CfgApiConf_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgApiConf> list =  iCfgApiConfSV.getCfgApiConfListByExample(example);
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
    * 创建API配置表
    * @param cfgApiConf
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgApiConf") CfgApiConf cfgApiConf) {
        logger.debug("request : {}", cfgApiConf);
        try {
            ControllerHelper.setDefaultValue(cfgApiConf, "id");
            int result = iCfgApiConfSV.create(cfgApiConf);
            if(result > 0) {
                return ResultData.success(cfgApiConf);
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
    * 批量维护API配置表
    * @param cfgApiConfs
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgApiConf[] cfgApiConfs) {
        logger.debug("request : {}", cfgApiConfs);

        try {
            ControllerHelper.setDefaultValue(cfgApiConfs, "id");
            ControllerHelper.reorderProperty(cfgApiConfs);

            int result = iCfgApiConfSV.batchOperate(cfgApiConfs);
            if(result > 0) {
                return ResultData.success(cfgApiConfs);
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
    * 更新API配置表
    * @param cfgApiConf
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgApiConf") CfgApiConf cfgApiConf) {
        logger.debug("request : {}", cfgApiConf);
        try {
            ControllerHelper.setDefaultValue(cfgApiConf, "id");
            int result = iCfgApiConfSV.update(cfgApiConf);
            if(result > 0) {
                return ResultData.success(cfgApiConf);
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
    * 创建或更新API配置表
    * @param cfgApiConf
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgApiConf") CfgApiConf cfgApiConf) {
        logger.debug("request : {}", cfgApiConf);
        try {
            ControllerHelper.setDefaultValue(cfgApiConf, "id");
            int result = iCfgApiConfSV.batchOperate(new CfgApiConf[]{cfgApiConf});
            if(result > 0) {
                return ResultData.success(cfgApiConf);
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
    * 删除API配置表
    * @param cfgApiConf
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgApiConf") CfgApiConf cfgApiConf) {
        logger.debug("request : {}", cfgApiConf);

        try {
            ControllerHelper.setDefaultValue(cfgApiConf, "id");
            int result = iCfgApiConfSV.delete(cfgApiConf);
            if(result > 0) {
                return ResultData.success(cfgApiConf);
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
 	
	public ICfgApiConfSV getICfgApiConfSV(){
		return iCfgApiConfSV;
	}
	//setter
	public void setICfgApiConfSV(ICfgApiConfSV iCfgApiConfSV){
    	this.iCfgApiConfSV = iCfgApiConfSV;
    }
}
