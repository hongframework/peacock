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
import com.hframework.peacock.system.domain.model.Dictionary;
import com.hframework.peacock.system.domain.model.Dictionary_Example;
import com.hframework.peacock.system.service.interfaces.IDictionarySV;

@Controller
@RequestMapping(value = "/system/dictionary")
public class DictionaryController   {
    private static final Logger logger = LoggerFactory.getLogger(DictionaryController.class);

	@Resource
	private IDictionarySV iDictionarySV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示瀛楀吀列表
     * @param dictionary
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("dictionary") Dictionary dictionary,
                                      @ModelAttribute("example") Dictionary_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", dictionary, example, pagination);
        try{
            ExampleUtils.parseExample(dictionary, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< Dictionary> list = iDictionarySV.getDictionaryListByExample(example);
            pagination.setTotalCount(iDictionarySV.getDictionaryCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示瀛楀吀明细
     * @param dictionary
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("dictionary") Dictionary dictionary){
        logger.debug("request : {},{}", dictionary.getDictionaryId(), dictionary);
        try{
            Dictionary result = null;
            if(dictionary.getDictionaryId() != null) {
                result = iDictionarySV.getDictionaryByPK(dictionary.getDictionaryId());
            }else {
                Dictionary_Example example = ExampleUtils.parseExample(dictionary, Dictionary_Example.class);
                List<Dictionary> list = iDictionarySV.getDictionaryListByExample(example);
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
    * 搜索一个瀛楀吀
    * @param  dictionary
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" dictionary") Dictionary  dictionary){
        logger.debug("request : {}",  dictionary);
        try{
            Dictionary result = null;
            if(dictionary.getDictionaryId() != null) {
                result =  iDictionarySV.getDictionaryByPK(dictionary.getDictionaryId());
            }else {
                Dictionary_Example example = ExampleUtils.parseExample( dictionary, Dictionary_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<Dictionary> list =  iDictionarySV.getDictionaryListByExample(example);
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
    * 创建瀛楀吀
    * @param dictionary
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("dictionary") Dictionary dictionary) {
        logger.debug("request : {}", dictionary);
        try {
            ControllerHelper.setDefaultValue(dictionary, "dictionaryId");
            int result = iDictionarySV.create(dictionary);
            if(result > 0) {
                return ResultData.success(dictionary);
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
    * 批量维护瀛楀吀
    * @param dictionarys
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody Dictionary[] dictionarys) {
        logger.debug("request : {}", dictionarys);

        try {
            ControllerHelper.setDefaultValue(dictionarys, "dictionaryId");
            ControllerHelper.reorderProperty(dictionarys);

            int result = iDictionarySV.batchOperate(dictionarys);
            if(result > 0) {
                return ResultData.success(dictionarys);
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
    * 更新瀛楀吀
    * @param dictionary
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("dictionary") Dictionary dictionary) {
        logger.debug("request : {}", dictionary);
        try {
            ControllerHelper.setDefaultValue(dictionary, "dictionaryId");
            int result = iDictionarySV.update(dictionary);
            if(result > 0) {
                return ResultData.success(dictionary);
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
    * 创建或更新瀛楀吀
    * @param dictionary
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("dictionary") Dictionary dictionary) {
        logger.debug("request : {}", dictionary);
        try {
            ControllerHelper.setDefaultValue(dictionary, "dictionaryId");
            int result = iDictionarySV.batchOperate(new Dictionary[]{dictionary});
            if(result > 0) {
                return ResultData.success(dictionary);
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
    * 删除瀛楀吀
    * @param dictionary
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("dictionary") Dictionary dictionary) {
        logger.debug("request : {}", dictionary);

        try {
            ControllerHelper.setDefaultValue(dictionary, "dictionaryId");
            int result = iDictionarySV.delete(dictionary);
            if(result > 0) {
                return ResultData.success(dictionary);
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
 	
	public IDictionarySV getIDictionarySV(){
		return iDictionarySV;
	}
	//setter
	public void setIDictionarySV(IDictionarySV iDictionarySV){
    	this.iDictionarySV = iDictionarySV;
    }
}
