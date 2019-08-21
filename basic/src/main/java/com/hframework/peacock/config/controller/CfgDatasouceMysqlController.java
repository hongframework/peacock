package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgDatasouceMysql;
import com.hframework.peacock.config.domain.model.CfgDatasouceMysql_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgDatasouceMysqlSV;

@Controller
@RequestMapping(value = "/config/cfgDatasouceMysql")
public class CfgDatasouceMysqlController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgDatasouceMysqlController.class);

	@Resource
	private ICfgDatasouceMysqlSV iCfgDatasouceMysqlSV;


    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示MYSQL列表
     * @param cfgDatasouceMysql
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgDatasouceMysql") CfgDatasouceMysql cfgDatasouceMysql,
                           @ModelAttribute("example") CfgDatasouceMysql_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgDatasouceMysql, example, pagination);
        try{
            ExampleUtils.parseExample(cfgDatasouceMysql, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgDatasouceMysql> list = iCfgDatasouceMysqlSV.getCfgDatasouceMysqlListByExample(example);
            pagination.setTotalCount(iCfgDatasouceMysqlSV.getCfgDatasouceMysqlCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示MYSQL明细
     * @param cfgDatasouceMysql
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgDatasouceMysql") CfgDatasouceMysql cfgDatasouceMysql){
        logger.debug("request : {},{}", cfgDatasouceMysql.getId(), cfgDatasouceMysql);
        try{
            CfgDatasouceMysql result = null;
            if(cfgDatasouceMysql.getId() != null) {
                result = iCfgDatasouceMysqlSV.getCfgDatasouceMysqlByPK(cfgDatasouceMysql.getId());
            }else {
                CfgDatasouceMysql_Example example = ExampleUtils.parseExample(cfgDatasouceMysql, CfgDatasouceMysql_Example.class);
                List<CfgDatasouceMysql> list = iCfgDatasouceMysqlSV.getCfgDatasouceMysqlListByExample(example);
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
    * 搜索一个MYSQL
    * @param  cfgDatasouceMysql
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgDatasouceMysql") CfgDatasouceMysql  cfgDatasouceMysql){
        logger.debug("request : {}",  cfgDatasouceMysql);
        try{
            CfgDatasouceMysql result = null;
            if(cfgDatasouceMysql.getId() != null) {
                result =  iCfgDatasouceMysqlSV.getCfgDatasouceMysqlByPK(cfgDatasouceMysql.getId());
            }else {
                CfgDatasouceMysql_Example example = ExampleUtils.parseExample( cfgDatasouceMysql, CfgDatasouceMysql_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgDatasouceMysql> list =  iCfgDatasouceMysqlSV.getCfgDatasouceMysqlListByExample(example);
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
    * 创建MYSQL
    * @param cfgDatasouceMysql
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgDatasouceMysql") CfgDatasouceMysql cfgDatasouceMysql) {
        logger.debug("request : {}", cfgDatasouceMysql);
        try {
            ControllerHelper.setDefaultValue(cfgDatasouceMysql, "id");
            int result = iCfgDatasouceMysqlSV.create(cfgDatasouceMysql);
            if(result > 0) {
                return ResultData.success(cfgDatasouceMysql);
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
    * 批量维护MYSQL
    * @param cfgDatasouceMysqls
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgDatasouceMysql[] cfgDatasouceMysqls) {
        logger.debug("request : {}", cfgDatasouceMysqls);

        try {
            ControllerHelper.setDefaultValue(cfgDatasouceMysqls, "id");
            ControllerHelper.reorderProperty(cfgDatasouceMysqls);

            int result = iCfgDatasouceMysqlSV.batchOperate(cfgDatasouceMysqls);
            if(result > 0) {
                return ResultData.success(cfgDatasouceMysqls);
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
    * 更新MYSQL
    * @param cfgDatasouceMysql
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgDatasouceMysql") CfgDatasouceMysql cfgDatasouceMysql) {
        logger.debug("request : {}", cfgDatasouceMysql);
        try {
            ControllerHelper.setDefaultValue(cfgDatasouceMysql, "id");
            int result = iCfgDatasouceMysqlSV.update(cfgDatasouceMysql);
            if(result > 0) {
                return ResultData.success(cfgDatasouceMysql);
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
    * 创建或更新MYSQL
    * @param cfgDatasouceMysql
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgDatasouceMysql") CfgDatasouceMysql cfgDatasouceMysql) {
        logger.debug("request : {}", cfgDatasouceMysql);
        try {
            ControllerHelper.setDefaultValue(cfgDatasouceMysql, "id");
            int result = iCfgDatasouceMysqlSV.batchOperate(new CfgDatasouceMysql[]{cfgDatasouceMysql});
            if(result > 0) {
                return ResultData.success(cfgDatasouceMysql);
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
    * 删除MYSQL
    * @param cfgDatasouceMysql
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgDatasouceMysql") CfgDatasouceMysql cfgDatasouceMysql) {
        logger.debug("request : {}", cfgDatasouceMysql);

        try {
            ControllerHelper.setDefaultValue(cfgDatasouceMysql, "id");
            int result = iCfgDatasouceMysqlSV.delete(cfgDatasouceMysql);
            if(result > 0) {
                return ResultData.success(cfgDatasouceMysql);
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
 	
	public ICfgDatasouceMysqlSV getICfgDatasouceMysqlSV(){
		return iCfgDatasouceMysqlSV;
	}
	//setter
	public void setICfgDatasouceMysqlSV(ICfgDatasouceMysqlSV iCfgDatasouceMysqlSV){
    	this.iCfgDatasouceMysqlSV = iCfgDatasouceMysqlSV;
    }
}
