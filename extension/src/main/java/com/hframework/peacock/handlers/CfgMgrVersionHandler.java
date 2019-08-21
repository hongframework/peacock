package com.hframework.peacock.handlers;

import com.hframework.peacock.config.domain.model.*;
import com.hframework.web.extension.AbstractBusinessHandler;
import com.hframework.web.extension.annotation.BeforeUpdateHandler;
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeApiSV;
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeHandlerSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CfgMgrVersionHandler extends AbstractBusinessHandler<CfgMgrVersion> {

    @Autowired
    private ICfgRuntimeApiSV apiSV;
    @Autowired
    private ICfgRuntimeHandlerSV handlerSV;

    private static final Logger logger = LoggerFactory.getLogger(CfgMgrVersion.class);

    @BeforeUpdateHandler(attr = "status", orig = "0" , target = "1")
    public boolean topicInActive(CfgMgrVersion version, CfgMgrVersion origVersion) throws Exception {

        CfgRuntimeApi_Example example = new CfgRuntimeApi_Example();
        example.createCriteria().andVersionIdEqualTo(version.getId());

        CfgRuntimeApi api = new CfgRuntimeApi();
        api.setState((byte) 2);
        apiSV.updateByExample(api, example);

        CfgRuntimeHandler_Example handlerExample = new CfgRuntimeHandler_Example();
        handlerExample.createCriteria().andVersionIdEqualTo(version.getId());

        CfgRuntimeHandler handler = new CfgRuntimeHandler();
        handler.setState((byte) 2);
        handlerSV.updateByExample(handler, handlerExample);

        return false;
    }

}
