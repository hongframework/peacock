package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.ThirdPubRequest;
import com.hframework.peacock.config.domain.model.ThirdPubRequest_Example;
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
import com.hframework.peacock.config.service.interfaces.IThirdPubRequestSV;

@Controller
@RequestMapping(value = "/config/thirdPubRequest")
public class ThirdPubRequestController   {
    private static final Logger logger = LoggerFactory.getLogger(ThirdPubRequestController.class);

	@Resource
	private IThirdPubRequestSV iThirdPubRequestSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示公共请求参数列表
     * @param thirdPubRequest
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("thirdPubRequest") ThirdPubRequest thirdPubRequest,
                           @ModelAttribute("example") ThirdPubRequest_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", thirdPubRequest, example, pagination);
        try{
            ExampleUtils.parseExample(thirdPubRequest, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< ThirdPubRequest> list = iThirdPubRequestSV.getThirdPubRequestListByExample(example);
            pagination.setTotalCount(iThirdPubRequestSV.getThirdPubRequestCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示公共请求参数明细
     * @param thirdPubRequest
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("thirdPubRequest") ThirdPubRequest thirdPubRequest){
        logger.debug("request : {},{}", thirdPubRequest.getId(), thirdPubRequest);
        try{
            ThirdPubRequest result = null;
            if(thirdPubRequest.getId() != null) {
                result = iThirdPubRequestSV.getThirdPubRequestByPK(thirdPubRequest.getId());
            }else {
                ThirdPubRequest_Example example = ExampleUtils.parseExample(thirdPubRequest, ThirdPubRequest_Example.class);
                List<ThirdPubRequest> list = iThirdPubRequestSV.getThirdPubRequestListByExample(example);
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
    * 搜索一个公共请求参数
    * @param  thirdPubRequest
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" thirdPubRequest") ThirdPubRequest  thirdPubRequest){
        logger.debug("request : {}",  thirdPubRequest);
        try{
            ThirdPubRequest result = null;
            if(thirdPubRequest.getId() != null) {
                result =  iThirdPubRequestSV.getThirdPubRequestByPK(thirdPubRequest.getId());
            }else {
                ThirdPubRequest_Example example = ExampleUtils.parseExample( thirdPubRequest, ThirdPubRequest_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<ThirdPubRequest> list =  iThirdPubRequestSV.getThirdPubRequestListByExample(example);
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
    * 创建公共请求参数
    * @param thirdPubRequest
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("thirdPubRequest") ThirdPubRequest thirdPubRequest) {
        logger.debug("request : {}", thirdPubRequest);
        try {
            ControllerHelper.setDefaultValue(thirdPubRequest, "id");
            int result = iThirdPubRequestSV.create(thirdPubRequest);
            if(result > 0) {
                return ResultData.success(thirdPubRequest);
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
    * 批量维护公共请求参数
    * @param thirdPubRequests
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody ThirdPubRequest[] thirdPubRequests) {
        logger.debug("request : {}", thirdPubRequests);

        try {
            ControllerHelper.setDefaultValue(thirdPubRequests, "id");
            ControllerHelper.reorderProperty(thirdPubRequests);

            int result = iThirdPubRequestSV.batchOperate(thirdPubRequests);
            if(result > 0) {
                return ResultData.success(thirdPubRequests);
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
    * 更新公共请求参数
    * @param thirdPubRequest
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("thirdPubRequest") ThirdPubRequest thirdPubRequest) {
        logger.debug("request : {}", thirdPubRequest);
        try {
            ControllerHelper.setDefaultValue(thirdPubRequest, "id");
            int result = iThirdPubRequestSV.update(thirdPubRequest);
            if(result > 0) {
                return ResultData.success(thirdPubRequest);
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
    * 创建或更新公共请求参数
    * @param thirdPubRequest
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("thirdPubRequest") ThirdPubRequest thirdPubRequest) {
        logger.debug("request : {}", thirdPubRequest);
        try {
            ControllerHelper.setDefaultValue(thirdPubRequest, "id");
            int result = iThirdPubRequestSV.batchOperate(new ThirdPubRequest[]{thirdPubRequest});
            if(result > 0) {
                return ResultData.success(thirdPubRequest);
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
    * 删除公共请求参数
    * @param thirdPubRequest
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("thirdPubRequest") ThirdPubRequest thirdPubRequest) {
        logger.debug("request : {}", thirdPubRequest);

        try {
            ControllerHelper.setDefaultValue(thirdPubRequest, "id");
            int result = iThirdPubRequestSV.delete(thirdPubRequest);
            if(result > 0) {
                return ResultData.success(thirdPubRequest);
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
 	
	public IThirdPubRequestSV getIThirdPubRequestSV(){
		return iThirdPubRequestSV;
	}
	//setter
	public void setIThirdPubRequestSV(IThirdPubRequestSV iThirdPubRequestSV){
    	this.iThirdPubRequestSV = iThirdPubRequestSV;
    }
}
