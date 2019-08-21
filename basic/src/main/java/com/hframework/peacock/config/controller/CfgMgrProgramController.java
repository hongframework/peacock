package com.hframework.peacock.config.controller;



import com.hframework.beans.controller.Pagination;

import com.hframework.beans.controller.ResultCode;

import com.hframework.beans.controller.ResultData;

import com.hframework.common.util.ExampleUtils;

import com.hframework.beans.exceptions.BusinessException;

import com.hframework.peacock.config.domain.model.CfgMgrProgram;
import com.hframework.peacock.config.domain.model.CfgMgrProgram_Example;
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

import com.hframework.peacock.config.service.interfaces.ICfgMgrProgramSV;



@Controller
@RequestMapping(value = "/config/cfgMgrProgram")

public class CfgMgrProgramController   {

    private static final Logger logger = LoggerFactory.getLogger(CfgMgrProgramController.class);



	@Resource

	private ICfgMgrProgramSV iCfgMgrProgramSV;

  











    @InitBinder

    protected void initBinder(HttpServletRequest request,

        ServletRequestDataBinder binder) throws Exception {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        CustomDateEditor editor = new CustomDateEditor(df, false);

        binder.registerCustomEditor(Date.class, editor);

    }



    /**

     * 查询展示项目列表

     * @param cfgMgrProgram

     * @return

     * @throws Throwable

     */

    @RequestMapping(value = "/queryListByAjax.json")

    @ResponseBody

    public ResultData list(@ModelAttribute("cfgMgrProgram") CfgMgrProgram cfgMgrProgram,

                           @ModelAttribute("example") CfgMgrProgram_Example example, Pagination pagination){

        logger.debug("request : {},{},{}", cfgMgrProgram, example, pagination);

        try{

            ExampleUtils.parseExample(cfgMgrProgram, example);

            //设置分页信息

            example.setLimitStart(pagination.getStartIndex());

            example.setLimitEnd(pagination.getEndIndex());



            final List< CfgMgrProgram> list = iCfgMgrProgramSV.getCfgMgrProgramListByExample(example);

            pagination.setTotalCount(iCfgMgrProgramSV.getCfgMgrProgramCountByExample(example));



            return ResultData.success().add("list",list).add("pagination",pagination);

        }catch (Exception e) {

            logger.error("error : ", e);

            return ResultData.error(ResultCode.ERROR);

        }

    }







    /**

     * 查询展示项目明细

     * @param cfgMgrProgram

     * @return

     * @throws Throwable

     */

    @RequestMapping(value = "/queryOneByAjax.json")

    @ResponseBody

    public ResultData detail(@ModelAttribute("cfgMgrProgram") CfgMgrProgram cfgMgrProgram){

        logger.debug("request : {},{}", cfgMgrProgram.getId(), cfgMgrProgram);

        try{

            CfgMgrProgram result = null;

            if(cfgMgrProgram.getId() != null) {

                result = iCfgMgrProgramSV.getCfgMgrProgramByPK(cfgMgrProgram.getId());

            }else {

                CfgMgrProgram_Example example = ExampleUtils.parseExample(cfgMgrProgram, CfgMgrProgram_Example.class);

                List<CfgMgrProgram> list = iCfgMgrProgramSV.getCfgMgrProgramListByExample(example);

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

    * 搜索一个项目

    * @param  cfgMgrProgram

    * @return

    * @throws Throwable

    */

    @RequestMapping(value = "/searchOneByAjax.json")

    @ResponseBody

    public ResultData search(@ModelAttribute(" cfgMgrProgram") CfgMgrProgram  cfgMgrProgram){

        logger.debug("request : {}",  cfgMgrProgram);

        try{

            CfgMgrProgram result = null;

            if(cfgMgrProgram.getId() != null) {

                result =  iCfgMgrProgramSV.getCfgMgrProgramByPK(cfgMgrProgram.getId());

            }else {

                CfgMgrProgram_Example example = ExampleUtils.parseExample( cfgMgrProgram, CfgMgrProgram_Example.class);



                example.setLimitStart(0);

                example.setLimitEnd(1);



                List<CfgMgrProgram> list =  iCfgMgrProgramSV.getCfgMgrProgramListByExample(example);

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

    * 创建项目

    * @param cfgMgrProgram

    * @return

    * @throws Throwable

    */

    @RequestMapping(value = "/createByAjax.json")

    @ResponseBody

    public ResultData create(@ModelAttribute("cfgMgrProgram") CfgMgrProgram cfgMgrProgram) {

        logger.debug("request : {}", cfgMgrProgram);

        try {

            ControllerHelper.setDefaultValue(cfgMgrProgram, "id");

            int result = iCfgMgrProgramSV.create(cfgMgrProgram);

            if(result > 0) {

                return ResultData.success(cfgMgrProgram);

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

    * 批量维护项目

    * @param cfgMgrPrograms

    * @return

    * @throws Throwable

    */

    @RequestMapping(value = "/createsByAjax.json")

    @ResponseBody

    public ResultData batchCreate(@RequestBody CfgMgrProgram[] cfgMgrPrograms) {

        logger.debug("request : {}", cfgMgrPrograms);



        try {

            ControllerHelper.setDefaultValue(cfgMgrPrograms, "id");

            ControllerHelper.reorderProperty(cfgMgrPrograms);



            int result = iCfgMgrProgramSV.batchOperate(cfgMgrPrograms);

            if(result > 0) {

                return ResultData.success(cfgMgrPrograms);

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

    * 更新项目

    * @param cfgMgrProgram

    * @return

    * @throws Throwable

    */

    @RequestMapping(value = "/updateByAjax.json")

    @ResponseBody

    public ResultData update(@ModelAttribute("cfgMgrProgram") CfgMgrProgram cfgMgrProgram) {

        logger.debug("request : {}", cfgMgrProgram);

        try {

            ControllerHelper.setDefaultValue(cfgMgrProgram, "id");

            int result = iCfgMgrProgramSV.update(cfgMgrProgram);

            if(result > 0) {

                return ResultData.success(cfgMgrProgram);

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

    * 创建或更新项目

    * @param cfgMgrProgram

    * @return

    * @throws Throwable

    */

    @RequestMapping(value = "/saveOrUpdateByAjax.json")

    @ResponseBody

    public ResultData saveOrUpdate(@ModelAttribute("cfgMgrProgram") CfgMgrProgram cfgMgrProgram) {

        logger.debug("request : {}", cfgMgrProgram);

        try {

            ControllerHelper.setDefaultValue(cfgMgrProgram, "id");

            int result = iCfgMgrProgramSV.batchOperate(new CfgMgrProgram[]{cfgMgrProgram});

            if(result > 0) {

                return ResultData.success(cfgMgrProgram);

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

    * 删除项目

    * @param cfgMgrProgram

    * @return

    * @throws Throwable

    */

    @RequestMapping(value = "/deleteByAjax.json")

    @ResponseBody

    public ResultData delete(@ModelAttribute("cfgMgrProgram") CfgMgrProgram cfgMgrProgram) {

        logger.debug("request : {}", cfgMgrProgram);



        try {

            ControllerHelper.setDefaultValue(cfgMgrProgram, "id");

            int result = iCfgMgrProgramSV.delete(cfgMgrProgram);

            if(result > 0) {

                return ResultData.success(cfgMgrProgram);

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

 	

	public ICfgMgrProgramSV getICfgMgrProgramSV(){

		return iCfgMgrProgramSV;

	}

	//setter

	public void setICfgMgrProgramSV(ICfgMgrProgramSV iCfgMgrProgramSV){

    	this.iCfgMgrProgramSV = iCfgMgrProgramSV;

    }

}
