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
import com.hframework.peacock.system.domain.model.User;
import com.hframework.peacock.system.domain.model.User_Example;
import com.hframework.peacock.system.service.interfaces.IUserSV;

@Controller
@RequestMapping(value = "/system/user")
public class UserController   {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Resource
	private IUserSV iUserSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示鐢ㄦ埛列表
     * @param user
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("user") User user,
                                      @ModelAttribute("example") User_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", user, example, pagination);
        try{
            ExampleUtils.parseExample(user, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< User> list = iUserSV.getUserListByExample(example);
            pagination.setTotalCount(iUserSV.getUserCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示鐢ㄦ埛明细
     * @param user
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("user") User user){
        logger.debug("request : {},{}", user.getUserId(), user);
        try{
            User result = null;
            if(user.getUserId() != null) {
                result = iUserSV.getUserByPK(user.getUserId());
            }else {
                User_Example example = ExampleUtils.parseExample(user, User_Example.class);
                List<User> list = iUserSV.getUserListByExample(example);
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
    * 搜索一个鐢ㄦ埛
    * @param  user
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" user") User  user){
        logger.debug("request : {}",  user);
        try{
            User result = null;
            if(user.getUserId() != null) {
                result =  iUserSV.getUserByPK(user.getUserId());
            }else {
                User_Example example = ExampleUtils.parseExample( user, User_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<User> list =  iUserSV.getUserListByExample(example);
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
    * 创建鐢ㄦ埛
    * @param user
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("user") User user) {
        logger.debug("request : {}", user);
        try {
            ControllerHelper.setDefaultValue(user, "userId");
            int result = iUserSV.create(user);
            if(result > 0) {
                return ResultData.success(user);
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
    * 批量维护鐢ㄦ埛
    * @param users
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody User[] users) {
        logger.debug("request : {}", users);

        try {
            ControllerHelper.setDefaultValue(users, "userId");
            ControllerHelper.reorderProperty(users);

            int result = iUserSV.batchOperate(users);
            if(result > 0) {
                return ResultData.success(users);
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
    * 更新鐢ㄦ埛
    * @param user
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("user") User user) {
        logger.debug("request : {}", user);
        try {
            ControllerHelper.setDefaultValue(user, "userId");
            int result = iUserSV.update(user);
            if(result > 0) {
                return ResultData.success(user);
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
    * 创建或更新鐢ㄦ埛
    * @param user
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("user") User user) {
        logger.debug("request : {}", user);
        try {
            ControllerHelper.setDefaultValue(user, "userId");
            int result = iUserSV.batchOperate(new User[]{user});
            if(result > 0) {
                return ResultData.success(user);
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
    * 删除鐢ㄦ埛
    * @param user
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("user") User user) {
        logger.debug("request : {}", user);

        try {
            ControllerHelper.setDefaultValue(user, "userId");
            int result = iUserSV.delete(user);
            if(result > 0) {
                return ResultData.success(user);
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
 	
	public IUserSV getIUserSV(){
		return iUserSV;
	}
	//setter
	public void setIUserSV(IUserSV iUserSV){
    	this.iUserSV = iUserSV;
    }
}
