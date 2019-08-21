package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgFeature;
import com.hframework.peacock.config.domain.model.CfgFeature_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgFeatureSV;

@Controller
@RequestMapping(value = "/config/cfgFeature")
public class CfgFeatureController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgFeatureController.class);

	@Resource
	private ICfgFeatureSV iCfgFeatureSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示鐗瑰緛列表
     * @param cfgFeature
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgFeature") CfgFeature cfgFeature,
                           @ModelAttribute("example") CfgFeature_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgFeature, example, pagination);
        try{
            ExampleUtils.parseExample(cfgFeature, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgFeature> list = iCfgFeatureSV.getCfgFeatureListByExample(example);
            pagination.setTotalCount(iCfgFeatureSV.getCfgFeatureCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示鐗瑰緛明细
     * @param cfgFeature
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgFeature") CfgFeature cfgFeature){
        logger.debug("request : {},{}", cfgFeature.getId(), cfgFeature);
        try{
            CfgFeature result = null;
            if(cfgFeature.getId() != null) {
                result = iCfgFeatureSV.getCfgFeatureByPK(cfgFeature.getId());
            }else {
                CfgFeature_Example example = ExampleUtils.parseExample(cfgFeature, CfgFeature_Example.class);
                List<CfgFeature> list = iCfgFeatureSV.getCfgFeatureListByExample(example);
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
    * 搜索一个鐗瑰緛
    * @param  cfgFeature
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgFeature") CfgFeature  cfgFeature){
        logger.debug("request : {}",  cfgFeature);
        try{
            CfgFeature result = null;
            if(cfgFeature.getId() != null) {
                result =  iCfgFeatureSV.getCfgFeatureByPK(cfgFeature.getId());
            }else {
                CfgFeature_Example example = ExampleUtils.parseExample( cfgFeature, CfgFeature_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgFeature> list =  iCfgFeatureSV.getCfgFeatureListByExample(example);
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
    * 创建鐗瑰緛
    * @param cfgFeature
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgFeature") CfgFeature cfgFeature) {
        logger.debug("request : {}", cfgFeature);
        try {
            ControllerHelper.setDefaultValue(cfgFeature, "id");
            int result = iCfgFeatureSV.create(cfgFeature);
            if(result > 0) {
                return ResultData.success(cfgFeature);
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
    * 批量维护鐗瑰緛
    * @param cfgFeatures
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgFeature[] cfgFeatures) {
        logger.debug("request : {}", cfgFeatures);

        try {
            ControllerHelper.setDefaultValue(cfgFeatures, "id");
            ControllerHelper.reorderProperty(cfgFeatures);

            int result = iCfgFeatureSV.batchOperate(cfgFeatures);
            if(result > 0) {
                return ResultData.success(cfgFeatures);
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
    * 更新鐗瑰緛
    * @param cfgFeature
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgFeature") CfgFeature cfgFeature) {
        logger.debug("request : {}", cfgFeature);
        try {
            ControllerHelper.setDefaultValue(cfgFeature, "id");
            int result = iCfgFeatureSV.update(cfgFeature);
            if(result > 0) {
                return ResultData.success(cfgFeature);
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
    * 创建或更新鐗瑰緛
    * @param cfgFeature
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgFeature") CfgFeature cfgFeature) {
        logger.debug("request : {}", cfgFeature);
        try {
            ControllerHelper.setDefaultValue(cfgFeature, "id");
            int result = iCfgFeatureSV.batchOperate(new CfgFeature[]{cfgFeature});
            if(result > 0) {
                return ResultData.success(cfgFeature);
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
    * 删除鐗瑰緛
    * @param cfgFeature
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgFeature") CfgFeature cfgFeature) {
        logger.debug("request : {}", cfgFeature);

        try {
            ControllerHelper.setDefaultValue(cfgFeature, "id");
            int result = iCfgFeatureSV.delete(cfgFeature);
            if(result > 0) {
                return ResultData.success(cfgFeature);
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
 	
	public ICfgFeatureSV getICfgFeatureSV(){
		return iCfgFeatureSV;
	}
	//setter
	public void setICfgFeatureSV(ICfgFeatureSV iCfgFeatureSV){
    	this.iCfgFeatureSV = iCfgFeatureSV;
    }
}
