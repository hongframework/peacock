package com.hframework.peacock.system.service.impl;

import java.util.*;

import com.hframework.peacock.system.service.interfaces.IOrganizeSV;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.google.common.collect.Lists;
import com.hframework.common.util.ExampleUtils;
import com.hframework.peacock.system.domain.model.Organize;
import com.hframework.peacock.system.domain.model.Organize_Example;
import com.hframework.peacock.system.dao.OrganizeMapper;

@Service("iOrganizeSV")
public class OrganizeSVImpl  implements IOrganizeSV {

	@Resource
	private OrganizeMapper organizeMapper;
  


    /**
    * 创建缁勭粐
    * @param organize
    * @return
    * @throws Exception
    */
    public int create(Organize organize) throws Exception {
        return organizeMapper.insertSelective(organize);
    }

    /**
    * 批量维护缁勭粐
    * @param organizes
    * @return
    * @throws Exception
    */
    public int batchOperate(Organize[] organizes) throws  Exception{
        int result = 0;
        if(organizes != null) {
            for (Organize organize : organizes) {
                if(organize.getOrganizeId() == null) {
                    result += this.create(organize);
                }else {
                    result += this.update(organize);
                }
            }
        }
        return result;
    }

    /**
    * 更新缁勭粐
    * @param organize
    * @return
    * @throws Exception
    */
    public int update(Organize organize) throws  Exception {
        return organizeMapper.updateByPrimaryKeySelective(organize);
    }

    /**
    * 通过查询对象更新缁勭粐
    * @param organize
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(Organize organize, Organize_Example example) throws  Exception {
        return organizeMapper.updateByExampleSelective(organize, example);
    }

    /**
    * 删除缁勭粐
    * @param organize
    * @return
    * @throws Exception
    */
    public int delete(Organize organize) throws  Exception {
        return organizeMapper.deleteByPrimaryKey(organize.getOrganizeId());
    }

    /**
    * 删除缁勭粐
    * @param organizeId
    * @return
    * @throws Exception
    */
    public int delete(long organizeId) throws  Exception {
        return organizeMapper.deleteByPrimaryKey(organizeId);
    }

    /**
    * 查找所有缁勭粐
    * @return
    */
    public List<Organize> getOrganizeAll()  throws  Exception {
        return organizeMapper.selectByExample(new Organize_Example());
    }

    /**
    * 通过缁勭粐ID查询缁勭粐
    * @param organizeId
    * @return
    * @throws Exception
    */
    public Organize getOrganizeByPK(long organizeId)  throws  Exception {
        return organizeMapper.selectByPrimaryKey(organizeId);
    }

    /**
    * 通过父级缁勭粐ID查询缁勭粐树
    * @param organize
    * @return
    * @throws Exception
    */
    public Map<Long, List<Organize>> getOrganizeTreeByParentId(Organize organize, Organize_Example example)  throws  Exception {

        Map<Long, List<Organize>> result = new HashMap<Long, List<Organize>>();

        fillOrganizeTreeCascade(result, Lists.newArrayList(
                organize.getParentOrganizeId() == null ? -1 : organize.getParentOrganizeId() ), example);
        return result;
    }

    private void fillOrganizeTreeCascade(Map<Long, List<Organize>> result, List<Long> parentIds, Organize_Example templateExample)  throws Exception {
        if(parentIds.size() == 0) {
            return ;
        }
        if(templateExample == null) {
            templateExample = new Organize_Example();
        }
        Organize_Example example = ExampleUtils.clone(templateExample);
        if(example.getOredCriteria() == null || example.getOredCriteria().size() == 0) {
            example.createCriteria();
        }

        example.getOredCriteria().get(0).andParentOrganizeIdIn(parentIds);
        List<Organize> organizes = organizeMapper.selectByExample(example);
        if(organizes == null || organizes.size() == 0) {
            return;
        }

        if(parentIds.size() == 1) {
            result.put(parentIds.get(0), organizes);
        }else {
            for (Organize organize : organizes) {
                Long parentId = organize.getParentOrganizeId();
                if(!result.containsKey(parentId)) {
                    result.put(parentId, new ArrayList<Organize>());
                }
                result.get(parentId).add(organize);
            }
        }

        List subIds = new ArrayList();
        for (Organize organize : organizes) {
            subIds.add(organize.getOrganizeId());
        }

        fillOrganizeTreeCascade(result,subIds, templateExample);
    }

    /**
    * 通过MAP参数查询缁勭粐
    * @param params
    * @return
    * @throws Exception
    */
    public List<Organize> getOrganizeListByParam(Map<String, Object> params)  throws  Exception {
        return null;
    }



    /**
    * 通过查询对象查询缁勭粐
    * @param example
    * @return
    * @throws Exception
    */
    public List<Organize> getOrganizeListByExample(Organize_Example example) throws  Exception {
        return organizeMapper.selectByExample(example);
    }

    /**
    * 通过MAP参数查询缁勭粐数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getOrganizeCountByParam(Map<String, Object> params)  throws  Exception {
        return 0;
    }

    /**
    * 通过查询对象查询缁勭粐数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getOrganizeCountByExample(Organize_Example example) throws  Exception {
        return organizeMapper.countByExample(example);
    }


  	//getter
 	
	public OrganizeMapper getOrganizeMapper(){
		return organizeMapper;
	}
	//setter
	public void setOrganizeMapper(OrganizeMapper organizeMapper){
    	this.organizeMapper = organizeMapper;
    }
}
