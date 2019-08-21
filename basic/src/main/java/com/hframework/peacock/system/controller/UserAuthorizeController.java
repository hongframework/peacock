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
import com.hframework.peacock.system.domain.model.UserAuthorize;
import com.hframework.peacock.system.domain.model.UserAuthorize_Example;
import com.hframework.peacock.system.service.interfaces.IUserAuthorizeSV;

@Controller
@RequestMapping(value = "/system/userAuthorize")
public class UserAuthorizeController   {
    private static final Logger logger = LoggerFactory.getLogger(UserAuthorizeController.class);

	@Resource
	private IUserAuthorizeSV iUserAuthorizeSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示鐢ㄦ埛鎺堟潈列表
     * @param userAuthorize
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("userAuthorize") UserAuthorize userAuthorize,
                                      @ModelAttribute("example") UserAuthorize_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", userAuthorize, example, pagination);
        try{
            ExampleUtils.parseExample(userAuthorize, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< UserAuthorize> list = iUserAuthorizeSV.getUserAuthorizeListByExample(example);
            pagination.setTotalCount(iUserAuthorizeSV.getUserAuthorizeCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示鐢ㄦ埛鎺堟潈明细
     * @param userAuthorize
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("userAuthorize") UserAuthorize userAuthorize){
        logger.debug("request : {},{}", userAuthorize.getUserAuthorizeId(), userAuthorize);
        try{
            UserAuthorize result = null;
            if(userAuthorize.getUserAuthorizeId() != null) {
                result = iUserAuthorizeSV.getUserAuthorizeByPK(userAuthorize.getUserAuthorizeId());
            }else {
                UserAuthorize_Example example = ExampleUtils.parseExample(userAuthorize, UserAuthorize_Example.class);
                List<UserAuthorize> list = iUserAuthorizeSV.getUserAuthorizeListByExample(example);
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
    * 搜索一个鐢ㄦ埛鎺堟潈
    * @param  userAuthorize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" userAuthorize") UserAuthorize  userAuthorize){
        logger.debug("request : {}",  userAuthorize);
        try{
            UserAuthorize result = null;
            if(userAuthorize.getUserAuthorizeId() != null) {
                result =  iUserAuthorizeSV.getUserAuthorizeByPK(userAuthorize.getUserAuthorizeId());
            }else {
                UserAuthorize_Example example = ExampleUtils.parseExample( userAuthorize, UserAuthorize_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<UserAuthorize> list =  iUserAuthorizeSV.getUserAuthorizeListByExample(example);
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
    * 创建鐢ㄦ埛鎺堟潈
    * @param userAuthorize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("userAuthorize") UserAuthorize userAuthorize) {
        logger.debug("request : {}", userAuthorize);
        try {
            ControllerHelper.setDefaultValue(userAuthorize, "userAuthorizeId");
            int result = iUserAuthorizeSV.create(userAuthorize);
            if(result > 0) {
                return ResultData.success(userAuthorize);
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
    * 批量维护鐢ㄦ埛鎺堟潈
    * @param userAuthorizes
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody UserAuthorize[] userAuthorizes) {
        logger.debug("request : {}", userAuthorizes);

        try {
            ControllerHelper.setDefaultValue(userAuthorizes, "userAuthorizeId");
            ControllerHelper.reorderProperty(userAuthorizes);

            int result = iUserAuthorizeSV.batchOperate(userAuthorizes);
            if(result > 0) {
                return ResultData.success(userAuthorizes);
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
    * 更新鐢ㄦ埛鎺堟潈
    * @param userAuthorize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("userAuthorize") UserAuthorize userAuthorize) {
        logger.debug("request : {}", userAuthorize);
        try {
            ControllerHelper.setDefaultValue(userAuthorize, "userAuthorizeId");
            int result = iUserAuthorizeSV.update(userAuthorize);
            if(result > 0) {
                return ResultData.success(userAuthorize);
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
    * 创建或更新鐢ㄦ埛鎺堟潈
    * @param userAuthorize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("userAuthorize") UserAuthorize userAuthorize) {
        logger.debug("request : {}", userAuthorize);
        try {
            ControllerHelper.setDefaultValue(userAuthorize, "userAuthorizeId");
            int result = iUserAuthorizeSV.batchOperate(new UserAuthorize[]{userAuthorize});
            if(result > 0) {
                return ResultData.success(userAuthorize);
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
    * 删除鐢ㄦ埛鎺堟潈
    * @param userAuthorize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("userAuthorize") UserAuthorize userAuthorize) {
        logger.debug("request : {}", userAuthorize);

        try {
            ControllerHelper.setDefaultValue(userAuthorize, "userAuthorizeId");
            int result = iUserAuthorizeSV.delete(userAuthorize);
            if(result > 0) {
                return ResultData.success(userAuthorize);
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
 	
	public IUserAuthorizeSV getIUserAuthorizeSV(){
		return iUserAuthorizeSV;
	}
	//setter
	public void setIUserAuthorizeSV(IUserAuthorizeSV iUserAuthorizeSV){
    	this.iUserAuthorizeSV = iUserAuthorizeSV;
    }
}
