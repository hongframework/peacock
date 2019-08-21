package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.CfgConsumer;
import com.hframework.peacock.config.domain.model.CfgConsumer_Example;
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
import com.hframework.peacock.config.service.interfaces.ICfgConsumerSV;

@Controller
@RequestMapping(value = "/config/cfgConsumer")
public class CfgConsumerController   {
    private static final Logger logger = LoggerFactory.getLogger(CfgConsumerController.class);

	@Resource
	private ICfgConsumerSV iCfgConsumerSV;
  

    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示API娑堣垂鑰�列表
     * @param cfgConsumer
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("cfgConsumer") CfgConsumer cfgConsumer,
                           @ModelAttribute("example") CfgConsumer_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", cfgConsumer, example, pagination);
        try{
            ExampleUtils.parseExample(cfgConsumer, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< CfgConsumer> list = iCfgConsumerSV.getCfgConsumerListByExample(example);
            pagination.setTotalCount(iCfgConsumerSV.getCfgConsumerCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示API娑堣垂鑰�明细
     * @param cfgConsumer
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("cfgConsumer") CfgConsumer cfgConsumer){
        logger.debug("request : {},{}", cfgConsumer.getId(), cfgConsumer);
        try{
            CfgConsumer result = null;
            if(cfgConsumer.getId() != null) {
                result = iCfgConsumerSV.getCfgConsumerByPK(cfgConsumer.getId());
            }else {
                CfgConsumer_Example example = ExampleUtils.parseExample(cfgConsumer, CfgConsumer_Example.class);
                List<CfgConsumer> list = iCfgConsumerSV.getCfgConsumerListByExample(example);
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
    * 搜索一个API娑堣垂鑰�
    * @param  cfgConsumer
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" cfgConsumer") CfgConsumer  cfgConsumer){
        logger.debug("request : {}",  cfgConsumer);
        try{
            CfgConsumer result = null;
            if(cfgConsumer.getId() != null) {
                result =  iCfgConsumerSV.getCfgConsumerByPK(cfgConsumer.getId());
            }else {
                CfgConsumer_Example example = ExampleUtils.parseExample( cfgConsumer, CfgConsumer_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<CfgConsumer> list =  iCfgConsumerSV.getCfgConsumerListByExample(example);
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
    * 创建API娑堣垂鑰�
    * @param cfgConsumer
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("cfgConsumer") CfgConsumer cfgConsumer) {
        logger.debug("request : {}", cfgConsumer);
        try {
            ControllerHelper.setDefaultValue(cfgConsumer, "id");
            int result = iCfgConsumerSV.create(cfgConsumer);
            if(result > 0) {
                return ResultData.success(cfgConsumer);
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
    * 批量维护API娑堣垂鑰�
    * @param cfgConsumers
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody CfgConsumer[] cfgConsumers) {
        logger.debug("request : {}", cfgConsumers);

        try {
            ControllerHelper.setDefaultValue(cfgConsumers, "id");
            ControllerHelper.reorderProperty(cfgConsumers);

            int result = iCfgConsumerSV.batchOperate(cfgConsumers);
            if(result > 0) {
                return ResultData.success(cfgConsumers);
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
    * 更新API娑堣垂鑰�
    * @param cfgConsumer
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("cfgConsumer") CfgConsumer cfgConsumer) {
        logger.debug("request : {}", cfgConsumer);
        try {
            ControllerHelper.setDefaultValue(cfgConsumer, "id");
            int result = iCfgConsumerSV.update(cfgConsumer);
            if(result > 0) {
                return ResultData.success(cfgConsumer);
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
    * 创建或更新API娑堣垂鑰�
    * @param cfgConsumer
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("cfgConsumer") CfgConsumer cfgConsumer) {
        logger.debug("request : {}", cfgConsumer);
        try {
            ControllerHelper.setDefaultValue(cfgConsumer, "id");
            int result = iCfgConsumerSV.batchOperate(new CfgConsumer[]{cfgConsumer});
            if(result > 0) {
                return ResultData.success(cfgConsumer);
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
    * 删除API娑堣垂鑰�
    * @param cfgConsumer
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("cfgConsumer") CfgConsumer cfgConsumer) {
        logger.debug("request : {}", cfgConsumer);

        try {
            ControllerHelper.setDefaultValue(cfgConsumer, "id");
            int result = iCfgConsumerSV.delete(cfgConsumer);
            if(result > 0) {
                return ResultData.success(cfgConsumer);
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
 	
	public ICfgConsumerSV getICfgConsumerSV(){
		return iCfgConsumerSV;
	}
	//setter
	public void setICfgConsumerSV(ICfgConsumerSV iCfgConsumerSV){
    	this.iCfgConsumerSV = iCfgConsumerSV;
    }
}
