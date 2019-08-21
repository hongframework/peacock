package com.hframework.peacock.controller.base.descriptor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.peacock.controller.base.NodeMeta;
import com.hframework.peacock.controller.bean.apiconf.Node;
import com.hframework.peacock.controller.base.NodeMeta;
import com.hframework.peacock.controller.bean.apiconf.Node;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class ResponseBodyDescriptor {
    private Map<String, ThirdNodeDescriptor> inputNodeDescriptors = new LinkedHashMap<>();
    private Map<String, ThirdNodeDescriptor> ruleNodeDescriptors = new LinkedHashMap<>();


    private boolean isDefined;
    private NodeMeta meta;

    public ResponseBodyDescriptor(List<Node> nodeList) {
        for (Node node : nodeList) {
            ThirdNodeDescriptor descriptor = new ThirdNodeDescriptor(node);
            if(descriptor.isInput()) {
                inputNodeDescriptors.put(node.getPath(), descriptor);
            }else {
                ruleNodeDescriptors.put(node.getPath(), descriptor);
            }
        }
        isDefined = !nodeList.isEmpty();

        List<String> paths = CollectionUtils.fetch(nodeList, new Fetcher<Node, String>() {
            @Override
            public String fetch(Node node) {
                return node.getPath();
            }
        });
        meta = NodeMeta.getInstanceByPaths(paths);

    }

    public Map<String, ThirdNodeDescriptor> getInputNodeDescriptors() {
        return inputNodeDescriptors;
    }

    public Map<String, ThirdNodeDescriptor> getRuleNodeDescriptors() {
        return ruleNodeDescriptors;
    }

    public boolean isDefined() {
        return isDefined;
    }
    @JsonIgnore
    public NodeMeta getMeta() {
        return meta;
    }
}