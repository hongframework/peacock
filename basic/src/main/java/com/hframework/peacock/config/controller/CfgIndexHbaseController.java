package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgIndexHbase;
import com.hframework.peacock.config.domain.model.CfgIndexHbase_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgIndexHbaseSV;

@Controller
@RequestMapping(value = "/config/cfgIndexHbase")
public class CfgIndexHbaseController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgIndexHbaseController.class);

	@Resource
	private ICfgIndexHbaseSV iCfgIndexHbaseSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示HBASE鎸囨爣列表
     * @param cfgIndexHbase
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgIndexHbase") CfgIndexHbase cfgIndexHbase,
                           @ModelAttribute("example") CfgIndexHbase_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgIndexHbase, example, pagination);
        try{
            ExampleUtils.parseExample(cfgIndexHbase, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgIndexHbase> list = iCfgIndexHbaseSV.getCfgIndexHbaseListByExample(example);
            pagination.setTotalCount(iCfgIndexHbaseSV.getCfgIndexHbaseCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示HBASE鎸囨爣明细
     * @param cfgIndexHbase
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgIndexHbase") CfgIndexHbase cfgIndexHbase){
        logger.debug("request : {},{}", cfgIndexHbase.getId(), cfgIndexHbase);
        try{
            CfgIndexHbase result = null;
            if(cfgIndexHbase.getId() != null) {
                result = iCfgIndexHbaseSV.getCfgIndexHbaseByPK(cfgIndexHbase.getId());
            }else {
                CfgIndexHbase_Example example = ExampleUtils.parseExample(cfgIndexHbase, CfgIndexHbase_Example.class);
                List<CfgIndexHbase> list = iCfgIndexHbaseSV.getCfgIndexHbaseListByExample(example);
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
    * 搜索一个HBASE鎸囨爣
    * @param  cfgIndexHbase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgIndexHbase") CfgIndexHbase  cfgIndexHbase){
        logger.debug("request : {}",  cfgIndexHbase);
        try{
            CfgIndexHbase result = null;
            if(cfgIndexHbase.getId() != null) {
                result =  iCfgIndexHbaseSV.getCfgIndexHbaseByPK(cfgIndexHbase.getId());
            }else {
                CfgIndexHbase_Example example = ExampleUtils.parseExample( cfgIndexHbase, CfgIndexHbase_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgIndexHbase> list =  iCfgIndexHbaseSV.getCfgIndexHbaseListByExample(example);
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
    * 创建HBASE鎸囨爣
    * @param cfgIndexHbase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgIndexHbase") CfgIndexHbase cfgIndexHbase) {
        logger.debug("request : {}", cfgIndexHbase);
        try {
            ControllerHelper.setDefaultValue(cfgIndexHbase, "id");
            int result = iCfgIndexHbaseSV.create(cfgIndexHbase);
            if(result > 0) {
                return ResultData.success(cfgIndexHbase);
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
    * 批量维护HBASE鎸囨爣
    * @param cfgIndexHbases
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgIndexHbase[] cfgIndexHbases) {
        logger.debug("request : {}", cfgIndexHbases);

        try {
            ControllerHelper.setDefaultValue(cfgIndexHbases, "id");
            ControllerHelper.reorderProperty(cfgIndexHbases);

            int result = iCfgIndexHbaseSV.batchOperate(cfgIndexHbases);
            if(result > 0) {
                return ResultData.success(cfgIndexHbases);
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
    * 更新HBASE鎸囨爣
    * @param cfgIndexHbase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgIndexHbase") CfgIndexHbase cfgIndexHbase) {
        logger.debug("request : {}", cfgIndexHbase);
        try {
            ControllerHelper.setDefaultValue(cfgIndexHbase, "id");
            int result = iCfgIndexHbaseSV.update(cfgIndexHbase);
            if(result > 0) {
                return ResultData.success(cfgIndexHbase);
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
    * 创建或更新HBASE鎸囨爣
    * @param cfgIndexHbase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgIndexHbase") CfgIndexHbase cfgIndexHbase) {
        logger.debug("request : {}", cfgIndexHbase);
        try {
            ControllerHelper.setDefaultValue(cfgIndexHbase, "id");
            int result = iCfgIndexHbaseSV.batchOperate(new CfgIndexHbase[]{cfgIndexHbase});
            if(result > 0) {
                return ResultData.success(cfgIndexHbase);
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
    * 删除HBASE鎸囨爣
    * @param cfgIndexHbase
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgIndexHbase") CfgIndexHbase cfgIndexHbase) {
        logger.debug("request : {}", cfgIndexHbase);

        try {
            ControllerHelper.setDefaultValue(cfgIndexHbase, "id");
            int result = iCfgIndexHbaseSV.delete(cfgIndexHbase);
            if(result > 0) {
                return ResultData.success(cfgIndexHbase);
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
 	
	public ICfgIndexHbaseSV getICfgIndexHbaseSV(){
		return iCfgIndexHbaseSV;
	}
	//setter
	public void setICfgIndexHbaseSV(ICfgIndexHbaseSV iCfgIndexHbaseSV){
    	this.iCfgIndexHbaseSV = iCfgIndexHbaseSV;
    }
}
