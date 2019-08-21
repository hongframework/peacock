package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.ThirdHelpParameter;
import com.hframework.peacock.config.domain.model.ThirdHelpParameter_Example;
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
import com.hframework.peacock.config.service.interfaces.IThirdHelpParameterSV;

@Controller
@RequestMapping(value = "/config/thirdHelpParameter")
public class ThirdHelpParameterController   {
    private static final Logger logger = LoggerFactory.getLogger(ThirdHelpParameterController.class);

	@Resource
	private IThirdHelpParameterSV iThirdHelpParameterSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示访问域常规参数列表
     * @param thirdHelpParameter
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("thirdHelpParameter") ThirdHelpParameter thirdHelpParameter,
                           @ModelAttribute("example") ThirdHelpParameter_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", thirdHelpParameter, example, pagination);
        try{
            ExampleUtils.parseExample(thirdHelpParameter, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< ThirdHelpParameter> list = iThirdHelpParameterSV.getThirdHelpParameterListByExample(example);
            pagination.setTotalCount(iThirdHelpParameterSV.getThirdHelpParameterCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示访问域常规参数明细
     * @param thirdHelpParameter
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("thirdHelpParameter") ThirdHelpParameter thirdHelpParameter){
        logger.debug("request : {},{}", thirdHelpParameter.getId(), thirdHelpParameter);
        try{
            ThirdHelpParameter result = null;
            if(thirdHelpParameter.getId() != null) {
                result = iThirdHelpParameterSV.getThirdHelpParameterByPK(thirdHelpParameter.getId());
            }else {
                ThirdHelpParameter_Example example = ExampleUtils.parseExample(thirdHelpParameter, ThirdHelpParameter_Example.class);
                List<ThirdHelpParameter> list = iThirdHelpParameterSV.getThirdHelpParameterListByExample(example);
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
    * 搜索一个访问域常规参数
    * @param  thirdHelpParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" thirdHelpParameter") ThirdHelpParameter  thirdHelpParameter){
        logger.debug("request : {}",  thirdHelpParameter);
        try{
            ThirdHelpParameter result = null;
            if(thirdHelpParameter.getId() != null) {
                result =  iThirdHelpParameterSV.getThirdHelpParameterByPK(thirdHelpParameter.getId());
            }else {
                ThirdHelpParameter_Example example = ExampleUtils.parseExample( thirdHelpParameter, ThirdHelpParameter_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<ThirdHelpParameter> list =  iThirdHelpParameterSV.getThirdHelpParameterListByExample(example);
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
    * 创建访问域常规参数
    * @param thirdHelpParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("thirdHelpParameter") ThirdHelpParameter thirdHelpParameter) {
        logger.debug("request : {}", thirdHelpParameter);
        try {
            ControllerHelper.setDefaultValue(thirdHelpParameter, "id");
            int result = iThirdHelpParameterSV.create(thirdHelpParameter);
            if(result > 0) {
                return ResultData.success(thirdHelpParameter);
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
    * 批量维护访问域常规参数
    * @param thirdHelpParameters
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody ThirdHelpParameter[] thirdHelpParameters) {
        logger.debug("request : {}", thirdHelpParameters);

        try {
            ControllerHelper.setDefaultValue(thirdHelpParameters, "id");
            ControllerHelper.reorderProperty(thirdHelpParameters);

            int result = iThirdHelpParameterSV.batchOperate(thirdHelpParameters);
            if(result > 0) {
                return ResultData.success(thirdHelpParameters);
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
    * 更新访问域常规参数
    * @param thirdHelpParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("thirdHelpParameter") ThirdHelpParameter thirdHelpParameter) {
        logger.debug("request : {}", thirdHelpParameter);
        try {
            ControllerHelper.setDefaultValue(thirdHelpParameter, "id");
            int result = iThirdHelpParameterSV.update(thirdHelpParameter);
            if(result > 0) {
                return ResultData.success(thirdHelpParameter);
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
    * 创建或更新访问域常规参数
    * @param thirdHelpParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("thirdHelpParameter") ThirdHelpParameter thirdHelpParameter) {
        logger.debug("request : {}", thirdHelpParameter);
        try {
            ControllerHelper.setDefaultValue(thirdHelpParameter, "id");
            int result = iThirdHelpParameterSV.batchOperate(new ThirdHelpParameter[]{thirdHelpParameter});
            if(result > 0) {
                return ResultData.success(thirdHelpParameter);
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
    * 删除访问域常规参数
    * @param thirdHelpParameter
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("thirdHelpParameter") ThirdHelpParameter thirdHelpParameter) {
        logger.debug("request : {}", thirdHelpParameter);

        try {
            ControllerHelper.setDefaultValue(thirdHelpParameter, "id");
            int result = iThirdHelpParameterSV.delete(thirdHelpParameter);
            if(result > 0) {
                return ResultData.success(thirdHelpParameter);
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
 	
	public IThirdHelpParameterSV getIThirdHelpParameterSV(){
		return iThirdHelpParameterSV;
	}
	//setter
	public void setIThirdHelpParameterSV(IThirdHelpParameterSV iThirdHelpParameterSV){
    	this.iThirdHelpParameterSV = iThirdHelpParameterSV;
    }
}
