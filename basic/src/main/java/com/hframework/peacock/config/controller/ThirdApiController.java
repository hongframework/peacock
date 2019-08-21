package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.ThirdApi;
import com.hframework.peacock.config.domain.model.ThirdApi_Example;
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
import com.hframework.peacock.config.service.interfaces.IThirdApiSV;

@Controller
@RequestMapping(value = "/config/thirdApi")
public class ThirdApiController   {
    private static final Logger logger = LoggerFactory.getLogger(ThirdApiController.class);

	@Resource
	private IThirdApiSV iThirdApiSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示访问API列表
     * @param thirdApi
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("thirdApi") ThirdApi thirdApi,
                           @ModelAttribute("example") ThirdApi_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", thirdApi, example, pagination);
        try{
            ExampleUtils.parseExample(thirdApi, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< ThirdApi> list = iThirdApiSV.getThirdApiListByExample(example);
            pagination.setTotalCount(iThirdApiSV.getThirdApiCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示访问API明细
     * @param thirdApi
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("thirdApi") ThirdApi thirdApi){
        logger.debug("request : {},{}", thirdApi.getId(), thirdApi);
        try{
            ThirdApi result = null;
            if(thirdApi.getId() != null) {
                result = iThirdApiSV.getThirdApiByPK(thirdApi.getId());
            }else {
                ThirdApi_Example example = ExampleUtils.parseExample(thirdApi, ThirdApi_Example.class);
                List<ThirdApi> list = iThirdApiSV.getThirdApiListByExample(example);
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
    * 搜索一个访问API
    * @param  thirdApi
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" thirdApi") ThirdApi  thirdApi){
        logger.debug("request : {}",  thirdApi);
        try{
            ThirdApi result = null;
            if(thirdApi.getId() != null) {
                result =  iThirdApiSV.getThirdApiByPK(thirdApi.getId());
            }else {
                ThirdApi_Example example = ExampleUtils.parseExample( thirdApi, ThirdApi_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<ThirdApi> list =  iThirdApiSV.getThirdApiListByExample(example);
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
    * 创建访问API
    * @param thirdApi
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("thirdApi") ThirdApi thirdApi) {
        logger.debug("request : {}", thirdApi);
        try {
            ControllerHelper.setDefaultValue(thirdApi, "id");
            int result = iThirdApiSV.create(thirdApi);
            if(result > 0) {
                return ResultData.success(thirdApi);
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
    * 批量维护访问API
    * @param thirdApis
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody ThirdApi[] thirdApis) {
        logger.debug("request : {}", thirdApis);

        try {
            ControllerHelper.setDefaultValue(thirdApis, "id");
            ControllerHelper.reorderProperty(thirdApis);

            int result = iThirdApiSV.batchOperate(thirdApis);
            if(result > 0) {
                return ResultData.success(thirdApis);
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
    * 更新访问API
    * @param thirdApi
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("thirdApi") ThirdApi thirdApi) {
        logger.debug("request : {}", thirdApi);
        try {
            ControllerHelper.setDefaultValue(thirdApi, "id");
            int result = iThirdApiSV.update(thirdApi);
            if(result > 0) {
                return ResultData.success(thirdApi);
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
    * 创建或更新访问API
    * @param thirdApi
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("thirdApi") ThirdApi thirdApi) {
        logger.debug("request : {}", thirdApi);
        try {
            ControllerHelper.setDefaultValue(thirdApi, "id");
            int result = iThirdApiSV.batchOperate(new ThirdApi[]{thirdApi});
            if(result > 0) {
                return ResultData.success(thirdApi);
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
    * 删除访问API
    * @param thirdApi
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("thirdApi") ThirdApi thirdApi) {
        logger.debug("request : {}", thirdApi);

        try {
            ControllerHelper.setDefaultValue(thirdApi, "id");
            int result = iThirdApiSV.delete(thirdApi);
            if(result > 0) {
                return ResultData.success(thirdApi);
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
 	
	public IThirdApiSV getIThirdApiSV(){
		return iThirdApiSV;
	}
	//setter
	public void setIThirdApiSV(IThirdApiSV iThirdApiSV){
    	this.iThirdApiSV = iThirdApiSV;
    }
}
