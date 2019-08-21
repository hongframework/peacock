package com.hframework.peacock.config;

import com.google.common.collect.Lists;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.peacock.config.domain.model.CfgMgrProgram;
import com.hframework.peacock.config.domain.model.CfgMgrProgram_Example;
import com.hframework.peacock.config.domain.model.CfgNode;
import com.hframework.peacock.config.domain.model.CfgNode_Example;
import com.hframework.monitor.ConfigMonitor;
import com.hframework.monitor.Monitor;
import com.hframework.monitor.MonitorListener;
import com.hframework.tracer.PeacockSystemCenter;
import com.hframework.peacock.config.service.interfaces.ICfgMgrProgramSV;
import com.hframework.peacock.config.service.interfaces.ICfgNodeSV;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

/**
 * Created by zhangquanhong on 2017/12/22.
 */
public class PeacockSystemLoader implements ApplicationListener<ContextRefreshedEvent> , MonitorListener<List<CfgNode>> {

    private static Logger logger = LoggerFactory.getLogger(PeacockSystemLoader.class);

    private ConfigMonitor<List<CfgNode>> handlesMonitor = null;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() != null){
            return;
        }
        ApplicationContext context = contextRefreshedEvent.getApplicationContext();
        PeacockSystemCenter.nodeCode = System.getProperty("node.code");
        logger.info("nodeCode = {}", PeacockSystemCenter.nodeCode);
        if (StringUtils.isBlank(PeacockSystemCenter.nodeCode)) {
//            throw new RuntimeException("node-id is blank !");
            CfgMgrProgram_Example example = new CfgMgrProgram_Example();
            example.createCriteria();
            try {
                List<CfgMgrProgram> cfgMgrPrograms = context.getBean(ICfgMgrProgramSV.class).getCfgMgrProgramListByExample(example);
                List<String> programList = CollectionUtils.fetch(cfgMgrPrograms, new Fetcher<CfgMgrProgram, String>() {
                    @Override
                    public String fetch(CfgMgrProgram cfgMgrProgram) {
                        return String.valueOf(cfgMgrProgram.getId());
                    }
                });

                if(programList.size() > 0) {
                    PeacockSystemCenter.mainProgram = programList.remove(0);
                    PeacockSystemCenter.maybePrograms = programList;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            final ICfgNodeSV sv = context.getBean(ICfgNodeSV.class);
            try {

                handlesMonitor = new ConfigMonitor<List<CfgNode>>(60) {
                    @Override
                    public List<CfgNode> fetch() throws Exception {
                        CfgNode_Example example = new CfgNode_Example();
                        example.createCriteria().andCodeEqualTo(PeacockSystemCenter.nodeCode).andStatusEqualTo((byte) 1);
                        return sv.getCfgNodeListByExample(example);
                    }
                };
                handlesMonitor.addListener(this);
                handlesMonitor.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onEvent(Monitor<List<CfgNode>> monitor) throws ClassNotFoundException, Exception {
        List<CfgNode> cfgNodes = monitor.getObject();
        if(cfgNodes == null || cfgNodes.isEmpty()) return;
        CfgNode cfgNode = cfgNodes.get(0);
        PeacockSystemCenter.nodeType = PeacockSystemCenter.NodeType.parse(cfgNode.getType());
        if(StringUtils.isNotBlank(cfgNode.getProgramId())) {
            List<String> programList = Lists.newArrayList(cfgNode.getProgramId().split(","));

            if(programList.size() > 0) {
                PeacockSystemCenter.mainProgram = programList.remove(0);
                PeacockSystemCenter.maybePrograms = programList;
            }
        }

    }
}
