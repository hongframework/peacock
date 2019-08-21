package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.ThirdCommonParameter;
import com.hframework.peacock.config.domain.model.ThirdCommonParameter_Example;
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
import com.hframework.peacock.config.service.interfaces.IThirdCommonParameterSV;

@Controller
@RequestMapping(value = "/config/thirdCommonParameter")
public class ThirdCommonParameterController   {
    private static final Logger logger = LoggerFactory.getLogger(ThirdCommonParameterController.class);

	@Resource
	private IThirdCommonParameterSV iThirdCommonParameterSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示域通用参数列表
     * @param thirdCommonParameter
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("thirdCommonParameter") ThirdCommonParameter thirdCommonParameter,
                           @ModelAttribute("example") ThirdCommonParameter_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", thirdCommonParameter, example, pagination);
        try{
            ExampleUtils.parseExample(thirdCommonParameter, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< ThirdCommonParameter> list = iThirdCommonParameterSV.getThirdCommonParameterListByExample(example);
            pagination.setTotalCount(iThirdCommonParameterSV.getThirdCommonParameterCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示域通用参数明细
     * @param thirdCommonParameter
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("thirdCommonParameter") ThirdCommonParameter thirdCommonParameter){
        logger.debug("request : {},{}", thirdCommonParameter.getId(), thirdCommonParameter);
        try{
            ThirdCommonParameter result = null;
            if(thirdCommonParameter.getId() != null) {
                result = iThirdCommonParameterSV.getThirdCommonParameterByPK(thirdCommonParameter.getId());
            }else {
                ThirdCommonParameter_Example example = ExampleUtils.parseExample(thirdCommonParameter, ThirdCommonParameter_Example.class);
                List<ThirdCommonParameter> list = iThirdCommonParameterSV.getThirdCommonParameterListByExample(example);
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
    * 搜索一个域通用参数
    * @param  thirdCommonParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" thirdCommonParameter") ThirdCommonParameter  thirdCommonParameter){
        logger.debug("request : {}",  thirdCommonParameter);
        try{
            ThirdCommonParameter result = null;
            if(thirdCommonParameter.getId() != null) {
                result =  iThirdCommonParameterSV.getThirdCommonParameterByPK(thirdCommonParameter.getId());
            }else {
                ThirdCommonParameter_Example example = ExampleUtils.parseExample( thirdCommonParameter, ThirdCommonParameter_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<ThirdCommonParameter> list =  iThirdCommonParameterSV.getThirdCommonParameterListByExample(example);
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
    * 创建域通用参数
    * @param thirdCommonParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("thirdCommonParameter") ThirdCommonParameter thirdCommonParameter) {
        logger.debug("request : {}", thirdCommonParameter);
        try {
            ControllerHelper.setDefaultValue(thirdCommonParameter, "id");
            int result = iThirdCommonParameterSV.create(thirdCommonParameter);
            if(result > 0) {
                return ResultData.success(thirdCommonParameter);
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
    * 批量维护域通用参数
    * @param thirdCommonParameters
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody ThirdCommonParameter[] thirdCommonParameters) {
        logger.debug("request : {}", thirdCommonParameters);

        try {
            ControllerHelper.setDefaultValue(thirdCommonParameters, "id");
            ControllerHelper.reorderProperty(thirdCommonParameters);

            int result = iThirdCommonParameterSV.batchOperate(thirdCommonParameters);
            if(result > 0) {
                return ResultData.success(thirdCommonParameters);
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
    * 更新域通用参数
    * @param thirdCommonParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("thirdCommonParameter") ThirdCommonParameter thirdCommonParameter) {
        logger.debug("request : {}", thirdCommonParameter);
        try {
            ControllerHelper.setDefaultValue(thirdCommonParameter, "id");
            int result = iThirdCommonParameterSV.update(thirdCommonParameter);
            if(result > 0) {
                return ResultData.success(thirdCommonParameter);
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
    * 创建或更新域通用参数
    * @param thirdCommonParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("thirdCommonParameter") ThirdCommonParameter thirdCommonParameter) {
        logger.debug("request : {}", thirdCommonParameter);
        try {
            ControllerHelper.setDefaultValue(thirdCommonParameter, "id");
            int result = iThirdCommonParameterSV.batchOperate(new ThirdCommonParameter[]{thirdCommonParameter});
            if(result > 0) {
                return ResultData.success(thirdCommonParameter);
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
    * 删除域通用参数
    * @param thirdCommonParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("thirdCommonParameter") ThirdCommonParameter thirdCommonParameter) {
        logger.debug("request : {}", thirdCommonParameter);

        try {
            ControllerHelper.setDefaultValue(thirdCommonParameter, "id");
            int result = iThirdCommonParameterSV.delete(thirdCommonParameter);
            if(result > 0) {
                return ResultData.success(thirdCommonParameter);
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
 	
	public IThirdCommonParameterSV getIThirdCommonParameterSV(){
		return iThirdCommonParameterSV;
	}
	//setter
	public void setIThirdCommonParameterSV(IThirdCommonParameterSV iThirdCommonParameterSV){
    	this.iThirdCommonParameterSV = iThirdCommonParameterSV;
    }
}
