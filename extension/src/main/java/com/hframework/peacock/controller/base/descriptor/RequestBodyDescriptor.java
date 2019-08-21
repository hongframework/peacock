package com.hframework.peacock.controller.base.descriptor;

import com.hframework.peacock.controller.bean.apiconf.Node;
import com.hframework.smartweb.bean.apiconf.Parameter;
import com.hframework.peacock.controller.bean.apiconf.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class RequestBodyDescriptor {

    private Map<String, ThirdNodeDescriptor> inputNodeDescriptors = new LinkedHashMap<>();
    private Map<String, ThirdNodeDescriptor> ruleNodeDescriptors = new LinkedHashMap<>();


    private boolean isDefined;

    public RequestBodyDescriptor(List<Node> nodeList) {
        for (Node node : nodeList) {
            ThirdNodeDescriptor descriptor = new ThirdNodeDescriptor(node);
            if(descriptor.isInput()) {
                inputNodeDescriptors.put(node.getPath(), descriptor);
            }else {
                ruleNodeDescriptors.put(node.getPath(), descriptor);
            }

        }
        isDefined = !nodeList.isEmpty();
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
}