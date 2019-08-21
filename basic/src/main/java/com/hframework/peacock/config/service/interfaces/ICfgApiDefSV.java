package com.hframework.peacock.config.service.interfaces;

import java.util.*;

import com.hframework.peacock.config.domain.model.CfgApiDef;
import com.hframework.peacock.config.domain.model.CfgApiDef_Example;


public interface ICfgApiDefSV   {

  
    /**
    * 创建API瀹氫箟琛�
    * @param cfgApiDef
    * @return
    * @throws Exception
    */
    public int create(CfgApiDef cfgApiDef) throws  Exception;

    /**
    * 批量维护API瀹氫箟琛�
    * @param cfgApiDefs
    * @return
    * @throws Exception
    */
    public int batchOperate(CfgApiDef[] cfgApiDefs) throws  Exception;
    /**
    * 更新API瀹氫箟琛�
    * @param cfgApiDef
    * @return
    * @throws Exception
    */
    public int update(CfgApiDef cfgApiDef) throws  Exception;

    /**
    * 通过查询对象更新API瀹氫箟琛�
    * @param cfgApiDef
    * @param example
    * @return
    * @throws Exception
    */
    public int updateByExample(CfgApiDef cfgApiDef, CfgApiDef_Example example) throws  Exception;

    /**
    * 删除API瀹氫箟琛�
    * @param cfgApiDef
    * @return
    * @throws Exception
    */
    public int delete(CfgApiDef cfgApiDef) throws  Exception;


    /**
    * 删除API瀹氫箟琛�
    * @param cfgApiDefId
    * @return
    * @throws Exception
    */
    public int delete(long cfgApiDefId) throws  Exception;


    /**
    * 查找所有API瀹氫箟琛�
    * @return
    */
    public List<CfgApiDef> getCfgApiDefAll()  throws  Exception;

    /**
    * 通过API瀹氫箟琛�ID查询API瀹氫箟琛�
    * @param cfgApiDefId
    * @return
    * @throws Exception
    */
    public CfgApiDef getCfgApiDefByPK(long cfgApiDefId)  throws  Exception;

    /**
    * 通过MAP参数查询API瀹氫箟琛�
    * @param params
    * @return
    * @throws Exception
    */
    public List<CfgApiDef> getCfgApiDefListByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询API瀹氫箟琛�
    * @param example
    * @return
    * @throws Exception
    */
    public List<CfgApiDef> getCfgApiDefListByExample(CfgApiDef_Example example) throws  Exception;


    /**
    * 通过MAP参数查询API瀹氫箟琛�数量
    * @param params
    * @return
    * @throws Exception
    */
    public int getCfgApiDefCountByParam(Map<String, Object> params)  throws  Exception;


    /**
    * 通过查询对象查询API瀹氫箟琛�数量
    * @param example
    * @return
    * @throws Exception
    */
    public int getCfgApiDefCountByExample(CfgApiDef_Example example) throws  Exception;


 }
