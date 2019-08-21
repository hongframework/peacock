package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgIndex;
import com.hframework.peacock.config.domain.model.CfgIndex_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgIndexSV;

@Controller
@RequestMapping(value = "/config/cfgIndex")
public class CfgIndexController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgIndexController.class);

	@Resource
	private ICfgIndexSV iCfgIndexSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示鎸囨爣列表
     * @param cfgIndex
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgIndex") CfgIndex cfgIndex,
                           @ModelAttribute("example") CfgIndex_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgIndex, example, pagination);
        try{
            ExampleUtils.parseExample(cfgIndex, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgIndex> list = iCfgIndexSV.getCfgIndexListByExample(example);
            pagination.setTotalCount(iCfgIndexSV.getCfgIndexCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示鎸囨爣明细
     * @param cfgIndex
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgIndex") CfgIndex cfgIndex){
        logger.debug("request : {},{}", cfgIndex.getId(), cfgIndex);
        try{
            CfgIndex result = null;
            if(cfgIndex.getId() != null) {
                result = iCfgIndexSV.getCfgIndexByPK(cfgIndex.getId());
            }else {
                CfgIndex_Example example = ExampleUtils.parseExample(cfgIndex, CfgIndex_Example.class);
                List<CfgIndex> list = iCfgIndexSV.getCfgIndexListByExample(example);
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
    * 搜索一个鎸囨爣
    * @param  cfgIndex
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgIndex") CfgIndex  cfgIndex){
        logger.debug("request : {}",  cfgIndex);
        try{
            CfgIndex result = null;
            if(cfgIndex.getId() != null) {
                result =  iCfgIndexSV.getCfgIndexByPK(cfgIndex.getId());
            }else {
                CfgIndex_Example example = ExampleUtils.parseExample( cfgIndex, CfgIndex_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgIndex> list =  iCfgIndexSV.getCfgIndexListByExample(example);
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
    * 创建鎸囨爣
    * @param cfgIndex
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgIndex") CfgIndex cfgIndex) {
        logger.debug("request : {}", cfgIndex);
        try {
            ControllerHelper.setDefaultValue(cfgIndex, "id");
            int result = iCfgIndexSV.create(cfgIndex);
            if(result > 0) {
                return ResultData.success(cfgIndex);
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
    * 批量维护鎸囨爣
    * @param cfgIndexs
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgIndex[] cfgIndexs) {
        logger.debug("request : {}", cfgIndexs);

        try {
            ControllerHelper.setDefaultValue(cfgIndexs, "id");
            ControllerHelper.reorderProperty(cfgIndexs);

            int result = iCfgIndexSV.batchOperate(cfgIndexs);
            if(result > 0) {
                return ResultData.success(cfgIndexs);
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
    * 更新鎸囨爣
    * @param cfgIndex
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgIndex") CfgIndex cfgIndex) {
        logger.debug("request : {}", cfgIndex);
        try {
            ControllerHelper.setDefaultValue(cfgIndex, "id");
            int result = iCfgIndexSV.update(cfgIndex);
            if(result > 0) {
                return ResultData.success(cfgIndex);
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
    * 创建或更新鎸囨爣
    * @param cfgIndex
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgIndex") CfgIndex cfgIndex) {
        logger.debug("request : {}", cfgIndex);
        try {
            ControllerHelper.setDefaultValue(cfgIndex, "id");
            int result = iCfgIndexSV.batchOperate(new CfgIndex[]{cfgIndex});
            if(result > 0) {
                return ResultData.success(cfgIndex);
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
    * 删除鎸囨爣
    * @param cfgIndex
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgIndex") CfgIndex cfgIndex) {
        logger.debug("request : {}", cfgIndex);

        try {
            ControllerHelper.setDefaultValue(cfgIndex, "id");
            int result = iCfgIndexSV.delete(cfgIndex);
            if(result > 0) {
                return ResultData.success(cfgIndex);
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
 	
	public ICfgIndexSV getICfgIndexSV(){
		return iCfgIndexSV;
	}
	//setter
	public void setICfgIndexSV(ICfgIndexSV iCfgIndexSV){
    	this.iCfgIndexSV = iCfgIndexSV;
    }
}
