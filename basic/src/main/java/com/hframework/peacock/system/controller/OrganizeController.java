package com.hframework.peacock.system.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
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
import com.hframework.peacock.system.domain.model.Organize;
import com.hframework.peacock.system.domain.model.Organize_Example;
import com.hframework.peacock.system.service.interfaces.IOrganizeSV;

@Controller
@RequestMapping(value = "/system/organize")
public class OrganizeController   {
    private static final Logger logger = LoggerFactory.getLogger(OrganizeController.class);

	@Resource
	private IOrganizeSV iOrganizeSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示缁勭粐列表
     * @param organize
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("organize") Organize organize,
                                      @ModelAttribute("example") Organize_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", organize, example, pagination);
        try{
            ExampleUtils.parseExample(organize, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< Organize> list = iOrganizeSV.getOrganizeListByExample(example);
            pagination.setTotalCount(iOrganizeSV.getOrganizeCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }


    /**
    * 查询展示缁勭粐树
    * @param organize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/queryTeeByAjax.json")
    @ResponseBody
    public ResultData tree(@ModelAttribute("organize") Organize organize,
                         @ModelAttribute("example") Organize_Example example){
        logger.debug("request : {},{}", organize, example);
        try{
            ExampleUtils.parseExample(organize, example);

            Map<Long, List< Organize>> treeMap =
                    iOrganizeSV.getOrganizeTreeByParentId(organize, example);

            return ResultData.success(treeMap);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }

    /**
     * 查询展示缁勭粐明细
     * @param organize
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("organize") Organize organize){
        logger.debug("request : {},{}", organize.getOrganizeId(), organize);
        try{
            Organize result = null;
            if(organize.getOrganizeId() != null) {
                result = iOrganizeSV.getOrganizeByPK(organize.getOrganizeId());
            }else {
                Organize_Example example = ExampleUtils.parseExample(organize, Organize_Example.class);
                List<Organize> list = iOrganizeSV.getOrganizeListByExample(example);
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
    * 搜索一个缁勭粐
    * @param  organize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" organize") Organize  organize){
        logger.debug("request : {}",  organize);
        try{
            Organize result = null;
            if(organize.getOrganizeId() != null) {
                result =  iOrganizeSV.getOrganizeByPK(organize.getOrganizeId());
            }else {
                Organize_Example example = ExampleUtils.parseExample( organize, Organize_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<Organize> list =  iOrganizeSV.getOrganizeListByExample(example);
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
    * 创建缁勭粐
    * @param organize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("organize") Organize organize) {
        logger.debug("request : {}", organize);
        try {
            ControllerHelper.setDefaultValue(organize, "organizeId");
            int result = iOrganizeSV.create(organize);
            if(result > 0) {
                return ResultData.success(organize);
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
    * 批量维护缁勭粐
    * @param organizes
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody Organize[] organizes) {
        logger.debug("request : {}", organizes);

        try {
            ControllerHelper.setDefaultValue(organizes, "organizeId");
            ControllerHelper.reorderProperty(organizes);

            int result = iOrganizeSV.batchOperate(organizes);
            if(result > 0) {
                return ResultData.success(organizes);
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
    * 更新缁勭粐
    * @param organize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("organize") Organize organize) {
        logger.debug("request : {}", organize);
        try {
            ControllerHelper.setDefaultValue(organize, "organizeId");
            int result = iOrganizeSV.update(organize);
            if(result > 0) {
                return ResultData.success(organize);
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
    * 创建或更新缁勭粐
    * @param organize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("organize") Organize organize) {
        logger.debug("request : {}", organize);
        try {
            ControllerHelper.setDefaultValue(organize, "organizeId");
            int result = iOrganizeSV.batchOperate(new Organize[]{organize});
            if(result > 0) {
                return ResultData.success(organize);
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
    * 删除缁勭粐
    * @param organize
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("organize") Organize organize) {
        logger.debug("request : {}", organize);

        try {
            ControllerHelper.setDefaultValue(organize, "organizeId");
            int result = iOrganizeSV.delete(organize);
            if(result > 0) {
                return ResultData.success(organize);
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
 	
	public IOrganizeSV getIOrganizeSV(){
		return iOrganizeSV;
	}
	//setter
	public void setIOrganizeSV(IOrganizeSV iOrganizeSV){
    	this.iOrganizeSV = iOrganizeSV;
    }
}
