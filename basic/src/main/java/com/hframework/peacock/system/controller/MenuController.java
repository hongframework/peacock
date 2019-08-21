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
import com.hframework.peacock.system.domain.model.Menu;
import com.hframework.peacock.system.domain.model.Menu_Example;
import com.hframework.peacock.system.service.interfaces.IMenuSV;

@Controller
@RequestMapping(value = "/system/menu")
public class MenuController   {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

	@Resource
	private IMenuSV iMenuSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示鑿滃崟列表
     * @param menu
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("menu") Menu menu,
                                      @ModelAttribute("example") Menu_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", menu, example, pagination);
        try{
            ExampleUtils.parseExample(menu, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< Menu> list = iMenuSV.getMenuListByExample(example);
            pagination.setTotalCount(iMenuSV.getMenuCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }


    /**
    * 查询展示鑿滃崟树
    * @param menu
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/queryTeeByAjax.json")
    @ResponseBody
    public ResultData tree(@ModelAttribute("menu") Menu menu,
                         @ModelAttribute("example") Menu_Example example){
        logger.debug("request : {},{}", menu, example);
        try{
            ExampleUtils.parseExample(menu, example);

            Map<Long, List< Menu>> treeMap =
                    iMenuSV.getMenuTreeByParentId(menu, example);

            return ResultData.success(treeMap);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }

    /**
     * 查询展示鑿滃崟明细
     * @param menu
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("menu") Menu menu){
        logger.debug("request : {},{}", menu.getMenuId(), menu);
        try{
            Menu result = null;
            if(menu.getMenuId() != null) {
                result = iMenuSV.getMenuByPK(menu.getMenuId());
            }else {
                Menu_Example example = ExampleUtils.parseExample(menu, Menu_Example.class);
                List<Menu> list = iMenuSV.getMenuListByExample(example);
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
    * 搜索一个鑿滃崟
    * @param  menu
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" menu") Menu  menu){
        logger.debug("request : {}",  menu);
        try{
            Menu result = null;
            if(menu.getMenuId() != null) {
                result =  iMenuSV.getMenuByPK(menu.getMenuId());
            }else {
                Menu_Example example = ExampleUtils.parseExample( menu, Menu_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<Menu> list =  iMenuSV.getMenuListByExample(example);
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
    * 创建鑿滃崟
    * @param menu
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("menu") Menu menu) {
        logger.debug("request : {}", menu);
        try {
            ControllerHelper.setDefaultValue(menu, "menuId");
            int result = iMenuSV.create(menu);
            if(result > 0) {
                return ResultData.success(menu);
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
    * 批量维护鑿滃崟
    * @param menus
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody Menu[] menus) {
        logger.debug("request : {}", menus);

        try {
            ControllerHelper.setDefaultValue(menus, "menuId");
            ControllerHelper.reorderProperty(menus);

            int result = iMenuSV.batchOperate(menus);
            if(result > 0) {
                return ResultData.success(menus);
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
    * 更新鑿滃崟
    * @param menu
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("menu") Menu menu) {
        logger.debug("request : {}", menu);
        try {
            ControllerHelper.setDefaultValue(menu, "menuId");
            int result = iMenuSV.update(menu);
            if(result > 0) {
                return ResultData.success(menu);
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
    * 创建或更新鑿滃崟
    * @param menu
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("menu") Menu menu) {
        logger.debug("request : {}", menu);
        try {
            ControllerHelper.setDefaultValue(menu, "menuId");
            int result = iMenuSV.batchOperate(new Menu[]{menu});
            if(result > 0) {
                return ResultData.success(menu);
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
    * 删除鑿滃崟
    * @param menu
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("menu") Menu menu) {
        logger.debug("request : {}", menu);

        try {
            ControllerHelper.setDefaultValue(menu, "menuId");
            int result = iMenuSV.delete(menu);
            if(result > 0) {
                return ResultData.success(menu);
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
 	
	public IMenuSV getIMenuSV(){
		return iMenuSV;
	}
	//setter
	public void setIMenuSV(IMenuSV iMenuSV){
    	this.iMenuSV = iMenuSV;
    }
}
