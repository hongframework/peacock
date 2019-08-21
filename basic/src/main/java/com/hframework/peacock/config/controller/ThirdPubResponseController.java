package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.ThirdPubResponse;
import com.hframework.peacock.config.domain.model.ThirdPubResponse_Example;
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
import com.hframework.peacock.config.service.interfaces.IThirdPubResponseSV;

@Controller
@RequestMapping(value = "/config/thirdPubResponse")
public class ThirdPubResponseController   {
    private static final Logger logger = LoggerFactory.getLogger(ThirdPubResponseController.class);

	@Resource
	private IThirdPubResponseSV iThirdPubResponseSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示公共响应参数列表
     * @param thirdPubResponse
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("thirdPubResponse") ThirdPubResponse thirdPubResponse,
                           @ModelAttribute("example") ThirdPubResponse_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", thirdPubResponse, example, pagination);
        try{
            ExampleUtils.parseExample(thirdPubResponse, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< ThirdPubResponse> list = iThirdPubResponseSV.getThirdPubResponseListByExample(example);
            pagination.setTotalCount(iThirdPubResponseSV.getThirdPubResponseCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示公共响应参数明细
     * @param thirdPubResponse
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("thirdPubResponse") ThirdPubResponse thirdPubResponse){
        logger.debug("request : {},{}", thirdPubResponse.getId(), thirdPubResponse);
        try{
            ThirdPubResponse result = null;
            if(thirdPubResponse.getId() != null) {
                result = iThirdPubResponseSV.getThirdPubResponseByPK(thirdPubResponse.getId());
            }else {
                ThirdPubResponse_Example example = ExampleUtils.parseExample(thirdPubResponse, ThirdPubResponse_Example.class);
                List<ThirdPubResponse> list = iThirdPubResponseSV.getThirdPubResponseListByExample(example);
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
    * 搜索一个公共响应参数
    * @param  thirdPubResponse
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" thirdPubResponse") ThirdPubResponse  thirdPubResponse){
        logger.debug("request : {}",  thirdPubResponse);
        try{
            ThirdPubResponse result = null;
            if(thirdPubResponse.getId() != null) {
                result =  iThirdPubResponseSV.getThirdPubResponseByPK(thirdPubResponse.getId());
            }else {
                ThirdPubResponse_Example example = ExampleUtils.parseExample( thirdPubResponse, ThirdPubResponse_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<ThirdPubResponse> list =  iThirdPubResponseSV.getThirdPubResponseListByExample(example);
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
    * 创建公共响应参数
    * @param thirdPubResponse
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("thirdPubResponse") ThirdPubResponse thirdPubResponse) {
        logger.debug("request : {}", thirdPubResponse);
        try {
            ControllerHelper.setDefaultValue(thirdPubResponse, "id");
            int result = iThirdPubResponseSV.create(thirdPubResponse);
            if(result > 0) {
                return ResultData.success(thirdPubResponse);
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
    * 批量维护公共响应参数
    * @param thirdPubResponses
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody ThirdPubResponse[] thirdPubResponses) {
        logger.debug("request : {}", thirdPubResponses);

        try {
            ControllerHelper.setDefaultValue(thirdPubResponses, "id");
            ControllerHelper.reorderProperty(thirdPubResponses);

            int result = iThirdPubResponseSV.batchOperate(thirdPubResponses);
            if(result > 0) {
                return ResultData.success(thirdPubResponses);
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
    * 更新公共响应参数
    * @param thirdPubResponse
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("thirdPubResponse") ThirdPubResponse thirdPubResponse) {
        logger.debug("request : {}", thirdPubResponse);
        try {
            ControllerHelper.setDefaultValue(thirdPubResponse, "id");
            int result = iThirdPubResponseSV.update(thirdPubResponse);
            if(result > 0) {
                return ResultData.success(thirdPubResponse);
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
    * 创建或更新公共响应参数
    * @param thirdPubResponse
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("thirdPubResponse") ThirdPubResponse thirdPubResponse) {
        logger.debug("request : {}", thirdPubResponse);
        try {
            ControllerHelper.setDefaultValue(thirdPubResponse, "id");
            int result = iThirdPubResponseSV.batchOperate(new ThirdPubResponse[]{thirdPubResponse});
            if(result > 0) {
                return ResultData.success(thirdPubResponse);
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
    * 删除公共响应参数
    * @param thirdPubResponse
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("thirdPubResponse") ThirdPubResponse thirdPubResponse) {
        logger.debug("request : {}", thirdPubResponse);

        try {
            ControllerHelper.setDefaultValue(thirdPubResponse, "id");
            int result = iThirdPubResponseSV.delete(thirdPubResponse);
            if(result > 0) {
                return ResultData.success(thirdPubResponse);
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
 	
	public IThirdPubResponseSV getIThirdPubResponseSV(){
		return iThirdPubResponseSV;
	}
	//setter
	public void setIThirdPubResponseSV(IThirdPubResponseSV iThirdPubResponseSV){
    	this.iThirdPubResponseSV = iThirdPubResponseSV;
    }
}
