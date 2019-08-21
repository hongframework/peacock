package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.ThirdDomainParameter;
import com.hframework.peacock.config.domain.model.ThirdDomainParameter_Example;
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
import com.hframework.peacock.config.service.interfaces.IThirdDomainParameterSV;

@Controller
@RequestMapping(value = "/config/thirdDomainParameter")
public class ThirdDomainParameterController   {
    private static final Logger logger = LoggerFactory.getLogger(ThirdDomainParameterController.class);

	@Resource
	private IThirdDomainParameterSV iThirdDomainParameterSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示访问域参数列表
     * @param thirdDomainParameter
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("thirdDomainParameter") ThirdDomainParameter thirdDomainParameter,
                           @ModelAttribute("example") ThirdDomainParameter_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", thirdDomainParameter, example, pagination);
        try{
            ExampleUtils.parseExample(thirdDomainParameter, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< ThirdDomainParameter> list = iThirdDomainParameterSV.getThirdDomainParameterListByExample(example);
            pagination.setTotalCount(iThirdDomainParameterSV.getThirdDomainParameterCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示访问域参数明细
     * @param thirdDomainParameter
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("thirdDomainParameter") ThirdDomainParameter thirdDomainParameter){
        logger.debug("request : {},{}", thirdDomainParameter.getId(), thirdDomainParameter);
        try{
            ThirdDomainParameter result = null;
            if(thirdDomainParameter.getId() != null) {
                result = iThirdDomainParameterSV.getThirdDomainParameterByPK(thirdDomainParameter.getId());
            }else {
                ThirdDomainParameter_Example example = ExampleUtils.parseExample(thirdDomainParameter, ThirdDomainParameter_Example.class);
                List<ThirdDomainParameter> list = iThirdDomainParameterSV.getThirdDomainParameterListByExample(example);
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
    * 搜索一个访问域参数
    * @param  thirdDomainParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" thirdDomainParameter") ThirdDomainParameter  thirdDomainParameter){
        logger.debug("request : {}",  thirdDomainParameter);
        try{
            ThirdDomainParameter result = null;
            if(thirdDomainParameter.getId() != null) {
                result =  iThirdDomainParameterSV.getThirdDomainParameterByPK(thirdDomainParameter.getId());
            }else {
                ThirdDomainParameter_Example example = ExampleUtils.parseExample( thirdDomainParameter, ThirdDomainParameter_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<ThirdDomainParameter> list =  iThirdDomainParameterSV.getThirdDomainParameterListByExample(example);
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
    * 创建访问域参数
    * @param thirdDomainParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("thirdDomainParameter") ThirdDomainParameter thirdDomainParameter) {
        logger.debug("request : {}", thirdDomainParameter);
        try {
            ControllerHelper.setDefaultValue(thirdDomainParameter, "id");
            int result = iThirdDomainParameterSV.create(thirdDomainParameter);
            if(result > 0) {
                return ResultData.success(thirdDomainParameter);
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
    * 批量维护访问域参数
    * @param thirdDomainParameters
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody ThirdDomainParameter[] thirdDomainParameters) {
        logger.debug("request : {}", thirdDomainParameters);

        try {
            ControllerHelper.setDefaultValue(thirdDomainParameters, "id");
            ControllerHelper.reorderProperty(thirdDomainParameters);

            int result = iThirdDomainParameterSV.batchOperate(thirdDomainParameters);
            if(result > 0) {
                return ResultData.success(thirdDomainParameters);
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
    * 更新访问域参数
    * @param thirdDomainParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("thirdDomainParameter") ThirdDomainParameter thirdDomainParameter) {
        logger.debug("request : {}", thirdDomainParameter);
        try {
            ControllerHelper.setDefaultValue(thirdDomainParameter, "id");
            int result = iThirdDomainParameterSV.update(thirdDomainParameter);
            if(result > 0) {
                return ResultData.success(thirdDomainParameter);
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
    * 创建或更新访问域参数
    * @param thirdDomainParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("thirdDomainParameter") ThirdDomainParameter thirdDomainParameter) {
        logger.debug("request : {}", thirdDomainParameter);
        try {
            ControllerHelper.setDefaultValue(thirdDomainParameter, "id");
            int result = iThirdDomainParameterSV.batchOperate(new ThirdDomainParameter[]{thirdDomainParameter});
            if(result > 0) {
                return ResultData.success(thirdDomainParameter);
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
    * 删除访问域参数
    * @param thirdDomainParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("thirdDomainParameter") ThirdDomainParameter thirdDomainParameter) {
        logger.debug("request : {}", thirdDomainParameter);

        try {
            ControllerHelper.setDefaultValue(thirdDomainParameter, "id");
            int result = iThirdDomainParameterSV.delete(thirdDomainParameter);
            if(result > 0) {
                return ResultData.success(thirdDomainParameter);
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
 	
	public IThirdDomainParameterSV getIThirdDomainParameterSV(){
		return iThirdDomainParameterSV;
	}
	//setter
	public void setIThirdDomainParameterSV(IThirdDomainParameterSV iThirdDomainParameterSV){
    	this.iThirdDomainParameterSV = iThirdDomainParameterSV;
    }
}
