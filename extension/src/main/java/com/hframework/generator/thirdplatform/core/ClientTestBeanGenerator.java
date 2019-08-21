package com.hframework.generator.thirdplatform.core;

import com.hframework.common.resource.ResourceWrapper;
import com.hframework.common.util.*;
import com.hframework.common.util.file.FileUtils;
import com.hframework.common.util.message.VelocityUtil;
import com.hframework.common.util.message.XmlUtils;
import com.hframework.beans.class0.Class;
import com.hframework.beans.class0.*;

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
public class ClientTestBeanGenerator extends AbstractGenerator implements Generator<GeneratorConfig,Descriptor>{

    private RequestConfig requestConfig;
    private ResponseConfig responseConfig;

    private Map<String, Class> requestBeanMap = new HashMap<String, Class>();


    @Override
    protected boolean generateInternal(GeneratorConfig generatorConfig, Descriptor descriptor) {

        List<Interface> interfaceList = descriptor.getInterfaces().getInterface1List();

        Class beanClass = new Class();
        beanClass.setSrcFilePath(javaTestRootPath);
        beanClass.setClassPackage(javaPackage);
        beanClass.setClassName(JavaUtil.getJavaClassName(platformName) + "ClientTest");


        beanClass.addImportClass("com.hframework.common.util.message.JsonUtils");
        beanClass.addImportClass("org.junit.Test");
        beanClass.addImportClass(javaPackage + ".bean.*");

        List<Method> methodList = clientClass.getMethodList();
        for (Method method : methodList) {
            Method newMethod = new Method();
            newMethod.setName(method.getName());
            newMethod.setExceptionStr(method.getExceptionStr());
            newMethod.addAnnotation("@Test");


            for (Field field : method.getParameterList()) {

                if(!"String".equals(field.getType()) && !"int".equals(field.getType()) && !"long".equals(field.getType())) {
                    newMethod.addCodeLn(field.getType() + " " + field.getType().substring(0,1).toLowerCase()
                            + field.getType().substring(1) + " = new " + field.getType() + "();");

                    Class requestBean = requestBeanMap.get(method.getName());

                    List<Field> fieldList = requestBean.getFieldList();
                    for (Field field1 : fieldList) {
                        if(field1.isSetGetMethod()) {
                            Method setMethod = MethodHelper.getSetMethod(field1);
                            newMethod.addCodeLn(field.getType().substring(0,1).toLowerCase() + field.getType().substring(1) + "." + setMethod.getName() + "(\"1\");");
                        }
                    }
                }
            }

            newMethod.addCodeLn(method.getReturnType() + " result =" + clientClass.getClassName() + "." + method.getName() + "("
                    + getMethodInvoke(method, interfaceList)
                    + ");");
            newMethod.addCodeLn("System.out.println(result);");
            newMethod.addCodeLn("System.out.println(JsonUtils.writeValueAsString(result));");
            beanClass.addMethod(newMethod);
        }

        Map map = new HashMap();
        map.put("CLASS", beanClass);
        String content = VelocityUtil.produceTemplateContent("com/hframework/generator/vm/bean.vm", map);
        System.out.println(content);
        FileUtils.writeFile(beanClass.getFilePath(), content);

        return false;
    }

    private String getMethodInvoke(Method method, List<Interface> interfaceList) {
        Map<String, String> urlParameters = new HashMap<String, String>();
        for (Interface anInterface : interfaceList) {
            if(method.getName().equals(anInterface.getName())) {
                if(StringUtils.isNotBlank(anInterface.getTemplate())) {
                    try {
                        InterfaceExample interfaceExample =
                                XmlUtils.readValueFromFile("/thirdplatform/" + anInterface.getTemplate(), InterfaceExample.class);
                        urlParameters = UrlHelper.getUrlParameters(interfaceExample.getUrl(), true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        String code = "";
        for (Field field : method.getParameterList()) {

            if("String".equals(field.getType())) {
                code += "\"" + urlParameters.get(field.getName())+ "\",";
            }else if("int".equals(field.getType()) || "long".equals(field.getType())) {
                code += urlParameters.get(field.getName()) + ",";
            }else {
                //what ???
                code += field.getType().substring(0,1).toLowerCase()
                        + field.getType().substring(1);
            }
        }
        code = code.endsWith(",") ? code.substring(0, code.length()-1) : code;
        return code;
    }

    private void createRequestBean(Interface anInterface, String requestDataName) {
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

                        BeanGeneratorUtil.generateByJsonNew(descriptor, requestMessage, requestDataName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        String url = "String url = UrlHelper.getFinalUrl(" +
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
        url += ", parameterMap";


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
                    String paramName = JavaUtil.getJavaVarName(strings[0].substring(2, strings[0].length() - 1));
                    if(strings[0].startsWith("#")) {
                        return JavaUtil.getJavaClassName(platformName) + "Config.getInstance().get"
                                + ResourceWrapper.JavaUtil.getJavaClassName(paramName) + "()";
                    }
                }
            }

            return "unkown";
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
                        Node subNode = node.getNodeList().get(0);
                        field.setType("java.util.Map<String, " +  "" + subNode.getType() + ">");
                        field.setName(JavaUtil.getJavaVarName(subNode.getType()));
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
            field.addFieldAnno("@XStreamOmitField");
            beanClass.addImportClass("com.thoughtworks.xstream.annotations.XStreamOmitField");
            beanClass.addField(field);
            beanClass.addImportClass("com.hframework.common.util.message.*");

            Method method = new Method();
            method.setName("convert");
            method.setExceptionStr(" throws Exception");

            if(ruleNodeList != null && ruleNodeList.size() > 0) {
                method.addCodeLn("if(!converted) {");
                method.addCodeLn("   String beforeInfo = XmlUtils.writeValueAsString(this);");
                method.addCodeLn("   System.out.println(beforeInfo);");

                method.addCodeLn("   converted = true;");
                method.setReturnType(beanClass.getClassName());

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
                                        String paramName = JavaUtil.getJavaVarName(strings[0].substring(2, strings[0].length() - 1));
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
            }

            method.addCodeLn("}");
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

    public void setRequestBeanMap(Map<String, Class> requestBeanMap) {
        this.requestBeanMap = requestBeanMap;
    }
}
