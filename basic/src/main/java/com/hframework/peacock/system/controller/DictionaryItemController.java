package com.hframework.peacock.system.controller;

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
import com.hframework.peacock.system.domain.model.DictionaryItem;
import com.hframework.peacock.system.domain.model.DictionaryItem_Example;
import com.hframework.peacock.system.service.interfaces.IDictionaryItemSV;

@Controller
@RequestMapping(value = "/system/dictionaryItem")
public class DictionaryItemController   {
    private static final Logger logger = LoggerFactory.getLogger(DictionaryItemController.class);

	@Resource
	private IDictionaryItemSV iDictionaryItemSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示瀛楀吀椤�列表
     * @param dictionaryItem
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("dictionaryItem") DictionaryItem dictionaryItem,
                                      @ModelAttribute("example") DictionaryItem_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", dictionaryItem, example, pagination);
        try{
            ExampleUtils.parseExample(dictionaryItem, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< DictionaryItem> list = iDictionaryItemSV.getDictionaryItemListByExample(example);
            pagination.setTotalCount(iDictionaryItemSV.getDictionaryItemCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示瀛楀吀椤�明细
     * @param dictionaryItem
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("dictionaryItem") DictionaryItem dictionaryItem){
        logger.debug("request : {},{}", dictionaryItem.getDictionaryItemId(), dictionaryItem);
        try{
            DictionaryItem result = null;
            if(dictionaryItem.getDictionaryItemId() != null) {
                result = iDictionaryItemSV.getDictionaryItemByPK(dictionaryItem.getDictionaryItemId());
            }else {
                DictionaryItem_Example example = ExampleUtils.parseExample(dictionaryItem, DictionaryItem_Example.class);
                List<DictionaryItem> list = iDictionaryItemSV.getDictionaryItemListByExample(example);
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
    * 搜索一个瀛楀吀椤�
    * @param  dictionaryItem
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" dictionaryItem") DictionaryItem  dictionaryItem){
        logger.debug("request : {}",  dictionaryItem);
        try{
            DictionaryItem result = null;
            if(dictionaryItem.getDictionaryItemId() != null) {
                result =  iDictionaryItemSV.getDictionaryItemByPK(dictionaryItem.getDictionaryItemId());
            }else {
                DictionaryItem_Example example = ExampleUtils.parseExample( dictionaryItem, DictionaryItem_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<DictionaryItem> list =  iDictionaryItemSV.getDictionaryItemListByExample(example);
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
    * 创建瀛楀吀椤�
    * @param dictionaryItem
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("dictionaryItem") DictionaryItem dictionaryItem) {
        logger.debug("request : {}", dictionaryItem);
        try {
            ControllerHelper.setDefaultValue(dictionaryItem, "dictionaryItemId");
            int result = iDictionaryItemSV.create(dictionaryItem);
            if(result > 0) {
                return ResultData.success(dictionaryItem);
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
    * 批量维护瀛楀吀椤�
    * @param dictionaryItems
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody DictionaryItem[] dictionaryItems) {
        logger.debug("request : {}", dictionaryItems);

        try {
            ControllerHelper.setDefaultValue(dictionaryItems, "dictionaryItemId");
            ControllerHelper.reorderProperty(dictionaryItems);

            int result = iDictionaryItemSV.batchOperate(dictionaryItems);
            if(result > 0) {
                return ResultData.success(dictionaryItems);
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
    * 更新瀛楀吀椤�
    * @param dictionaryItem
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("dictionaryItem") DictionaryItem dictionaryItem) {
        logger.debug("request : {}", dictionaryItem);
        try {
            ControllerHelper.setDefaultValue(dictionaryItem, "dictionaryItemId");
            int result = iDictionaryItemSV.update(dictionaryItem);
            if(result > 0) {
                return ResultData.success(dictionaryItem);
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
    * 创建或更新瀛楀吀椤�
    * @param dictionaryItem
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("dictionaryItem") DictionaryItem dictionaryItem) {
        logger.debug("request : {}", dictionaryItem);
        try {
            ControllerHelper.setDefaultValue(dictionaryItem, "dictionaryItemId");
            int result = iDictionaryItemSV.batchOperate(new DictionaryItem[]{dictionaryItem});
            if(result > 0) {
                return ResultData.success(dictionaryItem);
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
    * 删除瀛楀吀椤�
    * @param dictionaryItem
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("dictionaryItem") DictionaryItem dictionaryItem) {
        logger.debug("request : {}", dictionaryItem);

        try {
            ControllerHelper.setDefaultValue(dictionaryItem, "dictionaryItemId");
            int result = iDictionaryItemSV.delete(dictionaryItem);
            if(result > 0) {
                return ResultData.success(dictionaryItem);
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
 	
	public IDictionaryItemSV getIDictionaryItemSV(){
		return iDictionaryItemSV;
	}
	//setter
	public void setIDictionaryItemSV(IDictionaryItemSV iDictionaryItemSV){
    	this.iDictionaryItemSV = iDictionaryItemSV;
    }
}
