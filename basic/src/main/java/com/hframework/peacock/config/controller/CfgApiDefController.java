package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgApiDef;
import com.hframework.peacock.config.domain.model.CfgApiDef_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgApiDefSV;

@Controller
@RequestMapping(value = "/config/cfgApiDef")
public class CfgApiDefController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgApiDefController.class);

	@Resource
	private ICfgApiDefSV iCfgApiDefSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示API瀹氫箟琛�列表
     * @param cfgApiDef
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgApiDef") CfgApiDef cfgApiDef,
                           @ModelAttribute("example") CfgApiDef_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgApiDef, example, pagination);
        try{
            ExampleUtils.parseExample(cfgApiDef, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgApiDef> list = iCfgApiDefSV.getCfgApiDefListByExample(example);
            pagination.setTotalCount(iCfgApiDefSV.getCfgApiDefCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示API瀹氫箟琛�明细
     * @param cfgApiDef
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgApiDef") CfgApiDef cfgApiDef){
        logger.debug("request : {},{}", cfgApiDef.getId(), cfgApiDef);
        try{
            CfgApiDef result = null;
            if(cfgApiDef.getId() != null) {
                result = iCfgApiDefSV.getCfgApiDefByPK(cfgApiDef.getId());
            }else {
                CfgApiDef_Example example = ExampleUtils.parseExample(cfgApiDef, CfgApiDef_Example.class);
                List<CfgApiDef> list = iCfgApiDefSV.getCfgApiDefListByExample(example);
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
    * 搜索一个API瀹氫箟琛�
    * @param  cfgApiDef
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgApiDef") CfgApiDef  cfgApiDef){
        logger.debug("request : {}",  cfgApiDef);
        try{
            CfgApiDef result = null;
            if(cfgApiDef.getId() != null) {
                result =  iCfgApiDefSV.getCfgApiDefByPK(cfgApiDef.getId());
            }else {
                CfgApiDef_Example example = ExampleUtils.parseExample( cfgApiDef, CfgApiDef_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgApiDef> list =  iCfgApiDefSV.getCfgApiDefListByExample(example);
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
    * 创建API瀹氫箟琛�
    * @param cfgApiDef
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgApiDef") CfgApiDef cfgApiDef) {
        logger.debug("request : {}", cfgApiDef);
        try {
            ControllerHelper.setDefaultValue(cfgApiDef, "cfgApiDefId");
            int result = iCfgApiDefSV.create(cfgApiDef);
            if(result > 0) {
                return ResultData.success(cfgApiDef);
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
    * 批量维护API瀹氫箟琛�
    * @param cfgApiDefs
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgApiDef[] cfgApiDefs) {
        logger.debug("request : {}", cfgApiDefs);

        try {
            ControllerHelper.setDefaultValue(cfgApiDefs, "cfgApiDefId");
            ControllerHelper.reorderProperty(cfgApiDefs);

            int result = iCfgApiDefSV.batchOperate(cfgApiDefs);
            if(result > 0) {
                return ResultData.success(cfgApiDefs);
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
    * 更新API瀹氫箟琛�
    * @param cfgApiDef
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgApiDef") CfgApiDef cfgApiDef) {
        logger.debug("request : {}", cfgApiDef);
        try {
            ControllerHelper.setDefaultValue(cfgApiDef, "cfgApiDefId");
            int result = iCfgApiDefSV.update(cfgApiDef);
            if(result > 0) {
                return ResultData.success(cfgApiDef);
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
    * 创建或更新API瀹氫箟琛�
    * @param cfgApiDef
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgApiDef") CfgApiDef cfgApiDef) {
        logger.debug("request : {}", cfgApiDef);
        try {
            ControllerHelper.setDefaultValue(cfgApiDef, "cfgApiDefId");
            int result = iCfgApiDefSV.batchOperate(new CfgApiDef[]{cfgApiDef});
            if(result > 0) {
                return ResultData.success(cfgApiDef);
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
    * 删除API瀹氫箟琛�
    * @param cfgApiDef
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgApiDef") CfgApiDef cfgApiDef) {
        logger.debug("request : {}", cfgApiDef);

        try {
            ControllerHelper.setDefaultValue(cfgApiDef, "cfgApiDefId");
            int result = iCfgApiDefSV.delete(cfgApiDef);
            if(result > 0) {
                return ResultData.success(cfgApiDef);
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
 	
	public ICfgApiDefSV getICfgApiDefSV(){
		return iCfgApiDefSV;
	}
	//setter
	public void setICfgApiDefSV(ICfgApiDefSV iCfgApiDefSV){
    	this.iCfgApiDefSV = iCfgApiDefSV;
    }
}
