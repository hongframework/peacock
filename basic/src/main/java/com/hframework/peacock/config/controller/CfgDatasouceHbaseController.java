package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgDatasouceHbase;
import com.hframework.peacock.config.domain.model.CfgDatasouceHbase_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgDatasouceHbaseSV;

@Controller
@RequestMapping(value = "/config/cfgDatasouceHbase")
public class CfgDatasouceHbaseController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgDatasouceHbaseController.class);

	@Resource
	private ICfgDatasouceHbaseSV iCfgDatasouceHbaseSV;
  

    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示HBASE列表
     * @param cfgDatasouceHbase
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgDatasouceHbase") CfgDatasouceHbase cfgDatasouceHbase,
                           @ModelAttribute("example") CfgDatasouceHbase_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgDatasouceHbase, example, pagination);
        try{
            ExampleUtils.parseExample(cfgDatasouceHbase, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgDatasouceHbase> list = iCfgDatasouceHbaseSV.getCfgDatasouceHbaseListByExample(example);
            pagination.setTotalCount(iCfgDatasouceHbaseSV.getCfgDatasouceHbaseCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示HBASE明细
     * @param cfgDatasouceHbase
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgDatasouceHbase") CfgDatasouceHbase cfgDatasouceHbase){
        logger.debug("request : {},{}", cfgDatasouceHbase.getId(), cfgDatasouceHbase);
        try{
            CfgDatasouceHbase result = null;
            if(cfgDatasouceHbase.getId() != null) {
                result = iCfgDatasouceHbaseSV.getCfgDatasouceHbaseByPK(cfgDatasouceHbase.getId());
            }else {
                CfgDatasouceHbase_Example example = ExampleUtils.parseExample(cfgDatasouceHbase, CfgDatasouceHbase_Example.class);
                List<CfgDatasouceHbase> list = iCfgDatasouceHbaseSV.getCfgDatasouceHbaseListByExample(example);
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
    * 搜索一个HBASE
    * @param  cfgDatasouceHbase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgDatasouceHbase") CfgDatasouceHbase  cfgDatasouceHbase){
        logger.debug("request : {}",  cfgDatasouceHbase);
        try{
            CfgDatasouceHbase result = null;
            if(cfgDatasouceHbase.getId() != null) {
                result =  iCfgDatasouceHbaseSV.getCfgDatasouceHbaseByPK(cfgDatasouceHbase.getId());
            }else {
                CfgDatasouceHbase_Example example = ExampleUtils.parseExample( cfgDatasouceHbase, CfgDatasouceHbase_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgDatasouceHbase> list =  iCfgDatasouceHbaseSV.getCfgDatasouceHbaseListByExample(example);
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
    * 创建HBASE
    * @param cfgDatasouceHbase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgDatasouceHbase") CfgDatasouceHbase cfgDatasouceHbase) {
        logger.debug("request : {}", cfgDatasouceHbase);
        try {
            ControllerHelper.setDefaultValue(cfgDatasouceHbase, "id");
            int result = iCfgDatasouceHbaseSV.create(cfgDatasouceHbase);
            if(result > 0) {
                return ResultData.success(cfgDatasouceHbase);
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
    * 批量维护HBASE
    * @param cfgDatasouceHbases
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgDatasouceHbase[] cfgDatasouceHbases) {
        logger.debug("request : {}", cfgDatasouceHbases);

        try {
            ControllerHelper.setDefaultValue(cfgDatasouceHbases, "id");
            ControllerHelper.reorderProperty(cfgDatasouceHbases);

            int result = iCfgDatasouceHbaseSV.batchOperate(cfgDatasouceHbases);
            if(result > 0) {
                return ResultData.success(cfgDatasouceHbases);
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
    * 更新HBASE
    * @param cfgDatasouceHbase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgDatasouceHbase") CfgDatasouceHbase cfgDatasouceHbase) {
        logger.debug("request : {}", cfgDatasouceHbase);
        try {
            ControllerHelper.setDefaultValue(cfgDatasouceHbase, "id");
            int result = iCfgDatasouceHbaseSV.update(cfgDatasouceHbase);
            if(result > 0) {
                return ResultData.success(cfgDatasouceHbase);
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
    * 创建或更新HBASE
    * @param cfgDatasouceHbase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgDatasouceHbase") CfgDatasouceHbase cfgDatasouceHbase) {
        logger.debug("request : {}", cfgDatasouceHbase);
        try {
            ControllerHelper.setDefaultValue(cfgDatasouceHbase, "id");
            int result = iCfgDatasouceHbaseSV.batchOperate(new CfgDatasouceHbase[]{cfgDatasouceHbase});
            if(result > 0) {
                return ResultData.success(cfgDatasouceHbase);
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
    * 删除HBASE
    * @param cfgDatasouceHbase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgDatasouceHbase") CfgDatasouceHbase cfgDatasouceHbase) {
        logger.debug("request : {}", cfgDatasouceHbase);

        try {
            ControllerHelper.setDefaultValue(cfgDatasouceHbase, "id");
            int result = iCfgDatasouceHbaseSV.delete(cfgDatasouceHbase);
            if(result > 0) {
                return ResultData.success(cfgDatasouceHbase);
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
 	
	public ICfgDatasouceHbaseSV getICfgDatasouceHbaseSV(){
		return iCfgDatasouceHbaseSV;
	}
	//setter
	public void setICfgDatasouceHbaseSV(ICfgDatasouceHbaseSV iCfgDatasouceHbaseSV){
    	this.iCfgDatasouceHbaseSV = iCfgDatasouceHbaseSV;
    }
}
