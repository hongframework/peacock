package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgRuntimeDictionaryItems;
import com.hframework.peacock.config.domain.model.CfgRuntimeDictionaryItems_Example;
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeDictionaryItemsSV;
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
@RequestMapping(value = "/config/cfgRuntimeDictionaryItems")
public class CfgRuntimeDictionaryItemsController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgRuntimeDictionaryItemsController.class);

	@Resource
	private ICfgRuntimeDictionaryItemsSV iCfgRuntimeDictionaryItemsSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示API字典项列表
     * @param cfgRuntimeDictionaryItems
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgRuntimeDictionaryItems") CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems,
                           @ModelAttribute("example") CfgRuntimeDictionaryItems_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgRuntimeDictionaryItems, example, pagination);
        try{
            ExampleUtils.parseExample(cfgRuntimeDictionaryItems, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgRuntimeDictionaryItems> list = iCfgRuntimeDictionaryItemsSV.getCfgRuntimeDictionaryItemsListByExample(example);
            pagination.setTotalCount(iCfgRuntimeDictionaryItemsSV.getCfgRuntimeDictionaryItemsCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示API字典项明细
     * @param cfgRuntimeDictionaryItems
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgRuntimeDictionaryItems") CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems){
        logger.debug("request : {},{}", cfgRuntimeDictionaryItems.getId(), cfgRuntimeDictionaryItems);
        try{
            CfgRuntimeDictionaryItems result = null;
            if(cfgRuntimeDictionaryItems.getId() != null) {
                result = iCfgRuntimeDictionaryItemsSV.getCfgRuntimeDictionaryItemsByPK(cfgRuntimeDictionaryItems.getId());
            }else {
                CfgRuntimeDictionaryItems_Example example = ExampleUtils.parseExample(cfgRuntimeDictionaryItems, CfgRuntimeDictionaryItems_Example.class);
                List<CfgRuntimeDictionaryItems> list = iCfgRuntimeDictionaryItemsSV.getCfgRuntimeDictionaryItemsListByExample(example);
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
    * 搜索一个API字典项
    * @param  cfgRuntimeDictionaryItems
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgRuntimeDictionaryItems") CfgRuntimeDictionaryItems  cfgRuntimeDictionaryItems){
        logger.debug("request : {}",  cfgRuntimeDictionaryItems);
        try{
            CfgRuntimeDictionaryItems result = null;
            if(cfgRuntimeDictionaryItems.getId() != null) {
                result =  iCfgRuntimeDictionaryItemsSV.getCfgRuntimeDictionaryItemsByPK(cfgRuntimeDictionaryItems.getId());
            }else {
                CfgRuntimeDictionaryItems_Example example = ExampleUtils.parseExample( cfgRuntimeDictionaryItems, CfgRuntimeDictionaryItems_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgRuntimeDictionaryItems> list =  iCfgRuntimeDictionaryItemsSV.getCfgRuntimeDictionaryItemsListByExample(example);
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
    * 创建API字典项
    * @param cfgRuntimeDictionaryItems
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgRuntimeDictionaryItems") CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems) {
        logger.debug("request : {}", cfgRuntimeDictionaryItems);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeDictionaryItems, "id");
            int result = iCfgRuntimeDictionaryItemsSV.create(cfgRuntimeDictionaryItems);
            if(result > 0) {
                return ResultData.success(cfgRuntimeDictionaryItems);
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
    * 批量维护API字典项
    * @param cfgRuntimeDictionaryItemss
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgRuntimeDictionaryItems[] cfgRuntimeDictionaryItemss) {
        logger.debug("request : {}", cfgRuntimeDictionaryItemss);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeDictionaryItemss, "id");
            ControllerHelper.reorderProperty(cfgRuntimeDictionaryItemss);

            int result = iCfgRuntimeDictionaryItemsSV.batchOperate(cfgRuntimeDictionaryItemss);
            if(result > 0) {
                return ResultData.success(cfgRuntimeDictionaryItemss);
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
    * 更新API字典项
    * @param cfgRuntimeDictionaryItems
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgRuntimeDictionaryItems") CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems) {
        logger.debug("request : {}", cfgRuntimeDictionaryItems);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeDictionaryItems, "id");
            int result = iCfgRuntimeDictionaryItemsSV.update(cfgRuntimeDictionaryItems);
            if(result > 0) {
                return ResultData.success(cfgRuntimeDictionaryItems);
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
    * 创建或更新API字典项
    * @param cfgRuntimeDictionaryItems
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgRuntimeDictionaryItems") CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems) {
        logger.debug("request : {}", cfgRuntimeDictionaryItems);
        try {
            ControllerHelper.setDefaultValue(cfgRuntimeDictionaryItems, "id");
            int result = iCfgRuntimeDictionaryItemsSV.batchOperate(new CfgRuntimeDictionaryItems[]{cfgRuntimeDictionaryItems});
            if(result > 0) {
                return ResultData.success(cfgRuntimeDictionaryItems);
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
    * 删除API字典项
    * @param cfgRuntimeDictionaryItems
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgRuntimeDictionaryItems") CfgRuntimeDictionaryItems cfgRuntimeDictionaryItems) {
        logger.debug("request : {}", cfgRuntimeDictionaryItems);

        try {
            ControllerHelper.setDefaultValue(cfgRuntimeDictionaryItems, "id");
            int result = iCfgRuntimeDictionaryItemsSV.delete(cfgRuntimeDictionaryItems);
            if(result > 0) {
                return ResultData.success(cfgRuntimeDictionaryItems);
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
 	
	public ICfgRuntimeDictionaryItemsSV getICfgRuntimeDictionaryItemsSV(){
		return iCfgRuntimeDictionaryItemsSV;
	}
	//setter
	public void setICfgRuntimeDictionaryItemsSV(ICfgRuntimeDictionaryItemsSV iCfgRuntimeDictionaryItemsSV){
    	this.iCfgRuntimeDictionaryItemsSV = iCfgRuntimeDictionaryItemsSV;
    }
}
