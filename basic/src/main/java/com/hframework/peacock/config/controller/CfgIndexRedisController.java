package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgIndexRedis;
import com.hframework.peacock.config.domain.model.CfgIndexRedis_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgIndexRedisSV;

@Controller
@RequestMapping(value = "/config/cfgIndexRedis")
public class CfgIndexRedisController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgIndexRedisController.class);

	@Resource
	private ICfgIndexRedisSV iCfgIndexRedisSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示REDIS鎸囨爣列表
     * @param cfgIndexRedis
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgIndexRedis") CfgIndexRedis cfgIndexRedis,
                           @ModelAttribute("example") CfgIndexRedis_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgIndexRedis, example, pagination);
        try{
            ExampleUtils.parseExample(cfgIndexRedis, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgIndexRedis> list = iCfgIndexRedisSV.getCfgIndexRedisListByExample(example);
            pagination.setTotalCount(iCfgIndexRedisSV.getCfgIndexRedisCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示REDIS鎸囨爣明细
     * @param cfgIndexRedis
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgIndexRedis") CfgIndexRedis cfgIndexRedis){
        logger.debug("request : {},{}", cfgIndexRedis.getId(), cfgIndexRedis);
        try{
            CfgIndexRedis result = null;
            if(cfgIndexRedis.getId() != null) {
                result = iCfgIndexRedisSV.getCfgIndexRedisByPK(cfgIndexRedis.getId());
            }else {
                CfgIndexRedis_Example example = ExampleUtils.parseExample(cfgIndexRedis, CfgIndexRedis_Example.class);
                List<CfgIndexRedis> list = iCfgIndexRedisSV.getCfgIndexRedisListByExample(example);
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
    * 搜索一个REDIS鎸囨爣
    * @param  cfgIndexRedis
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgIndexRedis") CfgIndexRedis  cfgIndexRedis){
        logger.debug("request : {}",  cfgIndexRedis);
        try{
            CfgIndexRedis result = null;
            if(cfgIndexRedis.getId() != null) {
                result =  iCfgIndexRedisSV.getCfgIndexRedisByPK(cfgIndexRedis.getId());
            }else {
                CfgIndexRedis_Example example = ExampleUtils.parseExample( cfgIndexRedis, CfgIndexRedis_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgIndexRedis> list =  iCfgIndexRedisSV.getCfgIndexRedisListByExample(example);
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
    * 创建REDIS鎸囨爣
    * @param cfgIndexRedis
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgIndexRedis") CfgIndexRedis cfgIndexRedis) {
        logger.debug("request : {}", cfgIndexRedis);
        try {
            ControllerHelper.setDefaultValue(cfgIndexRedis, "id");
            int result = iCfgIndexRedisSV.create(cfgIndexRedis);
            if(result > 0) {
                return ResultData.success(cfgIndexRedis);
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
    * 批量维护REDIS鎸囨爣
    * @param cfgIndexRediss
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgIndexRedis[] cfgIndexRediss) {
        logger.debug("request : {}", cfgIndexRediss);

        try {
            ControllerHelper.setDefaultValue(cfgIndexRediss, "id");
            ControllerHelper.reorderProperty(cfgIndexRediss);

            int result = iCfgIndexRedisSV.batchOperate(cfgIndexRediss);
            if(result > 0) {
                return ResultData.success(cfgIndexRediss);
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
    * 更新REDIS鎸囨爣
    * @param cfgIndexRedis
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgIndexRedis") CfgIndexRedis cfgIndexRedis) {
        logger.debug("request : {}", cfgIndexRedis);
        try {
            ControllerHelper.setDefaultValue(cfgIndexRedis, "id");
            int result = iCfgIndexRedisSV.update(cfgIndexRedis);
            if(result > 0) {
                return ResultData.success(cfgIndexRedis);
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
    * 创建或更新REDIS鎸囨爣
    * @param cfgIndexRedis
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgIndexRedis") CfgIndexRedis cfgIndexRedis) {
        logger.debug("request : {}", cfgIndexRedis);
        try {
            ControllerHelper.setDefaultValue(cfgIndexRedis, "id");
            int result = iCfgIndexRedisSV.batchOperate(new CfgIndexRedis[]{cfgIndexRedis});
            if(result > 0) {
                return ResultData.success(cfgIndexRedis);
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
    * 删除REDIS鎸囨爣
    * @param cfgIndexRedis
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgIndexRedis") CfgIndexRedis cfgIndexRedis) {
        logger.debug("request : {}", cfgIndexRedis);

        try {
            ControllerHelper.setDefaultValue(cfgIndexRedis, "id");
            int result = iCfgIndexRedisSV.delete(cfgIndexRedis);
            if(result > 0) {
                return ResultData.success(cfgIndexRedis);
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
 	
	public ICfgIndexRedisSV getICfgIndexRedisSV(){
		return iCfgIndexRedisSV;
	}
	//setter
	public void setICfgIndexRedisSV(ICfgIndexRedisSV iCfgIndexRedisSV){
    	this.iCfgIndexRedisSV = iCfgIndexRedisSV;
    }
}
