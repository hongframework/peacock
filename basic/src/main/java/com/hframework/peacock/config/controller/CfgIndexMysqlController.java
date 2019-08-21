package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgIndexMysql;
import com.hframework.peacock.config.domain.model.CfgIndexMysql_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgIndexMysqlSV;

@Controller
@RequestMapping(value = "/config/cfgIndexMysql")
public class CfgIndexMysqlController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgIndexMysqlController.class);

	@Resource
	private ICfgIndexMysqlSV iCfgIndexMysqlSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示MYSQL鎸囨爣列表
     * @param cfgIndexMysql
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgIndexMysql") CfgIndexMysql cfgIndexMysql,
                           @ModelAttribute("example") CfgIndexMysql_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgIndexMysql, example, pagination);
        try{
            ExampleUtils.parseExample(cfgIndexMysql, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgIndexMysql> list = iCfgIndexMysqlSV.getCfgIndexMysqlListByExample(example);
            pagination.setTotalCount(iCfgIndexMysqlSV.getCfgIndexMysqlCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示MYSQL鎸囨爣明细
     * @param cfgIndexMysql
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgIndexMysql") CfgIndexMysql cfgIndexMysql){
        logger.debug("request : {},{}", cfgIndexMysql.getId(), cfgIndexMysql);
        try{
            CfgIndexMysql result = null;
            if(cfgIndexMysql.getId() != null) {
                result = iCfgIndexMysqlSV.getCfgIndexMysqlByPK(cfgIndexMysql.getId());
            }else {
                CfgIndexMysql_Example example = ExampleUtils.parseExample(cfgIndexMysql, CfgIndexMysql_Example.class);
                List<CfgIndexMysql> list = iCfgIndexMysqlSV.getCfgIndexMysqlListByExample(example);
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
    * 搜索一个MYSQL鎸囨爣
    * @param  cfgIndexMysql
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgIndexMysql") CfgIndexMysql  cfgIndexMysql){
        logger.debug("request : {}",  cfgIndexMysql);
        try{
            CfgIndexMysql result = null;
            if(cfgIndexMysql.getId() != null) {
                result =  iCfgIndexMysqlSV.getCfgIndexMysqlByPK(cfgIndexMysql.getId());
            }else {
                CfgIndexMysql_Example example = ExampleUtils.parseExample( cfgIndexMysql, CfgIndexMysql_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgIndexMysql> list =  iCfgIndexMysqlSV.getCfgIndexMysqlListByExample(example);
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
    * 创建MYSQL鎸囨爣
    * @param cfgIndexMysql
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgIndexMysql") CfgIndexMysql cfgIndexMysql) {
        logger.debug("request : {}", cfgIndexMysql);
        try {
            ControllerHelper.setDefaultValue(cfgIndexMysql, "id");
            int result = iCfgIndexMysqlSV.create(cfgIndexMysql);
            if(result > 0) {
                return ResultData.success(cfgIndexMysql);
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
    * 批量维护MYSQL鎸囨爣
    * @param cfgIndexMysqls
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgIndexMysql[] cfgIndexMysqls) {
        logger.debug("request : {}", cfgIndexMysqls);

        try {
            ControllerHelper.setDefaultValue(cfgIndexMysqls, "id");
            ControllerHelper.reorderProperty(cfgIndexMysqls);

            int result = iCfgIndexMysqlSV.batchOperate(cfgIndexMysqls);
            if(result > 0) {
                return ResultData.success(cfgIndexMysqls);
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
    * 更新MYSQL鎸囨爣
    * @param cfgIndexMysql
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgIndexMysql") CfgIndexMysql cfgIndexMysql) {
        logger.debug("request : {}", cfgIndexMysql);
        try {
            ControllerHelper.setDefaultValue(cfgIndexMysql, "id");
            int result = iCfgIndexMysqlSV.update(cfgIndexMysql);
            if(result > 0) {
                return ResultData.success(cfgIndexMysql);
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
    * 创建或更新MYSQL鎸囨爣
    * @param cfgIndexMysql
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgIndexMysql") CfgIndexMysql cfgIndexMysql) {
        logger.debug("request : {}", cfgIndexMysql);
        try {
            ControllerHelper.setDefaultValue(cfgIndexMysql, "id");
            int result = iCfgIndexMysqlSV.batchOperate(new CfgIndexMysql[]{cfgIndexMysql});
            if(result > 0) {
                return ResultData.success(cfgIndexMysql);
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
    * 删除MYSQL鎸囨爣
    * @param cfgIndexMysql
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgIndexMysql") CfgIndexMysql cfgIndexMysql) {
        logger.debug("request : {}", cfgIndexMysql);

        try {
            ControllerHelper.setDefaultValue(cfgIndexMysql, "id");
            int result = iCfgIndexMysqlSV.delete(cfgIndexMysql);
            if(result > 0) {
                return ResultData.success(cfgIndexMysql);
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
 	
	public ICfgIndexMysqlSV getICfgIndexMysqlSV(){
		return iCfgIndexMysqlSV;
	}
	//setter
	public void setICfgIndexMysqlSV(ICfgIndexMysqlSV iCfgIndexMysqlSV){
    	this.iCfgIndexMysqlSV = iCfgIndexMysqlSV;
    }
}
