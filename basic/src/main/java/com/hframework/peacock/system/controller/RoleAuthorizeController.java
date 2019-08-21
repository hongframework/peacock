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
import com.hframework.peacock.system.domain.model.RoleAuthorize;
import com.hframework.peacock.system.domain.model.RoleAuthorize_Example;
import com.hframework.peacock.system.service.interfaces.IRoleAuthorizeSV;

@Controller
@RequestMapping(value = "/system/roleAuthorize")
public class RoleAuthorizeController   {
    private static final Logger logger = LoggerFactory.getLogger(RoleAuthorizeController.class);

	@Resource
	private IRoleAuthorizeSV iRoleAuthorizeSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示瑙掕壊鎺堟潈列表
     * @param roleAuthorize
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("roleAuthorize") RoleAuthorize roleAuthorize,
                                      @ModelAttribute("example") RoleAuthorize_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", roleAuthorize, example, pagination);
        try{
            ExampleUtils.parseExample(roleAuthorize, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< RoleAuthorize> list = iRoleAuthorizeSV.getRoleAuthorizeListByExample(example);
            pagination.setTotalCount(iRoleAuthorizeSV.getRoleAuthorizeCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示瑙掕壊鎺堟潈明细
     * @param roleAuthorize
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("roleAuthorize") RoleAuthorize roleAuthorize){
        logger.debug("request : {},{}", roleAuthorize.getRoleAuthorizeId(), roleAuthorize);
        try{
            RoleAuthorize result = null;
            if(roleAuthorize.getRoleAuthorizeId() != null) {
                result = iRoleAuthorizeSV.getRoleAuthorizeByPK(roleAuthorize.getRoleAuthorizeId());
            }else {
                RoleAuthorize_Example example = ExampleUtils.parseExample(roleAuthorize, RoleAuthorize_Example.class);
                List<RoleAuthorize> list = iRoleAuthorizeSV.getRoleAuthorizeListByExample(example);
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
    * 搜索一个瑙掕壊鎺堟潈
    * @param  roleAuthorize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" roleAuthorize") RoleAuthorize  roleAuthorize){
        logger.debug("request : {}",  roleAuthorize);
        try{
            RoleAuthorize result = null;
            if(roleAuthorize.getRoleAuthorizeId() != null) {
                result =  iRoleAuthorizeSV.getRoleAuthorizeByPK(roleAuthorize.getRoleAuthorizeId());
            }else {
                RoleAuthorize_Example example = ExampleUtils.parseExample( roleAuthorize, RoleAuthorize_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<RoleAuthorize> list =  iRoleAuthorizeSV.getRoleAuthorizeListByExample(example);
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
    * 创建瑙掕壊鎺堟潈
    * @param roleAuthorize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("roleAuthorize") RoleAuthorize roleAuthorize) {
        logger.debug("request : {}", roleAuthorize);
        try {
            ControllerHelper.setDefaultValue(roleAuthorize, "roleAuthorizeId");
            int result = iRoleAuthorizeSV.create(roleAuthorize);
            if(result > 0) {
                return ResultData.success(roleAuthorize);
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
    * 批量维护瑙掕壊鎺堟潈
    * @param roleAuthorizes
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody RoleAuthorize[] roleAuthorizes) {
        logger.debug("request : {}", roleAuthorizes);

        try {
            ControllerHelper.setDefaultValue(roleAuthorizes, "roleAuthorizeId");
            ControllerHelper.reorderProperty(roleAuthorizes);

            int result = iRoleAuthorizeSV.batchOperate(roleAuthorizes);
            if(result > 0) {
                return ResultData.success(roleAuthorizes);
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
    * 更新瑙掕壊鎺堟潈
    * @param roleAuthorize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("roleAuthorize") RoleAuthorize roleAuthorize) {
        logger.debug("request : {}", roleAuthorize);
        try {
            ControllerHelper.setDefaultValue(roleAuthorize, "roleAuthorizeId");
            int result = iRoleAuthorizeSV.update(roleAuthorize);
            if(result > 0) {
                return ResultData.success(roleAuthorize);
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
    * 创建或更新瑙掕壊鎺堟潈
    * @param roleAuthorize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("roleAuthorize") RoleAuthorize roleAuthorize) {
        logger.debug("request : {}", roleAuthorize);
        try {
            ControllerHelper.setDefaultValue(roleAuthorize, "roleAuthorizeId");
            int result = iRoleAuthorizeSV.batchOperate(new RoleAuthorize[]{roleAuthorize});
            if(result > 0) {
                return ResultData.success(roleAuthorize);
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
    * 删除瑙掕壊鎺堟潈
    * @param roleAuthorize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("roleAuthorize") RoleAuthorize roleAuthorize) {
        logger.debug("request : {}", roleAuthorize);

        try {
            ControllerHelper.setDefaultValue(roleAuthorize, "roleAuthorizeId");
            int result = iRoleAuthorizeSV.delete(roleAuthorize);
            if(result > 0) {
                return ResultData.success(roleAuthorize);
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
 	
	public IRoleAuthorizeSV getIRoleAuthorizeSV(){
		return iRoleAuthorizeSV;
	}
	//setter
	public void setIRoleAuthorizeSV(IRoleAuthorizeSV iRoleAuthorizeSV){
    	this.iRoleAuthorizeSV = iRoleAuthorizeSV;
    }
}
