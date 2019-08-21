package com.hframework.peacock.config.controller;

import com.hframework.beans.controller.Pagination;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.ExampleUtils;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.peacock.config.domain.model.ThirdDomain;
import com.hframework.peacock.config.domain.model.ThirdDomain_Example;
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
import com.hframework.peacock.config.service.interfaces.IThirdDomainSV;

@Controller
@RequestMapping(value = "/config/thirdDomain")
public class ThirdDomainController   {
    private static final Logger logger = LoggerFactory.getLogger(ThirdDomainController.class);

	@Resource
	private IThirdDomainSV iThirdDomainSV;
  





    @InitBinder
    protected void initBinder(HttpServletRequest request,
        ServletRequestDataBinder binder) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor editor = new CustomDateEditor(df, false);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * 查询展示访问域列表
     * @param thirdDomain
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryListByAjax.json")
    @ResponseBody
    public ResultData list(@ModelAttribute("thirdDomain") ThirdDomain thirdDomain,
                           @ModelAttribute("example") ThirdDomain_Example example, Pagination pagination){
        logger.debug("request : {},{},{}", thirdDomain, example, pagination);
        try{
            ExampleUtils.parseExample(thirdDomain, example);
            //设置分页信息
            example.setLimitStart(pagination.getStartIndex());
            example.setLimitEnd(pagination.getEndIndex());

            final List< ThirdDomain> list = iThirdDomainSV.getThirdDomainListByExample(example);
            pagination.setTotalCount(iThirdDomainSV.getThirdDomainCountByExample(example));

            return ResultData.success().add("list",list).add("pagination",pagination);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }



    /**
     * 查询展示访问域明细
     * @param thirdDomain
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/queryOneByAjax.json")
    @ResponseBody
    public ResultData detail(@ModelAttribute("thirdDomain") ThirdDomain thirdDomain){
        logger.debug("request : {},{}", thirdDomain.getId(), thirdDomain);
        try{
            ThirdDomain result = null;
            if(thirdDomain.getId() != null) {
                result = iThirdDomainSV.getThirdDomainByPK(thirdDomain.getId());
            }else {
                ThirdDomain_Example example = ExampleUtils.parseExample(thirdDomain, ThirdDomain_Example.class);
                List<ThirdDomain> list = iThirdDomainSV.getThirdDomainListByExample(example);
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
    * 搜索一个访问域
    * @param  thirdDomain
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/searchOneByAjax.json")
    @ResponseBody
    public ResultData search(@ModelAttribute(" thirdDomain") ThirdDomain  thirdDomain){
        logger.debug("request : {}",  thirdDomain);
        try{
            ThirdDomain result = null;
            if(thirdDomain.getId() != null) {
                result =  iThirdDomainSV.getThirdDomainByPK(thirdDomain.getId());
            }else {
                ThirdDomain_Example example = ExampleUtils.parseExample( thirdDomain, ThirdDomain_Example.class);

                example.setLimitStart(0);
                example.setLimitEnd(1);

                List<ThirdDomain> list =  iThirdDomainSV.getThirdDomainListByExample(example);
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
    * 创建访问域
    * @param thirdDomain
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createByAjax.json")
    @ResponseBody
    public ResultData create(@ModelAttribute("thirdDomain") ThirdDomain thirdDomain) {
        logger.debug("request : {}", thirdDomain);
        try {
            ControllerHelper.setDefaultValue(thirdDomain, "id");
            int result = iThirdDomainSV.create(thirdDomain);
            if(result > 0) {
                return ResultData.success(thirdDomain);
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
    * 批量维护访问域
    * @param thirdDomains
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/createsByAjax.json")
    @ResponseBody
    public ResultData batchCreate(@RequestBody ThirdDomain[] thirdDomains) {
        logger.debug("request : {}", thirdDomains);

        try {
            ControllerHelper.setDefaultValue(thirdDomains, "id");
            ControllerHelper.reorderProperty(thirdDomains);

            int result = iThirdDomainSV.batchOperate(thirdDomains);
            if(result > 0) {
                return ResultData.success(thirdDomains);
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
    * 更新访问域
    * @param thirdDomain
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/updateByAjax.json")
    @ResponseBody
    public ResultData update(@ModelAttribute("thirdDomain") ThirdDomain thirdDomain) {
        logger.debug("request : {}", thirdDomain);
        try {
            ControllerHelper.setDefaultValue(thirdDomain, "id");
            int result = iThirdDomainSV.update(thirdDomain);
            if(result > 0) {
                return ResultData.success(thirdDomain);
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
    * 创建或更新访问域
    * @param thirdDomain
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/saveOrUpdateByAjax.json")
    @ResponseBody
    public ResultData saveOrUpdate(@ModelAttribute("thirdDomain") ThirdDomain thirdDomain) {
        logger.debug("request : {}", thirdDomain);
        try {
            ControllerHelper.setDefaultValue(thirdDomain, "id");
            int result = iThirdDomainSV.batchOperate(new ThirdDomain[]{thirdDomain});
            if(result > 0) {
                return ResultData.success(thirdDomain);
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
    * 删除访问域
    * @param thirdDomain
    * @return
    * @throws Throwable
    */
    @RequestMapping(value = "/deleteByAjax.json")
    @ResponseBody
    public ResultData delete(@ModelAttribute("thirdDomain") ThirdDomain thirdDomain) {
        logger.debug("request : {}", thirdDomain);

        try {
            ControllerHelper.setDefaultValue(thirdDomain, "id");
            int result = iThirdDomainSV.delete(thirdDomain);
            if(result > 0) {
                return ResultData.success(thirdDomain);
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
 	
	public IThirdDomainSV getIThirdDomainSV(){
		return iThirdDomainSV;
	}
	//setter
	public void setIThirdDomainSV(IThirdDomainSV iThirdDomainSV){
    	this.iThirdDomainSV = iThirdDomainSV;
    }
}
