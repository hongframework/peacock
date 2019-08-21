package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.service.interfaces.ICfgStaticExpanderSV;
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
import com.hframework.peacock.config.domain.model.CfgStaticExpander;
import com.hframework.peacock.config.domain.model.CfgStaticExpander_Example;

@Controller
@RequestMapping(value = "/config/cfgStaticExpander")
public class CfgStaticExpanderController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgStaticExpanderController.class);

	@Resource
	private ICfgStaticExpanderSV iCfgStaticExpanderSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示静态扩展器列表
     * @param cfgStaticExpander
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgStaticExpander") CfgStaticExpander cfgStaticExpander,
                                      @ModelAttribute("example") CfgStaticExpander_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgStaticExpander, example, pagination);
        try{
            ExampleUtils.parseExample(cfgStaticExpander, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgStaticExpander> list = iCfgStaticExpanderSV.getCfgStaticExpanderListByExample(example);
            pagination.setTotalCount(iCfgStaticExpanderSV.getCfgStaticExpanderCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示静态扩展器明细
     * @param cfgStaticExpander
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgStaticExpander") CfgStaticExpander cfgStaticExpander){
        logger.debug("request : {},{}", cfgStaticExpander.getId(), cfgStaticExpander);
        try{
            CfgStaticExpander result = null;
            if(cfgStaticExpander.getId() != null) {
                result = iCfgStaticExpanderSV.getCfgStaticExpanderByPK(cfgStaticExpander.getId());
            }else {
                CfgStaticExpander_Example example = ExampleUtils.parseExample(cfgStaticExpander, CfgStaticExpander_Example.class);
                List<CfgStaticExpander> list = iCfgStaticExpanderSV.getCfgStaticExpanderListByExample(example);
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
    * 搜索一个静态扩展器
    * @param  cfgStaticExpander
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgStaticExpander") CfgStaticExpander  cfgStaticExpander){
        logger.debug("request : {}",  cfgStaticExpander);
        try{
            CfgStaticExpander result = null;
            if(cfgStaticExpander.getId() != null) {
                result =  iCfgStaticExpanderSV.getCfgStaticExpanderByPK(cfgStaticExpander.getId());
            }else {
                CfgStaticExpander_Example example = ExampleUtils.parseExample( cfgStaticExpander, CfgStaticExpander_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgStaticExpander> list =  iCfgStaticExpanderSV.getCfgStaticExpanderListByExample(example);
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
    * 创建静态扩展器
    * @param cfgStaticExpander
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgStaticExpander") CfgStaticExpander cfgStaticExpander) {
        logger.debug("request : {}", cfgStaticExpander);
        try {
            ControllerHelper.setDefaultValue(cfgStaticExpander, "id");
            int result = iCfgStaticExpanderSV.create(cfgStaticExpander);
            if(result > 0) {
                return ResultData.success(cfgStaticExpander);
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
    * 批量维护静态扩展器
    * @param cfgStaticExpanders
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgStaticExpander[] cfgStaticExpanders) {
        logger.debug("request : {}", cfgStaticExpanders);

        try {
            ControllerHelper.setDefaultValue(cfgStaticExpanders, "id");
            ControllerHelper.reorderProperty(cfgStaticExpanders);

            int result = iCfgStaticExpanderSV.batchOperate(cfgStaticExpanders);
            if(result > 0) {
                return ResultData.success(cfgStaticExpanders);
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
    * 更新静态扩展器
    * @param cfgStaticExpander
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgStaticExpander") CfgStaticExpander cfgStaticExpander) {
        logger.debug("request : {}", cfgStaticExpander);
        try {
            ControllerHelper.setDefaultValue(cfgStaticExpander, "id");
            int result = iCfgStaticExpanderSV.update(cfgStaticExpander);
            if(result > 0) {
                return ResultData.success(cfgStaticExpander);
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
    * 创建或更新静态扩展器
    * @param cfgStaticExpander
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgStaticExpander") CfgStaticExpander cfgStaticExpander) {
        logger.debug("request : {}", cfgStaticExpander);
        try {
            ControllerHelper.setDefaultValue(cfgStaticExpander, "id");
            int result = iCfgStaticExpanderSV.batchOperate(new CfgStaticExpander[]{cfgStaticExpander});
            if(result > 0) {
                return ResultData.success(cfgStaticExpander);
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
    * 删除静态扩展器
    * @param cfgStaticExpander
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgStaticExpander") CfgStaticExpander cfgStaticExpander) {
        logger.debug("request : {}", cfgStaticExpander);

        try {
            ControllerHelper.setDefaultValue(cfgStaticExpander, "id");
            int result = iCfgStaticExpanderSV.delete(cfgStaticExpander);
            if(result > 0) {
                return ResultData.success(cfgStaticExpander);
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
 	
	public ICfgStaticExpanderSV getICfgStaticExpanderSV(){
		return iCfgStaticExpanderSV;
	}
	//setter
	public void setICfgStaticExpanderSV(ICfgStaticExpanderSV iCfgStaticExpanderSV){
    	this.iCfgStaticExpanderSV = iCfgStaticExpanderSV;
    }
}
