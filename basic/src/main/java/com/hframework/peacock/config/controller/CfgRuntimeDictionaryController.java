package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgRuntimeDictionary;
import com.hframework.peacock.config.domain.model.CfgRuntimeDictionary_Example;
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeDictionarySV;
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

@Controller
@RequestMapping(value = "/config/cfgRuntimeDictionary")
public class CfgRuntimeDictionaryController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgRuntimeDictionaryController.class);

	@Resource
	private ICfgRuntimeDictionarySV iCfgRuntimeDictionarySV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示API字典列表
     * @param cfgRuntimeDictionary
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgRuntimeDictionary") CfgRuntimeDictionary cfgRuntimeDictionary,
                           @ModelAttribute("example") CfgRuntimeDictionary_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgRuntimeDictionary, example, pagination);
        try{
            ExampleUtils.parseExample(cfgRuntimeDictionary, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgRuntimeDictionary> list = iCfgRuntimeDictionarySV.getCfgRuntimeDictionaryListByExample(example);
            pagination.setTotalCount(iCfgRuntimeDictionarySV.getCfgRuntimeDictionaryCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示API字典明细
     * @param cfgRuntimeDictionary
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgRuntimeDictionary") CfgRuntimeDictionary cfgRuntimeDictionary){
        logger.debug("request : {},{}", cfgRuntimeDictionary.getId(), cfgRuntimeDictionary);
        try{
            CfgRuntimeDictionary result = null;
            if(cfgRuntimeDictionary.getId() != null) {
                result = iCfgRuntimeDictionarySV.getCfgRuntimeDictionaryByPK(cfgRuntimeDictionary.getId());
            }else {
                CfgRuntimeDictionary_Example example = ExampleUtils.parseExample(cfgRuntimeDictionary, CfgRuntimeDictionary_Example.class);
                List<CfgRuntimeDictionary> list = iCfgRuntimeDictionarySV.getCfgRuntimeDictionaryListByExample(example);
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
    * 搜索一个API字典
    * @param  cfgRuntimeDictionary
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgRuntimeDictionary") CfgRuntimeDictionary  cfgRuntimeDictionary){
        logger.debug("request : {}",  cfgRuntimeDictionary);
        try{
            CfgRuntimeDictionary result = null;
            if(cfgRuntimeDictionary.getId() != null) {
                result =  iCfgRuntimeDictionarySV.getCfgRuntimeDictionaryByPK(cfgRuntimeDictionary.getId());
            }else {
                CfgRuntimeDictionary_Example example = ExampleUtils.parseExample( cfgRuntimeDictionary, CfgRuntimeDictionary_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgRuntimeDictionary> list =  iCfgRuntimeDictionarySV.getCfgRuntimeDictionaryListByExample(example);
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
    * 创建API字典
    * @param cfgRuntimeDictionary
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgRuntimeDictionary") CfgRuntimeDictionary cfgRuntimeDictionary) {
        logger.debug("request : {}", cfgRuntimeDictionary);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeDictionary, "id");
            int result = iCfgRuntimeDictionarySV.create(cfgRuntimeDictionary);
            if(result > 0) {
                return ResultData.success(cfgRuntimeDictionary);
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
    * 批量维护API字典
    * @param cfgRuntimeDictionarys
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgRuntimeDictionary[] cfgRuntimeDictionarys) {
        logger.debug("request : {}", cfgRuntimeDictionarys);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeDictionarys, "id");
            ControllerHelper.reorderProperty(cfgRuntimeDictionarys);

            int result = iCfgRuntimeDictionarySV.batchOperate(cfgRuntimeDictionarys);
            if(result > 0) {
                return ResultData.success(cfgRuntimeDictionarys);
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
    * 更新API字典
    * @param cfgRuntimeDictionary
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgRuntimeDictionary") CfgRuntimeDictionary cfgRuntimeDictionary) {
        logger.debug("request : {}", cfgRuntimeDictionary);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeDictionary, "id");
            int result = iCfgRuntimeDictionarySV.update(cfgRuntimeDictionary);
            if(result > 0) {
                return ResultData.success(cfgRuntimeDictionary);
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
    * 创建或更新API字典
    * @param cfgRuntimeDictionary
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgRuntimeDictionary") CfgRuntimeDictionary cfgRuntimeDictionary) {
        logger.debug("request : {}", cfgRuntimeDictionary);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeDictionary, "id");
            int result = iCfgRuntimeDictionarySV.batchOperate(new CfgRuntimeDictionary[]{cfgRuntimeDictionary});
            if(result > 0) {
                return ResultData.success(cfgRuntimeDictionary);
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
    * 删除API字典
    * @param cfgRuntimeDictionary
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgRuntimeDictionary") CfgRuntimeDictionary cfgRuntimeDictionary) {
        logger.debug("request : {}", cfgRuntimeDictionary);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeDictionary, "id");
            int result = iCfgRuntimeDictionarySV.delete(cfgRuntimeDictionary);
            if(result > 0) {
                return ResultData.success(cfgRuntimeDictionary);
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
 	
	public ICfgRuntimeDictionarySV getICfgRuntimeDictionarySV(){
		return iCfgRuntimeDictionarySV;
	}
	//setter
	public void setICfgRuntimeDictionarySV(ICfgRuntimeDictionarySV iCfgRuntimeDictionarySV){
    	this.iCfgRuntimeDictionarySV = iCfgRuntimeDictionarySV;
    }
}
