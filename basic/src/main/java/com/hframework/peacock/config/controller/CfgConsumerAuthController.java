package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgConsumerAuth;
import com.hframework.peacock.config.domain.model.CfgConsumerAuth_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgConsumerAuthSV;

@Controller
@RequestMapping(value = "/config/cfgConsumerAuth")
public class CfgConsumerAuthController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgConsumerAuthController.class);

	@Resource
	private ICfgConsumerAuthSV iCfgConsumerAuthSV;
  

    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示API娑堣垂鑰呮巿鏉�列表
     * @param cfgConsumerAuth
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgConsumerAuth") CfgConsumerAuth cfgConsumerAuth,
                           @ModelAttribute("example") CfgConsumerAuth_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgConsumerAuth, example, pagination);
        try{
            ExampleUtils.parseExample(cfgConsumerAuth, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgConsumerAuth> list = iCfgConsumerAuthSV.getCfgConsumerAuthListByExample(example);
            pagination.setTotalCount(iCfgConsumerAuthSV.getCfgConsumerAuthCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示API娑堣垂鑰呮巿鏉�明细
     * @param cfgConsumerAuth
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgConsumerAuth") CfgConsumerAuth cfgConsumerAuth){
        logger.debug("request : {},{}", cfgConsumerAuth.getId(), cfgConsumerAuth);
        try{
            CfgConsumerAuth result = null;
            if(cfgConsumerAuth.getId() != null) {
                result = iCfgConsumerAuthSV.getCfgConsumerAuthByPK(cfgConsumerAuth.getId());
            }else {
                CfgConsumerAuth_Example example = ExampleUtils.parseExample(cfgConsumerAuth, CfgConsumerAuth_Example.class);
                List<CfgConsumerAuth> list = iCfgConsumerAuthSV.getCfgConsumerAuthListByExample(example);
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
    * 搜索一个API娑堣垂鑰呮巿鏉�
    * @param  cfgConsumerAuth
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgConsumerAuth") CfgConsumerAuth  cfgConsumerAuth){
        logger.debug("request : {}",  cfgConsumerAuth);
        try{
            CfgConsumerAuth result = null;
            if(cfgConsumerAuth.getId() != null) {
                result =  iCfgConsumerAuthSV.getCfgConsumerAuthByPK(cfgConsumerAuth.getId());
            }else {
                CfgConsumerAuth_Example example = ExampleUtils.parseExample( cfgConsumerAuth, CfgConsumerAuth_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgConsumerAuth> list =  iCfgConsumerAuthSV.getCfgConsumerAuthListByExample(example);
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
    * 创建API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuth
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgConsumerAuth") CfgConsumerAuth cfgConsumerAuth) {
        logger.debug("request : {}", cfgConsumerAuth);
        try {
            ControllerHelper.setDefaultValue(cfgConsumerAuth, "id");
            int result = iCfgConsumerAuthSV.create(cfgConsumerAuth);
            if(result > 0) {
                return ResultData.success(cfgConsumerAuth);
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
    * 批量维护API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuths
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgConsumerAuth[] cfgConsumerAuths) {
        logger.debug("request : {}", cfgConsumerAuths);

        try {
            ControllerHelper.setDefaultValue(cfgConsumerAuths, "id");
            ControllerHelper.reorderProperty(cfgConsumerAuths);

            int result = iCfgConsumerAuthSV.batchOperate(cfgConsumerAuths);
            if(result > 0) {
                return ResultData.success(cfgConsumerAuths);
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
    * 更新API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuth
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgConsumerAuth") CfgConsumerAuth cfgConsumerAuth) {
        logger.debug("request : {}", cfgConsumerAuth);
        try {
            ControllerHelper.setDefaultValue(cfgConsumerAuth, "id");
            int result = iCfgConsumerAuthSV.update(cfgConsumerAuth);
            if(result > 0) {
                return ResultData.success(cfgConsumerAuth);
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
    * 创建或更新API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuth
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgConsumerAuth") CfgConsumerAuth cfgConsumerAuth) {
        logger.debug("request : {}", cfgConsumerAuth);
        try {
            ControllerHelper.setDefaultValue(cfgConsumerAuth, "id");
            int result = iCfgConsumerAuthSV.batchOperate(new CfgConsumerAuth[]{cfgConsumerAuth});
            if(result > 0) {
                return ResultData.success(cfgConsumerAuth);
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
    * 删除API娑堣垂鑰呮巿鏉�
    * @param cfgConsumerAuth
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgConsumerAuth") CfgConsumerAuth cfgConsumerAuth) {
        logger.debug("request : {}", cfgConsumerAuth);

        try {
            ControllerHelper.setDefaultValue(cfgConsumerAuth, "id");
            int result = iCfgConsumerAuthSV.delete(cfgConsumerAuth);
            if(result > 0) {
                return ResultData.success(cfgConsumerAuth);
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
 	
	public ICfgConsumerAuthSV getICfgConsumerAuthSV(){
		return iCfgConsumerAuthSV;
	}
	//setter
	public void setICfgConsumerAuthSV(ICfgConsumerAuthSV iCfgConsumerAuthSV){
    	this.iCfgConsumerAuthSV = iCfgConsumerAuthSV;
    }
}
