package com.hframework.peacock.controller.base.descriptor;

import com.hframework.smartweb.bean.apiconf.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class ParametersDescriptor {
    private List<ParameterDescriptor> parameters;

    public ParametersDescriptor(List<Parameter> parameterList) {
        parameters = new ArrayList<>();
        for (Parameter parameter : parameterList) {
            parameters.add(new ParameterDescriptor(parameter));
        }
    }

    public static enum FindScope{
        parameter, handler, global;

        public static FindScope parse(String text) {
            try {
                return FindScope.valueOf(text);
            }catch (Exception e) {
                return global;
            }

        }

        public static boolean fromParameter(String text){
            return parse(text) == parameter || parse(text) == global;
        }
    }

    public List<ParameterDescriptor> getParameters() {
        return parameters;
    }
}