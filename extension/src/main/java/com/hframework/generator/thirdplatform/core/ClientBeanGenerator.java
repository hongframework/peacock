package com.hframework.generator.thirdplatform.core;

import com.hframework.beans.class0.Class;
import com.hframework.beans.class0.*;
import com.hframework.common.resource.ResourceWrapper;
import com.hframework.common.util.*;
import com.hframework.common.util.file.FileUtils;
import com.hframework.common.util.message.VelocityUtil;
import com.hframework.common.util.message.XmlUtils;
import com.hframework.generator.BeanGeneratorUtil;
import com.hframework.generator.bean.AbstractGenerateDescriptor;
import com.hframework.generator.bean.GenerateDescriptor;
import com.hframework.generator.thirdplatform.bean.Descriptor;
import com.hframework.generator.thirdplatform.bean.GeneratorConfig;
import com.hframework.generator.thirdplatform.bean.InterfaceExample;
import com.hframework.generator.thirdplatform.bean.descriptor.*;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by zqh on 2016/4/16.
 */
public class ClientBeanGenerator extends AbstractGenerator implements Generator<GeneratorConfig,Descriptor>{

    private RequestConfig requestConfig;
    private ResponseConfig responseConfig;

    private Map<String, Class> requestBeanMap = new HashMap<String, Class>();


    @Override
    protected boolean generateInternal(GeneratorConfig generatorConfig, Descriptor descriptor) {
        requestConfig = descriptor.getGlobal().getRequestConfig();
        responseConfig = descriptor.getGlobal().getResponseConfig();

        Class beanClass = new Class();
        beanClass.setSrcFilePath(javaRootPath);
        beanClass.setClassPackage(javaPackage);
        beanClass.setClassName(JavaUtil.getJavaClassName(platformName) + "Client");


        beanClass.addImportClass("java.util.*");
        beanClass.addImportClass("com.hframework.common.util.protocol.HttpClient");
        beanClass.addImportClass("com.hframework.common.util.UrlHelper");
        beanClass.addImportClass("java.text.MessageFormat");
        beanClass.addImportClass("com.hframework.common.util.message.*");
        beanClass.addImportClass(javaPackage + ".bean.*");
        beanClass.addImportClass("com.hframework.common.util.FileUtils");
        clientClass = beanClass;

        List<Interface> interface1List = descriptor.getInterfaces().getInterface1List();
        for (Interface anInterface : interface1List) {

            Method method = new Method();
            method.setName(anInterface.getName());
            method.setModifier("public static");
            method.setExceptionStr(" throws Exception");

            method.addCodeLn(getFormatUrl(method, anInterface,
                    "false".equals(anInterface.getUsePublicParams()) ? new ArrayList<Parameter>() :
                            descriptor.getGlobal().getRequestConfig().getPublicParameters().getParameterList()));

            String requestBeanName = StringUtils.isNotBlank(anInterface.getRequest().getBeanName()) ? anInterface.getRequest().getBeanName() : "RequestData";
            String responseBeanName = StringUtils.isNotBlank(anInterface.getResponse().getBeanName()) ? anInterface.getResponse().getBeanName() : "ResponseData";
            responseBeanName = (!"xml".equals(anInterface.getResponse().getMessage()) && !"json".equals(anInterface.getResponse().getMessage()) ) ? "String" : responseBeanName;
            method.setReturnType(responseBeanName);
            //生成request请求对象
            if(StringUtils.isNotBlank(anInterface.getRequest().getMessage())) {
                method.addParameter(new Field(requestBeanName, "requestData"));
                Class requestBean = createRequestBean(anInterface, requestBeanName);
                if(requestBean != null) {
                    requestBeanMap.put(method.getName(), requestBean);
                }
            }

            //生成response请求对象
            if(StringUtils.isNotBlank(anInterface.getResponse().getMessage())) {
                createResponseBean(anInterface,responseBeanName);
            }

            method.addCodeLn("String result;");
            method.addCodeLn("if(\"true\".equals(" + JavaUtil.getJavaClassName(platformName) + "Config.getInstance().getTestModel())) {");
            method.addCodeLn("   result = FileUtils.readFile(Thread.currentThread().getContextClassLoader().getResource(");
            method.addCodeLn("          \"" + resourceFolder + "/" + platformName + "/" + anInterface.getName() + ".response" + "\").getPath());");
            method.addCodeLn("}else {");


            String httpCode = "   result = ";
            if("json".equals(anInterface.getRequest().getMessage())) {
                httpCode += "HttpClient.doJsonPost(url,requestData.convert());";
            }else if("xml".equals(anInterface.getRequest().getMessage())) {
                httpCode += "HttpClient.doXmlPost(url,requestData.convert());";
            }else {
                if("get".equals(anInterface.getMethod())) {
                    httpCode += "HttpClient.doGet(url,parameterMap);";
                }else {
                    httpCode += "HttpClient.doPost(url,parameterMap);";
                }

            }
            method.addCodeLn(httpCode);
            method.addCodeLn("}");

            if("json".equals(anInterface.getResponse().getMessage())) {
                method.addCodeLn(responseBeanName + " responseData = JsonUtils.readValue(result," + responseBeanName + ".class);");
            }else if("xml".equals(anInterface.getResponse().getMessage())) {
                method.addCodeLn(responseBeanName + " responseData = XmlUtils.readValue(result," + responseBeanName + ".class);");
            }

            if("json".equals(anInterface.getResponse().getMessage()) || "xml".equals(anInterface.getResponse().getMessage())) {
                method.addCodeLn("return responseData.convert();");
            }else {
                method.addCodeLn("return result;");
            }

            beanClass.addMethod(method);
        }

        Map map = new HashMap();
        map.put("CLASS", beanClass);
        String content = VelocityUtil.produceTemplateContent("com/hframework/generator/vm/bean.vm", map);
        System.out.println(content);
        FileUtils.writeFile(beanClass.getFilePath(), content);

        return false;
    }

    private Class createRequestBean(Interface anInterface, String requestDataName) {
        List<Node> ruleNodeList = new ArrayList<Node>();
        listAddAll(ruleNodeList, requestConfig.getPublicNodes().getNodeList());
        listAddAll(ruleNodeList, anInterface.getRequest().getNodes().getNodeList());
        try {
            if(StringUtils.isNotBlank(anInterface.getTemplate())) {
                InterfaceExample interfaceExample =
                        XmlUtils.readValueFromFile("/thirdplatform/" + anInterface.getTemplate(), InterfaceExample.class);
                String requestMessage = interfaceExample.getRequestMessage();
                if(StringUtils.isNotBlank(requestMessage)) {
                    ClientBeanGenerateDescriptor descriptor = new ClientBeanGenerateDescriptor();
                    descriptor.setJavaRootPath(javaRootPath);
                    descriptor.setJavaPackage(javaPackage + ".bean");
                    descriptor.setRuleNodeList(ruleNodeList);
                    if("json".equals(anInterface.getRequest().getMessage())) {
                        BeanGeneratorUtil.generateByJsonNew(descriptor, requestMessage, requestDataName);
                    }else if("xml".equals(anInterface.getRequest().getMessage())) {
                        Class requestBean = BeanGeneratorUtil.generateByXml(descriptor, requestMessage, requestDataName);
                        return requestBean;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void createResponseBean(Interface anInterface, String responseBeanName) {

        List<Node> ruleNodeList = new ArrayList<Node>();
        listAddAll(ruleNodeList, responseConfig.getPublicNodes().getNodeList());
        listAddAll(ruleNodeList, anInterface.getResponse().getNodeList());

        try {
            if(StringUtils.isNotBlank(anInterface.getTemplate())) {
                InterfaceExample interfaceExample =
                        XmlUtils.readValueFromFile("/thirdplatform/" + anInterface.getTemplate(), InterfaceExample.class);
                String responseMessage = interfaceExample.getResponseMessage();
                ClientBeanGenerateDescriptor descriptor = new ClientBeanGenerateDescriptor();
                descriptor.setJavaRootPath(javaRootPath);
                descriptor.setJavaPackage(javaPackage + ".bean");
                descriptor.setRuleNodeList(ruleNodeList);
                descriptor.setRequestBean(false);
                if(StringUtils.isNotBlank(responseMessage)) {
                    System.out.println(responseMessage);
                    if("json".equals(anInterface.getResponse().getMessage())) {
                        BeanGeneratorUtil.generateByJsonNew(descriptor, responseMessage, responseBeanName);
                    }else if("xml".equals(anInterface.getResponse().getMessage())) {

                        BeanGeneratorUtil.generateByXml(descriptor, responseMessage, responseBeanName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listAddAll(List<Node> ruleNodeList, List<Node> nodeList) {
        if(nodeList != null) {
            ruleNodeList.addAll(nodeList);
        }

    }


    private String getFormatUrl(Method method, Interface anInterface, List<Parameter> parentParameterList) {
        method.addCodeLn("Map<String, String> parameterMap = new LinkedHashMap();");
        String url = "String url = UrlHelper.getUrlPath(" +
                JavaUtil.getJavaClassName(platformName) + "Config.getInstance().get"
                + ResourceWrapper.JavaUtil.getJavaClassName(method.getName()) + "()";
        List<Parameter> parameterList = anInterface.getRequest().getParameters().getParameterList();
        for (Parameter parameter : parameterList) {
            String codeSegment = getCodeSegmentByParameter(method, parameter);
            method.addCodeLn("parameterMap.put(\"" + parameter.getName() + "\" ," + codeSegment +");");
//            url += ", " + codeSegment;
        }

        for (Parameter parameter : parentParameterList) {
            String codeSegment = getCodeSegmentByParameter(method, parameter);
            method.addCodeLn("parameterMap.put(\"" + parameter.getName() + "\" ," + codeSegment +");");
//            url += ", " + codeSegment;
        }

//        if(parameterList.size() == 0) {
//            url += ",null";
//        }
//        url += ", parameterMap";


        url += ");";
        return url;
    }

    private String getCodeSegmentByParameter(Method method, Parameter parameter) {
        if(!"false".equals(parameter.getVisiable())) {
            String javaName = ResourceWrapper.JavaUtil.getJavaVarName(parameter.getName());
            method.addParameter(new Field(parameter.getType(), javaName));
            return "String".equals(parameter.getType()) ? javaName : "String.valueOf(" +javaName + ")";
        }else {
            for (Method method1 : helperClass.getMethodList()) {
                if(method1.getName().equals(parameter.getRuleId())) {
                    return getMethodInvokeCode(method1);
                }
            }
            String value = parameter.getValue();
            if(StringUtils.isNotBlank(value)) {
                String[] strings = RegexUtils.find(value, "[\\#\\$]\\{[ a-zA-Z:0-9_]+\\}");
                if(strings != null && strings.length > 0) {
                    String paramName = strings[0].substring(2, strings[0].length() - 1);
                    if(strings[0].startsWith("#")) {
                        return JavaUtil.getJavaClassName(platformName) + "Config.getInstance().get"
                                + ResourceWrapper.JavaUtil.getJavaClassName(paramName) + "()";
                    }
                }
            }

            //说明是固定值
            return "\"" + value + "\"";
        }
    }

    private String getMethodInvokeCode(Method method1) {

        String code = JavaUtil.getJavaClassName(platformName) + "Helper."
                + method1.getName() + "(" ;
        for (Field field : method1.getParameterList()) {
            if("Object".equals(field.getType())) {
                code += "parameterMap";
            }else {
                //what ???
                code += "to do here ..";
            }
        }
        code = code.endsWith(",") ? code.substring(0, code.length()-1) : code  +  ")";

        return code;
    }

    public class ClientBeanGenerateDescriptor extends AbstractGenerateDescriptor implements GenerateDescriptor {
        private List<Node> ruleNodeList = null;
        private boolean requestBean = true;

        public ClientBeanGenerateDescriptor() {
        }

        public void executeClassExtend(Class beanClass, XmlNode xmlNode) {
            setDefaultAliasClassName(beanClass, xmlNode);
            addConvertMethod(beanClass,xmlNode);
            setAliasClassName(beanClass, xmlNode);


        }

        public void executeFieldExtend(Field field, XmlNode xmlNode){
            setDefaultAliasField(field, xmlNode);
            if(ruleNodeList != null && ruleNodeList.size() > 0) {
                Node node = matchNode(xmlNode, ruleNodeList);
                if(node == null) return ;
                String type = node.getType();
                if(Arrays.binarySearch(new String[]{"String","int","long",""},type) < 0) {
                    if("Map".equals(type)) {
                        Node subNode = null;
                        if(node.getNodeList().size() >  0) {
                            subNode = node.getNodeList().get(0);
                        }

                        field.setType("java.util.Map<String, " +  "" + (subNode == null ? "String" : subNode.getType()) + ">");
                        field.setName(JavaUtil.getJavaVarName(subNode == null ? "String" : subNode.getType()));
                    }
                }
            }
        }

        private void setDefaultAliasField(Field field, XmlNode xmlNode) {
            String nodeName = xmlNode.getNodeName();
            if("list".equals(nodeName)) {
                if(field.getType().endsWith(">")) {
                    field.setType(field.getType().substring(0,field.getType().length()-1) + "Bean>");
                }else {
                    field.setType(field.getType() + "Bean");
                }
            }
        }


        private void setDefaultAliasClassName(Class beanClass, XmlNode xmlNode) {
            String nodeName = xmlNode.getNodeName();
            if("list".equals(nodeName)) {
                beanClass.setClassName(beanClass.getClassName() + "Bean");
            }
        }
        private void setAliasClassName(Class beanClass, XmlNode xmlNode) {
            if(ruleNodeList != null && ruleNodeList.size() > 0) {
                Node node = matchNode(xmlNode, ruleNodeList);
                if(node == null) return ;
                String type = node.getType();
                if(!Arrays.asList(new String[]{"String", "int", "long"}).contains(type)) {
                    if("Map".equals(type)) {
                        beanClass.setClassName("Map");
                    }
                }
            }
        }

        public void executeXmlNodeExtend(XmlNode xmlNode) {
            if(ruleNodeList != null && ruleNodeList.size() > 0) {
                Node node = matchNodeTree(xmlNode, ruleNodeList);
                if(node != null) {
                    String type = node.getType();
                    if (!Arrays.asList(new String[]{"String", "int", "long", "Map", ""}).contains(type)) {
                        xmlNode.setNodeName(type);
                    }
                }
            }

            List<XmlNode> childrenXmlNode = xmlNode.getChildrenXmlNode();
            for (XmlNode node : childrenXmlNode) {
                executeXmlNodeExtend(node);
            }
        }

        private void addConvertMethod(Class beanClass, XmlNode xmlNode) {
            Field field = new Field("boolean", "converted");
            field.setSetGetMethod(false);

            String extendAttr = beanClass.getExtendAttr(Class.ExtendAttrCode.MESSAGE_ANNOTATION_TYPE);
            if("xml".equals(extendAttr)) {
                field.addFieldAnno("@XStreamOmitField");
                beanClass.addImportClass("com.thoughtworks.xstream.annotations.XStreamOmitField");
            }
            beanClass.addField(field);
            beanClass.addImportClass("com.hframework.common.util.message.*");

            Method method = new Method();
            method.setName("convert");
            method.setExceptionStr(" throws Exception");
            method.setReturnType(beanClass.getClassName());

            if(ruleNodeList != null && ruleNodeList.size() > 0) {
                method.addCodeLn("if(!converted) {");
                method.addCodeLn("   String beforeInfo = XmlUtils.writeValueAsString(this);");
                method.addCodeLn("   System.out.println(beforeInfo);");

                method.addCodeLn("   converted = true;");

                List<XmlNode> childrenXmlNode = xmlNode.getChildrenXmlNode();
                if(childrenXmlNode != null) {
                    for (XmlNode childXmlNode : childrenXmlNode) {
                        Node node = matchNode(childXmlNode, ruleNodeList);
                        if(node == null) continue;
                        String value = node.getValue();
                        String ruleId = node.getRuleId();
                        String type = node.getType();
                        if (childXmlNode.getChildrenXmlNode().size() == 0 && childXmlNode.getAttrMap().size() == 0) {
                            if (childXmlNode.isSingleton()) {
                                if(StringUtils.isNotBlank(value)) {
                                    String[] strings = RegexUtils.find(value, "[\\#\\$]\\{[ a-zA-Z:0-9_]+\\}");
                                    if(strings != null && strings.length > 0) {
                                        String paramName = strings[0].substring(2, strings[0].length() - 1);
                                        if(strings[0].startsWith("#")) {
                                            method.addCodeLn("   " + JavaUtil.getJavaVarName(childXmlNode.getNodeName()) +
                                                    "=" + JavaUtil.getJavaClassName(platformName) + "Config.getInstance().get"
                                                    + ResourceWrapper.JavaUtil.getJavaClassName(paramName) + "();");
                                        }
                                    }
                                }

                                if(StringUtils.isNotBlank(ruleId)) {
                                    method.addCodeLn("   " + JavaUtil.getJavaVarName(childXmlNode.getNodeName()) +
                                            "=" + JavaUtil.getJavaClassName(platformName) + "Helper."
                                            + ruleId + "(" + whetherContainThis(ruleId) + ");");
                                }
                                modifyMethod(beanClass, JavaUtil.getJavaVarName(childXmlNode.getNodeName()));
                            }
                        }
                    }
                }

                method.addCodeLn("   String afterInfo = XmlUtils.writeValueAsString(this);");
                method.addCodeLn("   System.out.println(afterInfo);");
                beanClass.addImportClass(beanClass.getClassPackage().substring(0, beanClass.getClassPackage().lastIndexOf(".")) + ".*");
                method.addCodeLn("}");
            }

            method.addCodeLn("return this;");

            beanClass.addMethod(method);
        }

        private void modifyMethod(Class beanClass, String javaVarName) {
            List<Field> fieldList = beanClass.getFieldList();
            for (Field field : fieldList) {
                if(javaVarName.equals(field.getName())) {
                    field.setSetGetMethod(false);
                    Method getMethod = MethodHelper.getGetMethod(field);
                    Method setMethod = MethodHelper.getSetMethod(field);
                    beanClass.addMethod(getMethod);
                    beanClass.addMethod(setMethod);
                    if(requestBean) {
                        setMethod.setModifier("private");
                    }else {
                        getMethod.setModifier("private");
                    }


                }
            }
        }

        private String whetherContainThis(String ruleId) {
            for (Method method1 : helperClass.getMethodList()) {
                if(method1.getName().equals(ruleId)) {
                    for (Field field : method1.getParameterList()) {
                        if("Object".equals(field.getType())) {
                            return "this";
                        }
                    }
                }
            }
            return "";
        }

        private  Node matchNode(XmlNode childXmlNode, List<Node> ruleNodeList) {
            for (Node node : ruleNodeList) {
//                System.out.println(node.getPath() + "--" + childXmlNode.getNodeCode());
                if(PathMatcherUtils.matches(node.getPath(), childXmlNode.getNodeCode())) {
                    return node;
                }
            }
            return null;
        }

        private  Node matchNodeTree(XmlNode childXmlNode, List<Node> ruleNodeList) {
            if(ruleNodeList != null) {
                for (Node node : ruleNodeList) {
                    if(PathMatcherUtils.matches(node.getPath(), childXmlNode.getNodeCode())) {
                        return node;
                    }
                    if(node.getNodeList() == null) {
                        continue;
                    }
                    Node matchNode = matchNodeTree(childXmlNode,node.getNodeList());
                    if(matchNode != null) {
                        return matchNode;
                    }
                }
            }
            return null;
        }

        public List<Node> getRuleNodeList() {
            return ruleNodeList;
        }

        public void setRuleNodeList(List<Node> ruleNodeList) {
            this.ruleNodeList = ruleNodeList;
        }

        public boolean isRequestBean() {
            return requestBean;
        }

        public void setRequestBean(boolean requestBean) {
            this.requestBean = requestBean;
        }
    }

    public Map<String, Class> getRequestBeanMap() {
        return requestBeanMap;
    }
}
