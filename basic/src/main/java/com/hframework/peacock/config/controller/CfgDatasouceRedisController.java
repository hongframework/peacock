package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgDatasouceRedis;
import com.hframework.peacock.config.domain.model.CfgDatasouceRedis_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgDatasouceRedisSV;

@Controller
@RequestMapping(value = "/config/cfgDatasouceRedis")
public class CfgDatasouceRedisController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgDatasouceRedisController.class);

	@Resource
	private ICfgDatasouceRedisSV iCfgDatasouceRedisSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示REDIS列表
     * @param cfgDatasouceRedis
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgDatasouceRedis") CfgDatasouceRedis cfgDatasouceRedis,
                           @ModelAttribute("example") CfgDatasouceRedis_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgDatasouceRedis, example, pagination);
        try{
            ExampleUtils.parseExample(cfgDatasouceRedis, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgDatasouceRedis> list = iCfgDatasouceRedisSV.getCfgDatasouceRedisListByExample(example);
            pagination.setTotalCount(iCfgDatasouceRedisSV.getCfgDatasouceRedisCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示REDIS明细
     * @param cfgDatasouceRedis
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgDatasouceRedis") CfgDatasouceRedis cfgDatasouceRedis){
        logger.debug("request : {},{}", cfgDatasouceRedis.getId(), cfgDatasouceRedis);
        try{
            CfgDatasouceRedis result = null;
            if(cfgDatasouceRedis.getId() != null) {
                result = iCfgDatasouceRedisSV.getCfgDatasouceRedisByPK(cfgDatasouceRedis.getId());
            }else {
                CfgDatasouceRedis_Example example = ExampleUtils.parseExample(cfgDatasouceRedis, CfgDatasouceRedis_Example.class);
                List<CfgDatasouceRedis> list = iCfgDatasouceRedisSV.getCfgDatasouceRedisListByExample(example);
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
    * 搜索一个REDIS
    * @param  cfgDatasouceRedis
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgDatasouceRedis") CfgDatasouceRedis  cfgDatasouceRedis){
        logger.debug("request : {}",  cfgDatasouceRedis);
        try{
            CfgDatasouceRedis result = null;
            if(cfgDatasouceRedis.getId() != null) {
                result =  iCfgDatasouceRedisSV.getCfgDatasouceRedisByPK(cfgDatasouceRedis.getId());
            }else {
                CfgDatasouceRedis_Example example = ExampleUtils.parseExample( cfgDatasouceRedis, CfgDatasouceRedis_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgDatasouceRedis> list =  iCfgDatasouceRedisSV.getCfgDatasouceRedisListByExample(example);
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
    * 创建REDIS
    * @param cfgDatasouceRedis
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgDatasouceRedis") CfgDatasouceRedis cfgDatasouceRedis) {
        logger.debug("request : {}", cfgDatasouceRedis);
        try {
            ControllerHelper.setDefaultValue(cfgDatasouceRedis, "id");
            int result = iCfgDatasouceRedisSV.create(cfgDatasouceRedis);
            if(result > 0) {
                return ResultData.success(cfgDatasouceRedis);
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
    * 批量维护REDIS
    * @param cfgDatasouceRediss
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgDatasouceRedis[] cfgDatasouceRediss) {
        logger.debug("request : {}", cfgDatasouceRediss);

        try {
            ControllerHelper.setDefaultValue(cfgDatasouceRediss, "id");
            ControllerHelper.reorderProperty(cfgDatasouceRediss);

            int result = iCfgDatasouceRedisSV.batchOperate(cfgDatasouceRediss);
            if(result > 0) {
                return ResultData.success(cfgDatasouceRediss);
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
    * 更新REDIS
    * @param cfgDatasouceRedis
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgDatasouceRedis") CfgDatasouceRedis cfgDatasouceRedis) {
        logger.debug("request : {}", cfgDatasouceRedis);
        try {
            ControllerHelper.setDefaultValue(cfgDatasouceRedis, "id");
            int result = iCfgDatasouceRedisSV.update(cfgDatasouceRedis);
            if(result > 0) {
                return ResultData.success(cfgDatasouceRedis);
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
    * 创建或更新REDIS
    * @param cfgDatasouceRedis
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgDatasouceRedis") CfgDatasouceRedis cfgDatasouceRedis) {
        logger.debug("request : {}", cfgDatasouceRedis);
        try {
            ControllerHelper.setDefaultValue(cfgDatasouceRedis, "id");
            int result = iCfgDatasouceRedisSV.batchOperate(new CfgDatasouceRedis[]{cfgDatasouceRedis});
            if(result > 0) {
                return ResultData.success(cfgDatasouceRedis);
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
    * 删除REDIS
    * @param cfgDatasouceRedis
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgDatasouceRedis") CfgDatasouceRedis cfgDatasouceRedis) {
        logger.debug("request : {}", cfgDatasouceRedis);

        try {
            ControllerHelper.setDefaultValue(cfgDatasouceRedis, "id");
            int result = iCfgDatasouceRedisSV.delete(cfgDatasouceRedis);
            if(result > 0) {
                return ResultData.success(cfgDatasouceRedis);
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
 	
	public ICfgDatasouceRedisSV getICfgDatasouceRedisSV(){
		return iCfgDatasouceRedisSV;
	}
	//setter
	public void setICfgDatasouceRedisSV(ICfgDatasouceRedisSV iCfgDatasouceRedisSV){
    	this.iCfgDatasouceRedisSV = iCfgDatasouceRedisSV;
    }
}
