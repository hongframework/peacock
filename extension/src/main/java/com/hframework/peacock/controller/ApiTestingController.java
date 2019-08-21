package com.hframework.peacock.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.util.UrlHelper;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.common.util.message.JsonUtils;
import com.hframework.peacock.controller.base.ApiManager;
import com.hframework.smartweb.APIErrorType;
import com.hframework.smartweb.DynResultVO;
import com.hframework.smartweb.SmartHandlerFactory;
import com.hframework.smartweb.exception.SmartHandlerException;
import com.hframework.tracer.PeacockSampler;
import com.hframework.web.ControllerHelper;
import com.hframework.web.SessionKey;
import com.hframework.web.context.WebContext;
import com.hframework.peacock.controller.base.dc.DC;
import com.hframework.peacock.controller.base.dc.DCUtils;
import com.hframework.peacock.config.domain.model.CfgTestCase;
import com.hframework.peacock.config.domain.model.CfgTestCase_Example;
import com.hframework.peacock.config.service.interfaces.ICfgTestCaseSV;
import com.hframework.peacock.system.domain.model.User;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/apitest")
public class ApiTestingController {
    private static final Logger logger = LoggerFactory.getLogger(ApiTestingController.class);

    public static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    private static final String CASE_ID = "caseId";
    private static final String CASE_NAME = "caseName";
    private static final String CASE_SHARE = "caseShare";
    private static final String CASE_STORE = "caseStore";
    private static final String URL = "url";
    private static final String METHOD = "method";
    private static final String REMARK = "remark";
    private static final String PARAMETERS = "parameters";
    private static final String REQUEST_BODY = "requestBody";
    private static final String RESPONSE_BODY = "responseBody";
    private static final String STORES = "stores";
    private static final String HISTORIES = "histories";
    private static final String SHARES = "shares";
    private static final String MY_SHARES = "myShares";
    private static final String IS_MINE = "isMine";


    @Resource
    private ICfgTestCaseSV testCaseSV;


    @RequestMapping("/init.json")
    @ResponseBody
    public ResultData init(String path, HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        DocumentParseController.initApiConfigMonitor();
        DocumentParseController.InterfaceInfo interfaceInfo = null;
        Map<String, String> urlParameters = UrlHelper.getUrlParameters(request.getHeader("Referer"), false);
        path = urlParameters.get("path");
        String programId = urlParameters.get("program");
        if(path.startsWith("/api/")){
            interfaceInfo = DocumentParseController.getApiInterfaceInfo(programId, path);
        }else {
            String tempPath = path.startsWith("/handler")? path.substring("/handler".length()) : path;
            Map<String, TreeMap<String, SmartHandlerFactory.HandlerInfo[]>> handlerInfo = SmartHandlerFactory.getHandlerInfo(programId);
            TreeMap<String, SmartHandlerFactory.HandlerInfo[]> versionInfo = handlerInfo.get(tempPath);
            List<DocumentParseController.InterfaceInfo> handlerInfo1 = DocumentParseController.getHandlerInfo(programId, tempPath, versionInfo);
            interfaceInfo = handlerInfo1.get(0);
        }

        List<String> paraPairs = CollectionUtils.fetch(interfaceInfo.getParameterInfos(), new Fetcher<DocumentParseController.ParameterInfo, String>() {
            @Override
            public String fetch(DocumentParseController.ParameterInfo param) {
                return param.getCode() + "=" + (StringUtils.isBlank(param.getDefaultValue()) ? "" : param.getDefaultValue());
            }
        });
        String paramString = paraPairs == null ? null : Joiner.on("&").join(paraPairs);
        String url =interfaceInfo.getUrl() + (StringUtils.isBlank(paramString)? "": ("?" + paramString.trim()));

        Map<String, String> parameters = new LinkedHashMap<>();
        ResultData resultData = ResultData.success()
                .add(URL, url)
                .add(METHOD, "post")
                .add(PARAMETERS, parameters);
        getCaseList(resultData, path);
        return resultData;
    }

    @RequestMapping("/exec.json")
    @ResponseBody
    public ResultData exec(String program, String method, String url, String parameter, String body, HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        try {
            String result = null;
            boolean isApi = url.startsWith("/api/");
            String tmpUrl = url;
            if(url.startsWith("/api/")){
                tmpUrl = url.substring("/api/".length());
            }else if(url.startsWith("/handler/")){
                tmpUrl = url.substring("/handler/".length());
            }
            String module = tmpUrl.substring(0, tmpUrl.indexOf("/"));
            String name = tmpUrl.substring((module + "/").length());
            String version = null;
            if(name.substring(name.lastIndexOf("/") +1).matches("[0-9.]+")){
                version = name.substring(name.lastIndexOf("/") + 1);
                name = name.substring(0, name.lastIndexOf("/"));
            }
            Map<String, String> urlParameters = ThirdApiEditController.getUrlParameters("?" + parameter);
            if(isApi){
                MockHttpServletRequest mockRequest = new MockHttpServletRequest();
                for (String key : urlParameters.keySet()) {
                    mockRequest.setParameter(key, urlParameters.get(key));
                }
                result = objToPrettyJson(DynResultVO.success(ApiManager.invoke(program, module, name, version, mockRequest, response).getObject()));
            }else {
//                List<Map<String,Object>> requestMapList = new ArrayList<>();
                Map<String,Object> parameters = new HashMap<>();

                for (String key : urlParameters.keySet()) {
                    parameters.put(key, urlParameters.get(key));
                }
                PeacockSampler.reqTL.set(request.getQueryString());
                DC dc = ApiManager.invokeHandler(program, module, name, version, DCUtils.valueOf(parameters), request, response, null);
                result = objToPrettyJson(DynResultVO.success(DCUtils.getValues(dc)));
            }
            return ResultData.success()
                    .add(RESPONSE_BODY, result.toString());
        }catch (SmartHandlerException e) {
            e.printStackTrace();
            return ResultData.success()
                    .add(RESPONSE_BODY, objToPrettyJson(DynResultVO.from(e.getAPIResultVO())));

        }catch (BusinessException e) {
            e.printStackTrace();
            return ResultData.success()
                    .add(RESPONSE_BODY, objToPrettyJson(DynResultVO.from(e.result())));

        }catch (Exception e) {
            e.printStackTrace();
            return ResultData.success()
                    .add(RESPONSE_BODY, objToPrettyJson(DynResultVO.error(e.getMessage())));

        }
    }

    public static String objToPrettyJson(Object obj){
        try {
            return  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            try {
                return JsonUtils.writeValueAsString(obj);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return "TODO";
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public ResultData save(Long id, String method, String url, String parameter, String body, @ModelAttribute("response") String responseBody){
        try {
            User user = WebContext.getSession(SessionKey.USER);
            if(user == null) {
                return ResultData.error(ResultCode.get("-999", "请先登录！"));
            }
            ResultData resultData = ResultData.success();
            CfgTestCase testCase = new CfgTestCase();
            testCase.setMethod(method);
            testCase.setPath(url);
            testCase.setParameterStr(parameter);
            testCase.setRequestBody(body);
            testCase.setResponseBody(responseBody);
//            testCase.setName();
            ControllerHelper.setDefaultValue(testCase, ControllerHelper.OperateType.UPDATE);
            if(id != null) {
                CfgTestCase_Example example = new CfgTestCase_Example();
                example.createCriteria().andIdEqualTo(id);
                testCaseSV.updateByExample(testCase, example);
            }else {
                ControllerHelper.setDefaultValue(testCase, ControllerHelper.OperateType.CREATE);
                testCase.setName("");
                testCase.setIsPub((byte) 0);
                testCase.setIsStore((byte) 0);
                testCase.setStatus((byte) 1);
                testCaseSV.create(testCase);
                resultData.add(CASE_STORE, 0);
                resultData.add(CASE_SHARE, 0);
                resultData.add(CASE_NAME, "");
            }

            resultData.add(CASE_ID, id != null ? id : testCase.getId());

            return getCaseList(resultData, url);

        }catch (Exception e) {
            e.printStackTrace();
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR);
        }
    }

    @RequestMapping("/select.json")
    @ResponseBody
    public ResultData select(Long id){
        try {
            User user = WebContext.getSession(SessionKey.USER);
            CfgTestCase testCase = testCaseSV.getCfgTestCaseByPK(id);
            String url =testCase.getPath() + (StringUtils.isBlank(testCase.getParameterStr())? "": ("?" + testCase.getParameterStr().trim()));

            Map<String, String> parameters = new LinkedHashMap<>();
            boolean isMine = user != null && testCase.getCreatorId() == user.getUserId();
            ResultData resultData = ResultData.success()
                    .add(URL, url)
                    .add(CASE_ID, id)
                    .add(METHOD, testCase.getMethod())
                    .add(REMARK, testCase.getCreateTime())
//                    .add(REQUEST_BODY, testCase.getRequestBody())
                    .add(RESPONSE_BODY, testCase.getResponseBody())
                    .add(IS_MINE, isMine)
                    .add(PARAMETERS, parameters);
            if(isMine) {
                resultData.add(CASE_NAME, testCase.getName())
                        .add(CASE_SHARE, testCase.getIsPub())
                        .add(CASE_STORE, testCase.getIsStore());
            }
            return resultData;

        }catch (Exception e) {
            e.printStackTrace();
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR);
        }
    }

    @RequestMapping("/name.json")
    @ResponseBody
    public ResultData name(Long id, String name, String path){
        try {
            CfgTestCase testCase = new CfgTestCase();
            testCase.setId(id);
            testCase.setName(name);
            testCaseSV.update(testCase);
            ResultData resultData = getCaseList(null, path);
            resultData.add(CASE_NAME, name);
            return resultData;
        }catch (Exception e) {
            e.printStackTrace();
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR);
        }
    }
    @RequestMapping("/share.json")
    @ResponseBody
    public ResultData share(Long id, Byte share, String path){
        try {
            CfgTestCase testCase = new CfgTestCase();
            testCase.setId(id);
            testCase.setIsPub(share);
            testCaseSV.update(testCase);
            ResultData resultData = getCaseList(null, path);
            resultData.add(CASE_SHARE, share);
            return resultData;
        }catch (Exception e) {
            e.printStackTrace();
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR);
        }
    }
    @RequestMapping("/store.json")
    @ResponseBody
    public ResultData store(Long id, Byte store, String path){
        try {
            CfgTestCase testCase = new CfgTestCase();
            testCase.setId(id);
            testCase.setIsStore(store);
            testCaseSV.update(testCase);
            ResultData resultData = getCaseList(null, path);
            resultData.add(CASE_STORE, store);
            return resultData;
        }catch (Exception e) {
            e.printStackTrace();
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR);
        }
    }


    public ResultData getCaseList(ResultData resultData, String path) throws Exception {
        User user = WebContext.getSession(SessionKey.USER);
        CfgTestCase_Example example = new CfgTestCase_Example();
        example.createCriteria().andPathEqualTo(path).andStatusEqualTo((byte)1);

        if(user != null) {
            example.getOredCriteria().get(0).andCreatorIdEqualTo(user.getUserId());
        }


        example.or().andPathEqualTo(path).andStatusEqualTo((byte)1).andIsPubEqualTo((byte)1);
        example.setOrderByClause(" modify_time desc ");
        List<CfgTestCase> caseList = testCaseSV.getCfgTestCaseListByExample(example);
        List storeList = new ArrayList();
        List historyList = new ArrayList();
        List shareList = new ArrayList();
        List myShareList = new ArrayList();
        for (CfgTestCase testCase : caseList) {
            Long id = testCase.getId();
            String name;
            if(StringUtils.isNotBlank(testCase.getName())){
                name = testCase.getName();
            }else {
                String parameterStr = testCase.getParameterStr();
                if(parameterStr.length()>= 28){
                    Map<String, String> urlParameters = ThirdApiEditController.getUrlParameters("?" + parameterStr);

                    for (Map.Entry<String, String> entry : urlParameters.entrySet()) {
                        if(entry.getValue().length() > 9) {
                            entry.setValue(entry.getValue().substring(0, 9) + ".");
                        }
                    }
                    parameterStr = UrlHelper.getUrlQueryString(urlParameters);
                    if(parameterStr.length()> 30){
                        parameterStr = parameterStr.substring(0, 30) + ".";
                    }

                }
                name =parameterStr;
            }


            boolean isMine = user != null && testCase.getCreatorId() == user.getUserId();
            boolean isStore =  testCase.getIsStore() == (byte)1;
            boolean isShare = testCase.getIsPub() == (byte)1;
            Date time = testCase.getModifyTime();
            String dateString = DateFormatUtils.format(time, "yyyy-MM-dd");
            String timeString = DateFormatUtils.format(new Date(),  "yyyy-MM-dd").equals(dateString) ?
                    ( DateFormatUtils.format(time, "HH:mm:ss")): dateString;
            Object[] items = {id, name, isStore, isShare, timeString};
            if(isMine) {
                historyList.add(items);
                if(isStore){
                    storeList.add(items);
                }
                if(isShare){
                    myShareList.add(items);
                }
            }else if(isShare){
                shareList.add(items);
            }
        }
        if(resultData == null) {
            resultData = ResultData.success();
        }
        resultData.add(HISTORIES, historyList);
        resultData.add(STORES, storeList);
        resultData.add(SHARES, shareList);
        resultData.add(MY_SHARES, myShareList);

        return resultData;
    }


}
