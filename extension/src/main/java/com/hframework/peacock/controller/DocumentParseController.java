package com.hframework.peacock.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.frame.ServiceFactory;
import com.hframework.common.util.StringUtils;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.common.util.collect.bean.Grouper;
import com.hframework.monitor.ConfigMonitor;
import com.hframework.smartweb.SmartExpanderFactory;
import com.hframework.smartweb.SmartHandlerFactory;
import com.hframework.smartweb.SmartResultHelper;
import com.hframework.smartweb.annotation.SmartApi;
import com.hframework.smartweb.annotation.SmartHolder;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.HolderParser;
import com.hframework.smartweb.bean.SmartChecker;
import com.hframework.smartweb.bean.SmartParser;
import com.hframework.smartweb.bean.SmartPattern;
import com.hframework.smartweb.bean.apiconf.Option;
import com.hframework.smartweb.bean.apiconf.Parser;
import com.hframework.smartweb.bean.apiconf.Result;
import com.hframework.smartweb.bean.apiconf.Schema;
import com.hframework.springext.requestmapping.SmartRequestMappingHandlerMapping;
import com.hframework.tracer.PeacockSystemCenter;
import com.hframework.peacock.controller.base.ApiConfigureRegistry;
import com.hframework.peacock.controller.base.ApiManager;
import com.hframework.peacock.controller.base.descriptor.ApiDescriptor;
import com.hframework.peacock.controller.base.descriptor.ParameterDescriptor;
import com.hframework.peacock.config.domain.model.CfgApiConf;
import com.hframework.peacock.config.domain.model.CfgMgrVersion;
import com.hframework.peacock.config.domain.model.CfgMgrVersion_Example;
import com.hframework.peacock.config.service.interfaces.ICfgApiConfSV;
import com.hframework.peacock.config.service.interfaces.ICfgMgrVersionSV;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/6/14.
 */
@Controller
@RequestMapping("/extend")
public class DocumentParseController {
    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private static final Logger logger = LoggerFactory.getLogger(DocumentParseController.class);

    private static ConfigMonitor<Map<String, Map<String, String>>> config;

    /**
     * 获取方法所有参数名
     * @param method
     * @return
     */
    public static String[] getParameterNames(Method method) {
        return parameterNameDiscoverer.getParameterNames(method);
    }

    public static void initApiConfigMonitor() throws Exception {
        if(config == null) {
            synchronized (DocumentParseController.class) {
                if(config == null) {
                    config = new ConfigMonitor<Map<String, Map<String, String>>>(3) {
                        @Override
                        public Map<String, Map<String, String>> fetch() throws Exception {
                            List<CfgApiConf> allConfig = ServiceFactory.getService(ICfgApiConfSV.class).getCfgApiConfAll();
                            Map<String, Map<String, String>> configs = new LinkedHashMap();
                            for (CfgApiConf cfgApiConf : allConfig) {
                                if((byte)1 == cfgApiConf.getState()) {
                                    String path = cfgApiConf.getPath();
                                    String version = cfgApiConf.getVersion();
                                    String propKey = cfgApiConf.getPropKey();
                                    String propValue = cfgApiConf.getPropValue();
                                    String configKey = path + ":" + version;
                                    if(!configs.containsKey(configKey)) configs.put(configKey, new LinkedHashMap<String, String>());
                                    configs.get(configKey).put(propKey, propValue);
                                }else if((byte)0 == cfgApiConf.getState()) {
                                    String path = cfgApiConf.getPath();
                                    String version = cfgApiConf.getVersion();
                                    String propKey = cfgApiConf.getPropKey();
                                    String propValue = cfgApiConf.getPropValue();
                                    String configKey = path + ":" + version + ":beta";
                                    if(!configs.containsKey(configKey)) configs.put(configKey, new LinkedHashMap<String, String>());
                                    configs.get(configKey).put(propKey, propValue);
                                }
                            }
                            return configs;
                        }
                    }.ok();
                }
            }
        }
    }

    public static InterfaceInfo getInterfaceInfoFromSmartApi(SmartApi methodAnnotation, HandlerMethod method, String url){
        String[] parameterNames = getParameterNames(method.getMethod());
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setName(methodAnnotation.name());
        interfaceInfo.setUrl(url);
        interfaceInfo.setStatus("【代码实现】");
        interfaceInfo.setDescription(methodAnnotation.description());
        interfaceInfo.setVersion(methodAnnotation.version());
        for (int i=0;i<parameterNames.length;i++){
            SmartHolder annotation = method.getMethodParameters()[i].getParameterAnnotation(SmartHolder.class);
            if(annotation != null) {
                ParameterInfo parameterInfo = new ParameterInfo();
                parameterInfo.setCode(StringUtils.isNotBlank(annotation.name()) ? annotation.name() : parameterNames[i]);
                parameterInfo.setName(StringUtils.isNotBlank(annotation.description()) ? annotation.description() : parameterInfo.getCode());
                parameterInfo.setDefaultValue(annotation.defaultValue());

                parameterInfo.setType(getType(annotation.checker(), annotation.parser(), annotation.pattern(), method.getMethod().getParameterTypes()[i]));
                String description = getDescription(annotation.enums(), annotation.options(), annotation.optionJson(),annotation.min(),
                        annotation.max(), annotation.regex(), annotation.checker(), annotation.pattern(), null);
                Class<? extends HolderParser> parser = annotation.parser();
                if(!parser.equals(HolderParser.class)) {
                    description += "满足规则：" + parser.getSimpleName() + "<br/>";
                }

                if(annotation.checker().equals(SmartChecker.class)
                        && !annotation.pattern().equals(SmartPattern.None)) {
                    description += "满足规则：" + annotation.pattern().getPattern() + "<br/>";
                }
                parameterInfo.setDescription(description);

                if(config.getObject() != null && config.getObject().containsKey(url + ":" + methodAnnotation.version())) {
                    parameterInfo.setCurValue(config.getObject().get(url + ":" + methodAnnotation.version()).
                            get(StringUtils.isNotBlank(annotation.name()) ? annotation.name() : parameterNames[i]));
                }
                if(config.getObject() != null && config.getObject().containsKey(url + ":" + methodAnnotation.version()+ ":beta")) {
                    parameterInfo.setTestValue(config.getObject().get(url + ":" + methodAnnotation.version() + ":beta").
                            get(StringUtils.isNotBlank(annotation.name()) ? annotation.name() : parameterNames[i]));
                }

                interfaceInfo.addStaticParameter(parameterInfo);
            }else {
                SmartParameter ann = method.getMethodParameters()[i].getParameterAnnotation(SmartParameter.class);
                ParameterInfo parameterInfo = new ParameterInfo();
                if(ann != null) {
                    parameterInfo.setType(getType(ann.checker(), ann.parser(), ann.pattern(), method.getMethod().getParameterTypes()[i]));
                }else {
                    parameterInfo.setType(String.class.getSimpleName().toLowerCase());
                }

                updateParameterByAnnotation(parameterNames, i, ann, parameterInfo);
                interfaceInfo.addBusinessParameter(parameterInfo);
            }

        }
        return interfaceInfo;
    }

    public static InterfaceInfo getApiInterfaceInfo(String program, String path){
        Map<RequestMappingInfo, HandlerMethod> map = ServiceFactory.getService(SmartRequestMappingHandlerMapping.class).getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            String url = m.getKey().getPatternsCondition().getPatterns().iterator().next();
            if(url.equals(path)) {
                HandlerMethod method = m.getValue();
                SmartApi methodAnnotation = method.getMethodAnnotation(SmartApi.class);
                if(methodAnnotation == null) continue;
                InterfaceInfo interfaceInfo = getInterfaceInfoFromSmartApi(methodAnnotation, method, url);
                return interfaceInfo;
            }
        }
        TreeMap<String, ApiDescriptor> apis = ApiConfigureRegistry.getDefaultInstance().getApis();
        for (String pathAndVersion : apis.keySet()) {
            String url = pathAndVersion.substring(0, pathAndVersion.lastIndexOf("/"));
            if(url.equals("/" + program + path.substring("/api".length()))) {
                String version = pathAndVersion.substring(pathAndVersion.lastIndexOf("/") + 1);
                ApiDescriptor apiDescriptor = apis.get(pathAndVersion);
                InterfaceInfo interfaceInfo =getInterfaceInfoFromApiDescriptor(apiDescriptor, path, version);
                return interfaceInfo;
            }
        }
        return null;
    }

    public  void addInterfaceInfo(List<InterfaceInfo> list, Set<TreeItem> apiSet, InterfaceInfo interfaceInfo, String reqVersion, String reqStatus, String url, String name) {

        String reqStatusText = StringUtils.isBlank(reqStatus) || "null".equals(reqStatus) ? "" : ApiDescriptor.ApiState.parseFrom(reqStatus).toText();

        boolean versionCheck = StringUtils.isBlank(reqVersion)
                || interfaceInfo.getVersion().equals(reqVersion)
                || ("-1".equals(reqVersion) && "null".equals(interfaceInfo.getVersion()));
        boolean statusCheck = StringUtils.isBlank(reqStatus)
                || interfaceInfo.getStatus().equals(reqStatusText);
        if(versionCheck && statusCheck) {
            list.add(interfaceInfo);
            parseListInfo(apiSet, url, "name");
        }
    }

    @RequestMapping(value = "/apidoc.json")
    @ResponseBody
    public ResultData apidoc(HttpServletRequest request) throws Exception {
        initApiConfigMonitor();
        String programId = request.getParameter("programId");
        String reqVersion = request.getParameter("version");
        String reqStatus = request.getParameter("status");
        if(StringUtils.isBlank(programId)) {
            programId = PeacockSystemCenter.mainProgram;
        }
        final List<InterfaceInfo> list = new ArrayList<InterfaceInfo>();
        Map<RequestMappingInfo, HandlerMethod> map = ServiceFactory.getService(SmartRequestMappingHandlerMapping.class).getHandlerMethods();

        Set<TreeItem> apiSet = new HashSet<TreeItem>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            String url = m.getKey().getPatternsCondition().getPatterns().iterator().next();

            HandlerMethod method = m.getValue();
            SmartApi methodAnnotation = method.getMethodAnnotation(SmartApi.class);
            if(methodAnnotation == null) continue;
            if(methodAnnotation.owners().length != 0) {
                for (String own : methodAnnotation.owners()) {
                    if(programId.equals(own)) {
                        InterfaceInfo interfaceInfo = getInterfaceInfoFromSmartApi(methodAnnotation, method, url);
                        addInterfaceInfo(list, apiSet, interfaceInfo, reqVersion, reqStatus, url, "name");
                        break;

                    }
                }
            }else {
                InterfaceInfo interfaceInfo = getInterfaceInfoFromSmartApi(methodAnnotation, method, url);
                addInterfaceInfo(list, apiSet, interfaceInfo, reqVersion, reqStatus, url, "name");
            }
        }


        ApiConfigureRegistry apiRegistry = ApiConfigureRegistry.getDefaultInstance();
//        Map<String, String> apiDefaultPaths = apiRegistry.getApiDefaultPaths();
        TreeMap<String, ApiDescriptor> apis = apiRegistry.getApis();
        for (String pathAndVersion : apis.keySet()) {
            if(programId != null && pathAndVersion.startsWith("/" + programId)) {
                String path = "/api" + pathAndVersion.substring(programId.length() + 1, pathAndVersion.lastIndexOf("/"));
                String version = pathAndVersion.substring(pathAndVersion.lastIndexOf("/") + 1);
                ApiDescriptor apiDescriptor = apis.get(pathAndVersion);
                InterfaceInfo interfaceInfo =getInterfaceInfoFromApiDescriptor(apiDescriptor, path, version);
                addInterfaceInfo(list, apiSet, interfaceInfo, reqVersion, reqStatus, path/* + "<i class='icon-magic red\">"*/, "name");
            }

        }

        Collections.sort(list, new Comparator<InterfaceInfo>() {
            public int compare(InterfaceInfo o1, InterfaceInfo o2) {
                return o1.getUrl().compareTo(o2.getUrl());
            }
        });

        ArrayList<TreeItem> treeItems = Lists.newArrayList(apiSet);
        Collections.sort(treeItems, new Comparator<TreeItem>() {
            public int compare(TreeItem o1, TreeItem o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        final Map<String, List<TreeItem>> group = CollectionUtils.group(treeItems, new Grouper<String, TreeItem>() {
            public <K> K groupKey(TreeItem treeItem) {
                return (K) treeItem.getPid();
            }
        });

        return buildResultData(programId, "manager", list, group, request);
    }

    public static InterfaceInfo getInterfaceInfoFromApiDescriptor(ApiDescriptor apiDescriptor, String path, String version) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setName(apiDescriptor.getTitle());
        interfaceInfo.setStatus(apiDescriptor.getState().toText());
        interfaceInfo.setUrl(path);
        interfaceInfo.setDescription(apiDescriptor.getDescription());
        interfaceInfo.setVersion(version);


        interfaceInfo.setParameterInfos(CollectionUtils.fetch(apiDescriptor.getParameterDescriptors(), new Fetcher<ParameterDescriptor, ParameterInfo>() {
            @Override
            public ParameterInfo fetch(ParameterDescriptor parameterDescriptor) {

                ParameterInfo parameterInfo = new ParameterInfo();
                parameterInfo.setCode(parameterDescriptor.getParameter().getName());
                parameterInfo.setName(parameterDescriptor.getParameter().getDescription());
                parameterInfo.setDefaultValue(parameterDescriptor.getParameter().getDefaultValue());
                String jsonOption = null;
                if(parameterDescriptor.getParameter().getOptionList() != null) {
                    JSONObject optionJson = new JSONObject(true);
                    for (Option option : parameterDescriptor.getParameter().getOptionList()) {
                        optionJson.put(option.getValue(), option.getText() + ("true".equals(option.getDefaultValue()) ? "（默认)" : ""));
                    }
                    jsonOption = optionJson.toJSONString();
                }
                String description = getDescription(SmartParameter.None.class, null, jsonOption, parameterDescriptor.getMin(),
                        parameterDescriptor.getMax(), parameterDescriptor.getRegex(), SmartChecker.class, null, parameterDescriptor.getParameter().getParser());
                parameterInfo.setDescription(description);
                parameterDescriptor.getOptions();
                parameterInfo.setType(parameterDescriptor.getParameter().getType());
                parameterInfo.setRequired("true".equals(parameterDescriptor.getParameter().getRequired())? true : false);
                return parameterInfo;
            }
        }));
        fillResponseByApiDescriptor(interfaceInfo, apiDescriptor);

        return interfaceInfo;
    }


    public static List<InterfaceInfo> getHandlerInfo(String program, String url, TreeMap<String, SmartHandlerFactory.HandlerInfo[]> versionInfo){
        List<InterfaceInfo> list = new ArrayList<>();
        for (Map.Entry<String, SmartHandlerFactory.HandlerInfo[]> entry : versionInfo.entrySet()) {
            SmartHandlerFactory.HandlerInfo[] handlerInfos = entry.getValue();
            SmartHandlerFactory.HandlerInfo singleMethod = handlerInfos[0];
            InterfaceInfo interfaceInfo = new InterfaceInfo();
            interfaceInfo.setName(singleMethod.getTitle());
            interfaceInfo.setUrl("/handler" + url);
            interfaceInfo.setDescription("");
            interfaceInfo.setVersion(entry.getKey());

            if(singleMethod.getMethod() != null) {
                interfaceInfo.setStatus("【代码实现】");
                String[] parameterNames = getParameterNames(singleMethod.getMethod());
                for (int i=0;i<parameterNames.length;i++){
                    HandlerMethod method = new HandlerMethod(ServiceFactory.getService(singleMethod.getMethod().getDeclaringClass()), singleMethod.getMethod());
                    SmartHolder annotation = method.getMethodParameters()[i].getParameterAnnotation(SmartHolder.class);
                    if(annotation != null) {
                        interfaceInfo.addStaticParameter(getStaticParameterInfo(method, annotation, parameterNames, i, url, entry.getKey()));
                    }else{
                        interfaceInfo.addBusinessParameter(getBusinessParameterInfo(method, parameterNames, i, url, entry.getKey()));
                    }
                }
            }else {
                Map<String, Class> parameters = singleMethod.getParameters();
                Map<String, String> parameterCodeAndName = singleMethod.getParameterCodeAndName();
                Set<String> requiredParameters = singleMethod.getRequiredParameters();
                Map<String, Object> parameterDefaultValues = singleMethod.getParameterDefaultValues();
                for (Map.Entry<String, Class> stringClassEntry : parameters.entrySet()) {
                    String parameterName = stringClassEntry.getKey();
                    Class parameterType = stringClassEntry.getValue();
                    ParameterInfo parameterInfo = new ParameterInfo();
                    parameterInfo.setCode(parameterName);
                    parameterInfo.setName(parameterCodeAndName.get(parameterName));
                    if(parameterType == null) {
                        parameterInfo.setType("未知");
                    }else {
                        parameterInfo.setType(parameterType.getSimpleName().toLowerCase());
                    }
                    parameterInfo.setRequired(requiredParameters.contains(parameterName));
                    if(parameterDefaultValues.containsKey(parameterName)) {
                        parameterInfo.setDefaultValue(String.valueOf(parameterDefaultValues.get(parameterName)));
                    }
                    interfaceInfo.addBusinessParameter(parameterInfo);
                }
                singleMethod.getResultsByHandlesXML();//TODO 设置响应结果
                String module = url.substring(1).substring(0, url.substring(1).indexOf("/"));
                String name = url.substring(1).substring(url.substring(1).indexOf("/") + 1);
                ApiDescriptor descriptor = ApiManager.findHandlerDescriptor(program, module, name, null);
                fillResponseByApiDescriptor(interfaceInfo, descriptor);
                interfaceInfo.setStatus(descriptor.getState().toText());
            }

            logger.info("xml : {}", singleMethod.getXmlContent());
            list.add(interfaceInfo);
        }
        return list;
    }

    public static void fillResponseByApiDescriptor(InterfaceInfo interfaceInfo, final ApiDescriptor descriptor){
        Map<String[], List<Result>> struct = descriptor.getResultStruct().getStruct();
        List<Schema> schemaList = descriptor.getApiConf().getSchemaList();
        final Long program = Long.valueOf(descriptor.getProgram());
        Map<String, String> schemaMap = new HashMap<>();
        if(schemaList != null) {
            for (Schema schema : schemaList) {
                schemaMap.put(schema.getPath(), schema.getDescription());
            }
        }
        for (String[] key : struct.keySet()) {
            String path = key[0];
            boolean isMulti = Boolean.valueOf(key[2]);
            List<ParameterInfo> paramInfos = CollectionUtils.fetch(struct.get(key), new Fetcher<Result, ParameterInfo>() {
                @Override
                public ParameterInfo fetch(Result result) {
                    ParameterInfo parameterInfo = new ParameterInfo();
                    parameterInfo.setCode(StringUtils.isNotBlank(result.getAlias()) ? result.getAlias() : result.getName());
                    parameterInfo.setName(result.getDescription());
                    parameterInfo.setType(result.getType());
                    parameterInfo.setDefaultValue(result.getDefaultValue());
                    refreshParameterInfo(program, parameterInfo, result);

                    return parameterInfo;
                }


            });

            if(StringUtils.isBlank(path)) {
                interfaceInfo.setResponseInfos(Sets.newLinkedHashSet(paramInfos));
            }else {
                ParameterInfo parameterInfo = new ParameterInfo();
                parameterInfo.setCode(path);
                parameterInfo.setName(schemaMap.containsKey(path) ? schemaMap.get(path) : path);
                parameterInfo.setType(isMulti? "object[]" : "object");
                interfaceInfo.addResponseParameter(parameterInfo);
                interfaceInfo.putChildResponseParameter(schemaMap.containsKey(path) ? schemaMap.get(path) + "-" + path : path, path, paramInfos);
            }
        }
    }

    @RequestMapping(value = "/handlerdoc.json")
    @ResponseBody
    public ResultData handlerDoc(HttpServletRequest request) throws Exception {
        String programId = request.getParameter("programId");
        String reqVersion = request.getParameter("version");
        String reqStatus = request.getParameter("status");
        if(StringUtils.isBlank(programId)) {
            programId = PeacockSystemCenter.mainProgram;
        }
        final List<InterfaceInfo> list = new ArrayList<InterfaceInfo>();
        Map<RequestMappingInfo, HandlerMethod> map = ServiceFactory.getService(SmartRequestMappingHandlerMapping.class).getHandlerMethods();

        Set<TreeItem> apiSet = new HashSet<TreeItem>();
        Map<String, TreeMap<String, SmartHandlerFactory.HandlerInfo[]>> handlerInfo = SmartHandlerFactory.getHandlerInfo(programId);
        for (Map.Entry<String, TreeMap<String, SmartHandlerFactory.HandlerInfo[]>> m : handlerInfo.entrySet()) {
            String url = m.getKey();
            List<InterfaceInfo> handlerInfo1 = getHandlerInfo(programId, url, m.getValue());
            for (InterfaceInfo interfaceInfo : handlerInfo1) {
                addInterfaceInfo(list, apiSet, interfaceInfo, reqVersion, reqStatus, "/handler" + url, "name");
            }
//
//            parseListInfo(apiSet, "/handler" + url, "name");
//            list.addAll(getHandlerInfo(programId, url, m.getValue()));
        }


        Collections.sort(list, new Comparator<InterfaceInfo>() {
            public int compare(InterfaceInfo o1, InterfaceInfo o2) {
                return o1.getUrl().compareTo(o2.getUrl());
            }
        });

        ArrayList<TreeItem> treeItems = Lists.newArrayList(apiSet);
        Collections.sort(treeItems, new Comparator<TreeItem>() {
            public int compare(TreeItem o1, TreeItem o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        final Map<String, List<TreeItem>> group = CollectionUtils.group(treeItems, new Grouper<String, TreeItem>() {
            public <K> K groupKey(TreeItem treeItem) {
                return (K) treeItem.getPid();
            }
        });

        return buildResultData(programId, "manager", list, group, request);

    }

    public enum VersionStatus{
        all, miss, devlop, deploy, hist //强升后的历史版本即为历史版本
    }

    public ResultData buildResultData(final String program, final String role, final List<InterfaceInfo> list,
                                      final Map<String, List<TreeItem>> group, HttpServletRequest request) throws Exception {
        String path = request.getRequestURI();
        String parameters = request.getQueryString() == null ? "" : request.getQueryString();
        parameters = parameters.contains("version=") ? parameters.substring(0, parameters.indexOf("version=")) : parameters;
        parameters = parameters.contains("status=") ? parameters.substring(0, parameters.indexOf("status=")) : parameters;
        parameters = parameters.endsWith("&")? parameters.substring(0, parameters.length() - 1) : parameters;

        String rootPath = StringUtils.isNotBlank(parameters) ? (path + "?" + parameters) : path;
        String templatePath = StringUtils.isNotBlank(parameters) ? (path + "?" + parameters + "&") : (path + "?");

        String reqVersion = request.getParameter("version");
        String status = request.getParameter("status");
        String versionExtendString = StringUtils.isBlank(reqVersion) ? "" : "version=" + reqVersion + "&";
        String statusExtendString = StringUtils.isBlank(status) ? "" : "&status=" + status;


        final List<String[]> versionVOs = new ArrayList<>();

        CfgMgrVersion_Example example = new CfgMgrVersion_Example();
        example.createCriteria().andProgramIdEqualTo(Long.valueOf(program));
        List<CfgMgrVersion> versionList = ServiceFactory.getService(ICfgMgrVersionSV.class).getCfgMgrVersionListByExample(example);

        versionVOs.add(new String[]{"所有版本", rootPath, StringUtils.isBlank(reqVersion) ? "" : "switch-hidden"});
        versionVOs.add(new String[]{"缺省版本", templatePath + "version=-1" + statusExtendString, "-1".equals(reqVersion) ? "" : "switch-hidden"});
        VersionStatus qryVersionStatus = "-1".equals(reqVersion) ? VersionStatus.miss : VersionStatus.all;
        for (CfgMgrVersion version : versionList) {
            versionVOs.add(new String[]{ version.getCode() + (version.getStatus() == (byte) 0 ? "(开发中)" : ""),
                    templatePath + "version=" + version.getCode() + statusExtendString,
                    version.getCode().equals(reqVersion) ? "" : "switch-hidden"});

            if(version.getCode().equals(reqVersion)) {
                if(version.getStatus() == (byte) 0) {
                    qryVersionStatus = VersionStatus.devlop;
                }else {
                    qryVersionStatus = VersionStatus.deploy;
                }
            }
        }

        final List<String[]> statusVOs = new ArrayList<>();

        String[] qryAll = {"所有", templatePath + versionExtendString, StringUtils.isBlank(status) ? "" : "switch-hidden"};
        String[] qryDev = {"开发中", templatePath + versionExtendString + "status=0", "0".equals(status) ? "" : "switch-hidden"};
        String[] qryTest = {"测试中", templatePath + versionExtendString + "status=1", "1".equals(status) ? "" : "switch-hidden"};
        String[] qryDeploy = {"运行中", templatePath + versionExtendString + "status=2", "2".equals(status) ? "" : "switch-hidden"};
        String[] qryHist = {"已过时", templatePath + versionExtendString + "status=4", "4".equals(status) ? "" : "switch-hidden"};
        String[] qryCancel = {"已失效", templatePath + versionExtendString + "status=3", "3".equals(status) ? "" : "switch-hidden"};

        statusVOs.add(qryAll);
        switch (qryVersionStatus) {
            case all:
                statusVOs.add(qryDev);
                statusVOs.add(qryTest);
                statusVOs.add(qryDeploy);
                statusVOs.add(qryCancel);
                break;
            case miss:
                statusVOs.add(qryDev);
                statusVOs.add(qryTest);
                statusVOs.add(qryCancel);
                break;
            case devlop:
                statusVOs.add(qryDev);
                statusVOs.add(qryTest);
                statusVOs.add(qryCancel);
                break;
            case deploy:
                statusVOs.add(qryDeploy);
                statusVOs.add(qryHist);
                statusVOs.add(qryCancel);
                break;
            default:

        }


        return ResultData.success(new HashMap<String, Object>() {{
            put("apihome", new HashedMap() {{
                put("role", role);//manager, viewer
                put("list", list);
                put("islist", true);
                put("program", program);
                put("versions", versionVOs);
                put("filterStatus", statusVOs);
            }});
            put("apitree", group);
        }});
    }


    @RequestMapping(value = "/doc.json")
    @ResponseBody
    public ResultData doc(HttpServletRequest request) throws Exception {
        initApiConfigMonitor();
        String path = request.getParameter("path");
        final String programId = request.getParameter("program");
        final List<InterfaceInfo> list = new ArrayList<InterfaceInfo>();
        if(path.startsWith("/api/")){
            InterfaceInfo apiInterfaceInfo = getApiInterfaceInfo(programId, path);
            list.add(apiInterfaceInfo);
        }else {
            path = path.startsWith("/handler")? path.substring("/handler".length()) : path;
            Map<String, TreeMap<String, SmartHandlerFactory.HandlerInfo[]>> handlerInfo = SmartHandlerFactory.getHandlerInfo(programId);
            TreeMap<String, SmartHandlerFactory.HandlerInfo[]> versionInfo = handlerInfo.get(path);
            list.addAll(getHandlerInfo(programId, path, versionInfo));
        }

        return ResultData.success(new HashMap<String, Object>() {{
            put("apihome", new HashedMap() {{
                put("role", "manager");//manager, viewer
                put("list", list);
                put("islist", false);
                put("program", programId);
            }});
        }});
    }

    private static ParameterInfo getBusinessParameterInfo(HandlerMethod method, String[] parameterNames, int i, String url, String key) {
        SmartParameter ann = method.getMethodParameters()[i].getParameterAnnotation(SmartParameter.class);
        ParameterInfo parameterInfo = new ParameterInfo();
        if(ann != null) {
            parameterInfo.setType(getType(ann.checker(), ann.parser(), ann.pattern(), method.getMethod().getParameterTypes()[i]));
        }else {
            parameterInfo.setType(String.class.getSimpleName().toLowerCase());
        }

        updateParameterByAnnotation(parameterNames, i, ann, parameterInfo);
        return parameterInfo;
    }

    private static void updateParameterByAnnotation(String[] parameterNames, int i, SmartParameter ann, ParameterInfo parameterInfo) {
        if(ann != null) {
            parameterInfo.setCode(StringUtils.isNotBlank(ann.name()) ? ann.name() : parameterNames[i]);
            parameterInfo.setName(StringUtils.isNotBlank(ann.description()) ? ann.description() : parameterInfo.getCode());
            parameterInfo.setRequired(ann.required());
            parameterInfo.setDefaultValue(ValueConstants.DEFAULT_NONE.equals(ann.defaultValue()) ? "" : ann.defaultValue());
            String description = getDescription(ann.enums(), ann.options(),ann.optionJson(), ann.min(), ann.max(), ann.regex(), ann.checker(), ann.pattern(), null);
            Class<? extends SmartParser> parser = ann.parser();
            SmartPattern pattern = ann.pattern();
            if(!parser.equals(SmartParser.class)) {
                String patternString = "";
                if(!parser.equals(SmartPattern.None)) {
                    patternString = pattern.getPattern();
                }else {
                    try {
                        patternString = (String) parser.newInstance().defaultPattern().getKey();
                    } catch (Exception e) {
                    }
                }
                if(StringUtils.isNotBlank(patternString)) {
                    description += "满足规则：" + pattern + "<br/>";
                }
            }

            if(ann.checker().equals(SmartChecker.class) && parser.equals(SmartParser.class)
                    && !pattern.equals(SmartPattern.None)) {
                description += "满足规则：" + pattern.getPattern() + "<br/>";
            }
            parameterInfo.setDescription(description);
        }else {
            parameterInfo.setCode(parameterNames[i]);
            parameterInfo.setName(parameterInfo.getCode());
            parameterInfo.setRequired(false);
            parameterInfo.setDefaultValue("");
            parameterInfo.setDescription("");
        }
    }

    private static ParameterInfo getStaticParameterInfo(HandlerMethod method, SmartHolder annotation, String[] parameterNames, int i, String url, String version) {
        ParameterInfo parameterInfo = new ParameterInfo();
        parameterInfo.setCode(StringUtils.isNotBlank(annotation.name()) ? annotation.name() : parameterNames[i]);
        parameterInfo.setName(StringUtils.isNotBlank(annotation.description()) ? annotation.description() : parameterInfo.getCode());
        parameterInfo.setDefaultValue(annotation.defaultValue());

        parameterInfo.setType(getType(annotation.checker(), annotation.parser(), annotation.pattern(), method.getMethod().getParameterTypes()[i]));
        String description = getDescription(annotation.enums(), annotation.options(), annotation.optionJson(),annotation.min(),
                annotation.max(), annotation.regex(), annotation.checker(), annotation.pattern(), null);
        Class<? extends HolderParser> parser = annotation.parser();
        if(!parser.equals(HolderParser.class)) {
            description += "满足规则：" + parser.getSimpleName() + "<br/>";
        }

        if(annotation.checker().equals(SmartChecker.class)
                && !annotation.pattern().equals(SmartPattern.None)) {
            description += "满足规则：" + annotation.pattern().getPattern() + "<br/>";
        }
        parameterInfo.setDescription(description);

        if(config.getObject() != null && config.getObject().containsKey(url + ":" + version)) {
            parameterInfo.setCurValue(config.getObject().get(url + ":" + version).
                    get(StringUtils.isNotBlank(annotation.name()) ? annotation.name() : parameterNames[i]));
        }
        if(config.getObject() != null && config.getObject().containsKey(url + ":" + version + ":beta")) {
            parameterInfo.setTestValue(config.getObject().get(url + ":" + version + ":beta").
                    get(StringUtils.isNotBlank(annotation.name()) ? annotation.name() : parameterNames[i]));
        }

        return parameterInfo;
    }

    private static String getType(Class<? extends SmartChecker> checker, Class parser, SmartPattern pattern, Class<?> aClass) {
        if(checker.equals(SmartChecker.class) && (parser.equals(SmartParser.class) || parser.equals(HolderParser.class))
                && pattern.equals(SmartPattern.None)) {
            if(aClass.equals(int.class)) {
                return Integer.class.getSimpleName().toLowerCase();
            }else{
                return aClass.getSimpleName().toLowerCase();
            }

        }else {
            return String.class.getSimpleName().toLowerCase();
        }
    }

    private static void refreshParameterInfo(Long program, ParameterInfo parameterInfo, Result result) {
        StringBuffer description = new StringBuffer();
        if(StringUtils.isNotBlank(result.getDescription()) && result.getDescription().contains(">>")) {
            parameterInfo.setName(result.getDescription().split(">>")[0].trim());
            description.append(result.getDescription().split(">>")[1].trim());
            parameterInfo.setDescription(result.getDescription().split(">>")[1].trim());
        }

        if(StringUtils.isNotBlank(result.getFormatter())) {
            parameterInfo.setType(SmartResultHelper.mergeType(parameterInfo.getType(),
                    result.getFormatter(), result.getPattern()));
            parameterInfo.setDescription(SmartResultHelper.mergeDescription(description,
                    program,result.getFormatter(), result.getPattern()).toString());
        }
    }



    private static String getDescription(Class<? extends Enum<?>> enums, String[] options, String optionJson, long min,
                                         long max, String regex, Class<? extends SmartChecker> checker, SmartPattern pattern, Parser parser) {
        StringBuffer descrpiton = new StringBuffer();
        if(!enums.equals(SmartParameter.None.class)) {
            List<String> items = new ArrayList<>();
            for (Field declaredField : enums.getDeclaredFields()) {
                if(declaredField.isEnumConstant())
                    items.add(declaredField.getName());
            }
            SmartResultHelper.appendDescriptionOptions(descrpiton, items);
        }
        if(options != null && options.length != 0) {
            SmartResultHelper.appendDescriptionOptions(descrpiton, Arrays.asList(options));
        }
        if(StringUtils.isNotBlank(optionJson)){
            SmartResultHelper.appendDescriptionOptions(descrpiton, JSONObject.parseObject(optionJson));
        }
        SmartResultHelper.appendDescriptionMinMax(descrpiton, min, max);
        SmartResultHelper.appendDescriptionRule(descrpiton, regex);

        if(parser != null) {
            String parseInfo = SmartExpanderFactory.getExpanderParameterDescription(parser.getClazz(), parser.getPattern());
            if(StringUtils.isNotBlank(parseInfo)) {
                SmartResultHelper.appendDescriptionLine(descrpiton, /*"满足格式：&nbsp;" + */parseInfo);
            }
        }

        if(!checker.equals(SmartChecker.class)) {
            String patternString = "";
            if(!pattern.equals(SmartPattern.None)) {
                patternString = pattern.getPattern();
            }else {
                try {
                    patternString = (String) checker.newInstance().defaultPattern().getKey();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            SmartResultHelper.appendDescriptionRule(descrpiton, patternString);
        }
        return descrpiton.toString();
    }

    private void parseListInfo(Set<TreeItem> apiSet, String url, String name) {
        if(apiSet.contains(url) && StringUtils.isBlank(url)) return;
        String parentPath = url.substring(0, url.lastIndexOf("/"));
        if(StringUtils.isNotBlank(parentPath)) {
            apiSet.add(new TreeItem(url, parentPath, url.substring(url.lastIndexOf("/")+1)));
            parseListInfo(apiSet, parentPath, name);
        }else {
            apiSet.add(new TreeItem(url, "-1", url.substring(url.lastIndexOf("/")+1)));
        }

    }

    public static class TreeItem{
        private String id;
        private String pid;
        private String name;
        private String url;
        private String icon;

        public TreeItem(String id, String pid, String name) {
            this.id = id;
            this.pid = pid;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof String) {
                return this.id.equals(obj);

            }
            return this.id.equals(((TreeItem)obj).getId());
        }
    }

    public static class InterfaceInfo {
        private String url;
        private String name;
        private String description;
        private String version;
        private String status;


        private List<ParameterInfo> publicInfos;
        private List<ParameterInfo> parameterInfos;
        private List<ParameterInfo> staticInfos;

        private Set<ParameterInfo> responseInfos;

        private Map<ParameterInfo, List<ParameterInfo>> childResponseInfos;



        public void addPublicParameter(ParameterInfo parameterInfo) {
            if(publicInfos ==null)
                publicInfos = new ArrayList<ParameterInfo>();
            publicInfos.add(parameterInfo);
        }

        public void addBusinessParameter(ParameterInfo parameterInfo) {
            if(parameterInfos ==null)
                parameterInfos = new ArrayList<ParameterInfo>();
            parameterInfos.add(parameterInfo);
        }

        public void addStaticParameter(ParameterInfo parameterInfo) {
            if(staticInfos ==null)
                staticInfos = new ArrayList<ParameterInfo>();
            staticInfos.add(parameterInfo);
        }

        public void addResponseParameter(ParameterInfo parameterInfo) {
            if(responseInfos ==null)
                responseInfos = new LinkedHashSet<>();
            responseInfos.add(parameterInfo);
        }

        public void putChildResponseParameter(String name, String code, List<ParameterInfo> subs) {
            if(childResponseInfos ==null)
                childResponseInfos = new LinkedHashMap<>();
            ParameterInfo key = new ParameterInfo();
            key.setCode(code);
            key.setName(name);
            childResponseInfos.put(key, subs);
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<ParameterInfo> getPublicInfos() {
            return publicInfos;
        }

        public void setPublicInfos(List<ParameterInfo> publicInfos) {
            this.publicInfos = publicInfos;
        }

        public List<ParameterInfo> getParameterInfos() {
            return parameterInfos;
        }

        public void setParameterInfos(List<ParameterInfo> parameterInfos) {
            this.parameterInfos = parameterInfos;
        }

        public List<ParameterInfo> getStaticInfos() {
            return staticInfos;
        }

        public void setStaticInfos(List<ParameterInfo> staticInfos) {
            this.staticInfos = staticInfos;
        }

        public Set<ParameterInfo> getResponseInfos() {
            return responseInfos;
        }

        public void setResponseInfos(Set<ParameterInfo> responseInfos) {
            this.responseInfos = responseInfos;
        }

        public Map<ParameterInfo, List<ParameterInfo>> getChildResponseInfos() {
            return childResponseInfos;
        }

        public void setChildResponseInfos(Map<ParameterInfo, List<ParameterInfo>> childResponseInfos) {
            this.childResponseInfos = childResponseInfos;
        }
    }
    public static class ParameterInfo {
        private String name;
        private String code;
        private String type;
        private boolean required;
        private String defaultValue;
        private String description;
        private String curValue;
        private String testValue;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCurValue() {
            return curValue;
        }

        public void setCurValue(String curValue) {
            this.curValue = curValue;
        }

        public String getTestValue() {
            return testValue;
        }

        public void setTestValue(String testValue) {
            this.testValue = testValue;
        }
    }
}
