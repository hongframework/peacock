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
import com.hframework.peacock.system.domain.model.Role;
import com.hframework.peacock.system.domain.model.Role_Example;
import com.hframework.peacock.system.service.interfaces.IRoleSV;

@Controller
@RequestMapping(value = "/system/role")
public class RoleController   {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Resource
	private IRoleSV iRoleSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示瑙掕壊列表
     * @param role
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("role") Role role,
                                      @ModelAttribute("example") Role_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", role, example, pagination);
        try{
            ExampleUtils.parseExample(role, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< Role> list = iRoleSV.getRoleListByExample(example);
            pagination.setTotalCount(iRoleSV.getRoleCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示瑙掕壊明细
     * @param role
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("role") Role role){
        logger.debug("request : {},{}", role.getRoleId(), role);
        try{
            Role result = null;
            if(role.getRoleId() != null) {
                result = iRoleSV.getRoleByPK(role.getRoleId());
            }else {
                Role_Example example = ExampleUtils.parseExample(role, Role_Example.class);
                List<Role> list = iRoleSV.getRoleListByExample(example);
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
    * 搜索一个瑙掕壊
    * @param  role
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" role") Role  role){
        logger.debug("request : {}",  role);
        try{
            Role result = null;
            if(role.getRoleId() != null) {
                result =  iRoleSV.getRoleByPK(role.getRoleId());
            }else {
                Role_Example example = ExampleUtils.parseExample( role, Role_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<Role> list =  iRoleSV.getRoleListByExample(example);
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
    * 创建瑙掕壊
    * @param role
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("role") Role role) {
        logger.debug("request : {}", role);
        try {
            ControllerHelper.setDefaultValue(role, "roleId");
            int result = iRoleSV.create(role);
            if(result > 0) {
                return ResultData.success(role);
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
    * 批量维护瑙掕壊
    * @param roles
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody Role[] roles) {
        logger.debug("request : {}", roles);

        try {
            ControllerHelper.setDefaultValue(roles, "roleId");
            ControllerHelper.reorderProperty(roles);

            int result = iRoleSV.batchOperate(roles);
            if(result > 0) {
                return ResultData.success(roles);
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
    * 更新瑙掕壊
    * @param role
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("role") Role role) {
        logger.debug("request : {}", role);
        try {
            ControllerHelper.setDefaultValue(role, "roleId");
            int result = iRoleSV.update(role);
            if(result > 0) {
                return ResultData.success(role);
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
    * 创建或更新瑙掕壊
    * @param role
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("role") Role role) {
        logger.debug("request : {}", role);
        try {
            ControllerHelper.setDefaultValue(role, "roleId");
            int result = iRoleSV.batchOperate(new Role[]{role});
            if(result > 0) {
                return ResultData.success(role);
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
    * 删除瑙掕壊
    * @param role
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("role") Role role) {
        logger.debug("request : {}", role);

        try {
            ControllerHelper.setDefaultValue(role, "roleId");
            int result = iRoleSV.delete(role);
            if(result > 0) {
                return ResultData.success(role);
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
 	
	public IRoleSV getIRoleSV(){
		return iRoleSV;
	}
	//setter
	public void setIRoleSV(IRoleSV iRoleSV){
    	this.iRoleSV = iRoleSV;
    }
}
