package com.hframework.peacock.controller.base.descriptor;

import com.hframework.smartweb.bean.SmartChecker;
import com.hframework.smartweb.bean.SmartParser;
import com.hframework.smartweb.bean.apiconf.Checker;
import com.hframework.smartweb.bean.apiconf.Option;
import com.hframework.smartweb.bean.apiconf.Parameter;
import com.hframework.peacock.controller.base.HelperRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class ParameterDescriptor{
    private Parameter parameter;
    private boolean required = false;
    private String defaultValue = null;
    private Class type = String.class;
    private Long min = Long.MIN_VALUE;
    private Long max = Long.MAX_VALUE;
    private Class<? extends SmartParser> parser = null;
    private Map<Class<? extends SmartChecker>, String> checker = null;
    private Class<? extends Enum<?>> enums = null;

    private String[] options = null;

    public ParameterDescriptor(Parameter parameter) {
        this.parameter = parameter;
        if(StringUtils.isNoneBlank(parameter.getRequired())) {
            required = Boolean.parseBoolean(parameter.getRequired());
        }
        if(StringUtils.isNoneBlank(parameter.getType())) {
            type = HelperRegistry.getType(parameter.getType());
        }
        if(StringUtils.isNoneBlank(parameter.getMin())) {
            min = Long.parseLong(parameter.getMin());
        }
        if(StringUtils.isNoneBlank(parameter.getMax())) {
            max = Long.parseLong(parameter.getMax());
        }
        if(parameter.getParser() != null && StringUtils.isNoneBlank(parameter.getParser().getClazz())) {
            parser = HelperRegistry.getParser(parameter.getParser().getClazz());
        }


        if(StringUtils.isNoneBlank(parameter.getDefaultValue())) {
            defaultValue = parameter.getDefaultValue();
        }

        if(parameter.getCheckerList() != null && parameter.getCheckerList().size() > 0) {
            checker = new HashMap<>();
            for (Checker aChecker : parameter.getCheckerList()) {
                checker.put(HelperRegistry.getChecker(aChecker.getClazz()), aChecker.getPattern());
            }
        }

        if(parameter.getOptionList() != null && parameter.getOptionList().size() > 0) {
            List<String> optionList = new ArrayList<>();
            for (Option option : parameter.getOptionList()) {
                optionList.add(option.getValue());
                if("true".equals(option.getDefaultValue()) && StringUtils.isBlank(defaultValue)) {
                    defaultValue = option.getValue();
                }
            }
            options = optionList.toArray(new String[0]);
        }

        if(StringUtils.isNoneBlank(parameter.getEnumClass())) {
            enums = HelperRegistry.getEnum(parameter.getEnumClass());
        }

    }

    @Override
    public String toString() {
        return "ParameterDescriptor:" + parameter.getName() + ":" + parameter.getDescription();
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Class getType() {
        return type;
    }
    public Long getMin() {
        return min;
    }

    public Long getMax() {
        return max;
    }

    public String getName() {
        return parameter.getName();
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public Class<? extends SmartParser> getParser() {
        return parser;
    }

    public String getRegex() {
        return parameter.getRegex();
    }

    public String getPattern() {
        return parameter.getParser() != null ? parameter.getParser().getPattern() : null;
    }

    public String[] getOptions() {
        return options;
    }

    public Class<? extends Enum<?>> getEnums() {
        return enums;
    }

    public Map<Class<? extends SmartChecker>, String> getChecker() {
        return checker;
    }
}