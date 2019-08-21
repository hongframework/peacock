package com.hframework.generator;

import com.fasterxml.jackson.databind.JsonNode;
import com.hframework.beans.class0.Class;
import com.hframework.beans.class0.Field;
import com.hframework.beans.class0.XmlNode;
import com.hframework.common.resource.ResourceWrapper;
import com.hframework.common.util.StringUtils;
import com.hframework.common.util.file.FileUtils;
import com.hframework.common.util.message.Dom4jUtils;
import com.hframework.common.util.message.JacksonObjectMapper;
import com.hframework.common.util.message.VelocityUtil;
import com.hframework.generator.bean.GenerateDescriptor;
import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.IOException;
import java.util.*;

/**
 * User: zhangqh6
 * Date: 2016/1/20 16:33:33
 */
public class BeanGeneratorUtil {

    private static final Map<String,String> KEYWORDS= new HashMap<String, String>() {{
        put("interface","interface1");
        put("class","clazz");
    }};

    /**
     * 通过Json数据生成Bean对象
     * @param rootClassName
     * @param jsonString
     */
    public static void generateByJson(GenerateDescriptor descriptor, String jsonString, String rootClassName) throws IOException {
        JsonNode jsonNode = JacksonObjectMapper.getInstance().readTree(jsonString);

        Map<String, Object> mergeMap = new HashMap<String, Object>();
        mergeMap.put(rootClassName, parseJsonNode(jsonNode));
        Map<String,Integer> nameRepeatCache = new HashMap<String, Integer>();
        generateClassByJson(descriptor, rootClassName, mergeMap.get(rootClassName), true, nameRepeatCache);
    }



//    /**
//     * 通过Xml数据生成Bean对象
//     * @param packagePath
//     * @param rootClassName
//     * @param xmlString
//     */
//    public static void generateByXml(String packagePath, String rootClassName,String rootXmlName, String xmlString) throws IOException {
//        Document document = Dom4jUtils.getDocumentByContent(xmlString);
//        Element root = document.getRootElement();
//        Map<String, Object> mergeMap = new HashMap<String, Object>();
//        mergeMap.put(rootClassName, parseXmlNode(root));
//        generateClassByXml(packagePath, rootClassName, rootXmlName, mergeMap.get(rootClassName), true);
//
//    }

    public static Map<String, Object> getFlatMap(XmlNode rootNode,  String kvContainersStr){
        Map<String, Object> flatMap = new LinkedHashMap<>();
        flatXmlNode(flatMap, rootNode, "", kvContainersStr.split(","), false);
        return flatMap;
    }

    public static void  flatXmlNode(Map<String, Object> flatMap, XmlNode rootNode, String parentPath, String[] kvContainers, boolean igonreflag){
        List<XmlNode> childrenXmlNode = rootNode.getChildrenXmlNode();
        if(parentPath.endsWith("{}/") && !igonreflag){
            flatMap.put(parentPath +  "_key_", rootNode.getNodeName());
            for (XmlNode childNode : childrenXmlNode) {
                flatXmlNode(flatMap, childNode, parentPath, kvContainers, true);
            }
        }else {
            String path = parentPath +  rootNode.getNodeName() + (rootNode.isSingleton() ? "" : "[]");
            List<String> kvContainerList = new ArrayList<>();
            for (String kvContainer : kvContainers) {
                if(StringUtils.isNotBlank(kvContainer)) {
                    kvContainerList.add(kvContainer.endsWith("{}") ? kvContainer.substring(0, kvContainer.length() - 2) : kvContainer);
                }
            }
            if(kvContainerList.contains(path)) {
                path += "{}";
            }
            if(childrenXmlNode.isEmpty()){
                flatMap.put(path, rootNode.getNodeText());
            }else {
                if(!path.isEmpty()){
                    flatMap.put(path, "");
                }

                for (XmlNode childNode : childrenXmlNode) {
                    flatXmlNode(flatMap, childNode, path + "/", kvContainers, false);
                }
            }
        }

    }



    /**
     * 通过Xml数据获取XmlNode对象
     * @param jsonString
     */
    public static XmlNode getXmlNodeByJson(GenerateDescriptor descriptor, String jsonString) throws IOException {
        JsonNode jsonNode = JacksonObjectMapper.getInstance().readTree(jsonString);

        XmlNode rootXmlNode = parseXmlNode("",jsonNode);

        rootXmlNode.settingNodeCode();

        descriptor.executeXmlNodeExtend(rootXmlNode);

        Map<String,List<XmlNode>> sameNameNodeMap = rootXmlNode.fetchSameNameNode(new LinkedHashMap<String, List<XmlNode>>());

        XmlNode.XmlNodeHelper.filterSingletonNode(sameNameNodeMap);

        //获取合并规则 TODO
        for (String nodeName : sameNameNodeMap.keySet()) {
            List<XmlNode> xmlNodes = sameNameNodeMap.get(nodeName);
            XmlNode baseNode = xmlNodes.get(0);
            for (int i = 1; i < xmlNodes.size(); i++) {
                baseNode.mergeOutSide(xmlNodes.get(i));
                xmlNodes.get(i).getParentXmlNode().getChildrenXmlNode().set(
                        xmlNodes.get(i).getParentXmlNode().getChildrenXmlNode().indexOf(xmlNodes.get(i)),baseNode);
            }
        }

        return rootXmlNode;
    }


    /**
     * 通过Xml数据获取XmlNode对象
     * @param xmlString
     */
    public static XmlNode getXmlNodeByXml(GenerateDescriptor descriptor, String xmlString) throws IOException {
        Document document = Dom4jUtils.getDocumentByContent(xmlString);
        Element root = document.getRootElement();

        XmlNode rootXmlNode = parseXmlNodeNew(root);

        rootXmlNode.settingNodeCode();

        descriptor.executeXmlNodeExtend(rootXmlNode);

        Map<String,List<XmlNode>> sameNameNodeMap = rootXmlNode.fetchSameNameNode(new LinkedHashMap<String, List<XmlNode>>());

        XmlNode.XmlNodeHelper.filterSingletonNode(sameNameNodeMap);

        //获取合并规则 TODO
        for (String nodeName : sameNameNodeMap.keySet()) {
            List<XmlNode> xmlNodes = sameNameNodeMap.get(nodeName);
            XmlNode baseNode = xmlNodes.get(0);
            for (int i = 1; i < xmlNodes.size(); i++) {
                XmlNode targetXmlNode = xmlNodes.get(i);
                //将targetXmlNode原父节点下替换为新的base节点
                List<XmlNode> brotherXmlNode = targetXmlNode.getParentXmlNode().getChildrenXmlNode();
                baseNode.mergeOutSide(targetXmlNode);
                if(brotherXmlNode.indexOf(xmlNodes.get(i)) > -1) {
                    brotherXmlNode.set(brotherXmlNode.indexOf(xmlNodes.get(i)), baseNode);
                }

            }
        }

        return rootXmlNode;
    }

    /**
     * 通过Xml数据生成Bean对象
     * @param descriptor 类生成描述器
     * @param rootClassName
     * @param xmlString
     */
    public static Class generateByXml(GenerateDescriptor descriptor, String xmlString, String rootClassName)  throws IOException {
        return generateClassByXmlNode(descriptor, rootClassName, getXmlNodeByXml(descriptor, xmlString), true,"xml");
    }

    /**
     * 通过Json数据生成Bean对象
     * @param descriptor 类生成描述器
     * @param rootClassName
     * @param jsonString
     */
    public static void generateByJsonNew(GenerateDescriptor descriptor, String jsonString, String rootClassName)  throws IOException {
        generateClassByXmlNode(descriptor, rootClassName, getXmlNodeByJson(descriptor,jsonString), true,"json");
    }

//    /**
//     * 生成类文件
//     * @param packagePath
//     * @param rootClassName
//     * @param rootXmlName
//     * @param data
//     * @param isRoot
//     */
//    private static void generateClassByXml(String packagePath, String rootClassName,String rootXmlName, Object data, boolean isRoot) {
//        Class beanClass = new Class();
//        beanClass.setSrcFilePath("D:\\my_workspace\\hframe-trunk\\hframe-target\\src\\main\\java\\");
//        beanClass.setClassPackage(packagePath);
//        beanClass.setClassName(rootClassName);
//        beanClass.addConstructor();
//        beanClass.addAnnotation("@XStreamAlias(\"" + rootXmlName + "\")");
//
//        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
//        if(data instanceof Map) {
//            dataMap = (Map<String, Object>) data;
//        }else if(data instanceof List){
//            dataMap = (Map<String, Object>) ((List) data).get(0);
//        }else {
//            return ;
//        }
//        beanClass.addImportClass("com.thoughtworks.xstream.annotations.XStreamAlias");
//        beanClass.addImportClass("com.thoughtworks.xstream.annotations.XStreamAsAttribute");
//
//        for (String fieldName : dataMap.keySet()) {
//            String subElementName = getSubElementName(dataMap.get(fieldName));
//            Field field = getField(fieldName, dataMap.get(fieldName), subElementName);
//            field.addFieldAnno("@XStreamAlias(\"" + fieldName + "\")");
//
//            beanClass.addField(field);
//            if(!"String".equals(field.getType())) {
//                if(field.getType().startsWith("List<") && !beanClass.getImportClassList().contains("java.util.List")) {
//                    beanClass.addImportClass("com.thoughtworks.xstream.annotations.XStreamImplicit");
//                    beanClass.addImportClass("java.util.List");
//                    field.addFieldAnno("@XStreamImplicit");
//                }
//                if(isRoot) {
//                    beanClass.addImportClass(packagePath + "." + ResourceWrapper.JavaUtil.getJavaClassName(rootClassName).toLowerCase() + ".*");
//                }
//
//                if(subElementName != null) {
//                    generateClassByXml(packagePath + (isRoot ? ("." + ResourceWrapper.JavaUtil.getJavaClassName(rootClassName).toLowerCase()) : ""),
//                            ResourceWrapper.JavaUtil.getJavaClassName(subElementName), subElementName, dataMap.get(fieldName), false);
//                }else {
//                    generateClassByXml(packagePath + (isRoot ? ("." + ResourceWrapper.JavaUtil.getJavaClassName(rootClassName).toLowerCase()) : ""),
//                            ResourceWrapper.JavaUtil.getJavaClassName(fieldName), fieldName, dataMap.get(fieldName), false);
//                }
//
//            }
//        }
//
//        Map map = new HashMap();
//        map.put("CLASS", beanClass);
//        String content = VelocityUtil.produceTemplateContent("com/hframe/generator/vm/poByTemplate.vm", map);
//        System.out.println(content);
//        FileUtils.writeFile(beanClass.getFilePath(), content);
//    }

    /**
     * 生成类文件
     * @param rootClassName
     * @param rootXmlNode
     * @param isRoot
     */
    private static Class generateClassByXmlNode(GenerateDescriptor descriptor, String rootClassName, XmlNode rootXmlNode, boolean isRoot, String beanType) {

        Class beanClass = generateDefaultClassByXmlNode(descriptor,rootClassName,rootXmlNode,isRoot, beanType);
        //类扩展处理
        descriptor.executeClassExtend(beanClass, rootXmlNode);

        List<XmlNode> childrenXmlNode = rootXmlNode.getChildrenXmlNode();
        if(childrenXmlNode != null) {
            for (XmlNode childXmlNode : childrenXmlNode) {
                boolean cascadeFlag = true;
                if(childXmlNode.getChildrenXmlNode().size() == 0 &&
                        (childXmlNode.getAttrMap() == null || childXmlNode.getAttrMap().size() == 0 )) {
                    cascadeFlag = false;
                }
                if(cascadeFlag) {
                    if(isRoot) {
//                        FileUtils.createDir(beanClass.getFilePath().substring(0, beanClass.getFilePath().lastIndexOf("/"))
//                                + "/" + ResourceWrapper.JavaUtil.getJavaClassName(rootClassName).toLowerCase());
                        beanClass.addImportClass(descriptor.getJavaPackage() + "." + ResourceWrapper.JavaUtil.getJavaClassName(rootClassName).toLowerCase() + ".*");
                    }
                    GenerateDescriptor newDescriptor = descriptor;
//                    try {
//                        newDescriptor = (GenerateDescriptor) BeanUtils.cloneBean(descriptor);
//                    }catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    String sourceJavaPackage = descriptor.getJavaPackage();
                    newDescriptor.setJavaPackage(descriptor.getJavaPackage() +
                            (isRoot ? ("." + ResourceWrapper.JavaUtil.getJavaClassName(rootClassName).toLowerCase()) : ""));
                    generateClassByXmlNode(newDescriptor,
                            ResourceWrapper.JavaUtil.getJavaClassName(childXmlNode.getNodeName()), childXmlNode, false, beanType);
                    newDescriptor.setJavaPackage(sourceJavaPackage);
                }
            }
        }

        if(!"Map".equals(beanClass.getClassName())) {
            //注意：这里迭代调用是否存在先后关系 TODO
            Map map = new HashMap();
            map.put("CLASS", beanClass);
            String content = VelocityUtil.produceTemplateContent(descriptor.getTemplatePath(), map);
            System.out.println(content);
            FileUtils.writeFile(beanClass.getFilePath(), content);
        }

        return beanClass;

    }
    private static Class generateDefaultClassByXmlNode(GenerateDescriptor descriptor, String rootClassName, XmlNode rootXmlNode, boolean isRoot, String beanType) {
        if("xml".equals(beanType)) {
            return generateDefaultXmlClassByXmlNode(descriptor,rootClassName,rootXmlNode,isRoot);
        }else {
            return generateDefaultJsonClassByXmlNode(descriptor,rootClassName,rootXmlNode,isRoot);
        }
    }

    private static Class generateDefaultXmlClassByXmlNode(GenerateDescriptor descriptor, String rootClassName, XmlNode rootXmlNode, boolean isRoot) {
        Class beanClass = new Class();
        beanClass.setSrcFilePath(descriptor.getJavaRootPath());
        beanClass.setClassPackage(descriptor.getJavaPackage());
        beanClass.setClassName(rootClassName);
        beanClass.addConstructor();
        beanClass.addAnnotation("@XStreamAlias(\"" + rootXmlNode.getNodeName() + "\")");
        beanClass.addExtendAttr(Class.ExtendAttrCode.MESSAGE_ANNOTATION_TYPE, "xml");

        beanClass.addImportClass("com.thoughtworks.xstream.annotations.XStreamAlias");

        List<XmlNode> childrenXmlNode = rootXmlNode.getChildrenXmlNode();
        if(childrenXmlNode != null) {
            for (XmlNode childXmlNode : childrenXmlNode) {
                Field field ;
                if(childXmlNode.getChildrenXmlNode().size() == 0 &&
                        (childXmlNode.getAttrMap() == null || childXmlNode.getAttrMap().size() == 0 )) {
                    if(childXmlNode.isSingleton()) {
                        field = new Field("String",
                                ResourceWrapper.JavaUtil.getJavaVarName(childXmlNode.getNodeName()));
                    }else {
                        field = new Field("List<String>",
                                ResourceWrapper.JavaUtil.getJavaVarName(childXmlNode.getNodeName()) + "List");
                        beanClass.addImportClass("com.thoughtworks.xstream.annotations.XStreamImplicit");
                        beanClass.addImportClass("java.util.List");
                        field.addFieldAnno("@XStreamImplicit");
                    }

                }else if(childXmlNode.isSingleton()) {
                    field = new Field(ResourceWrapper.JavaUtil.getJavaClassName(childXmlNode.getNodeName()),
                            ResourceWrapper.JavaUtil.getJavaVarName(childXmlNode.getNodeName()));
                }else {
                    field = new Field("List<" + ResourceWrapper.JavaUtil.getJavaClassName(childXmlNode.getNodeName()) + ">",
                            ResourceWrapper.JavaUtil.getJavaVarName(childXmlNode.getNodeName()) + "List");
                    beanClass.addImportClass("com.thoughtworks.xstream.annotations.XStreamImplicit");
                    beanClass.addImportClass("java.util.List");
                    field.addFieldAnno("@XStreamImplicit");
                }
                field.addFieldAnno("@XStreamAlias(\"" + childXmlNode.getNodeName() + "\")");
                beanClass.addField(field);

                if(childXmlNode.isGenerated()) {
                    continue;
                }
                childXmlNode.setGenerated(true);
            }
        }

        Map<String, String> attrMap = rootXmlNode.getAttrMap();
        for (Map.Entry<String, String> entry : attrMap.entrySet()) {
            Field field = new Field("String",
                    ResourceWrapper.JavaUtil.getJavaVarName(entry.getKey()));
            field.addFieldAnno("@XStreamAsAttribute");
            field.addFieldAnno("@XStreamAlias(\"" + entry.getKey()+ "\")");
            beanClass.addImportClass("com.thoughtworks.xstream.annotations.XStreamAsAttribute");
            beanClass.addField(field);
        }


        String nodeText = rootXmlNode.getNodeText();
        if(StringUtils.isNotBlank(nodeText)) {
            Field field = new Field("String","text");
            field.setFieldComment("标签内内容");
            beanClass.addAnnotation("@XStreamConverter(value=ToAttributedValueConverter.class, strings={\"text\"})");
            beanClass.addImportClass("com.thoughtworks.xstream.annotations.XStreamConverter");
            beanClass.addImportClass("com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter");
            beanClass.addField(field);
        }
        return beanClass;
    }

    private static Class generateDefaultJsonClassByXmlNode(GenerateDescriptor descriptor, String rootClassName, XmlNode rootXmlNode, boolean isRoot) {
        Class beanClass = new Class();
        beanClass.setSrcFilePath(descriptor.getJavaRootPath());
        beanClass.setClassPackage(descriptor.getJavaPackage());
//        Integer integer = nameRepeatCache.get(rootClassName);
//        beanClass.setClassName(rootClassName + (integer==null ? "" : integer));
//        nameRepeatCache.put(rootClassName, (integer == null ? 1 : ++integer));
        beanClass.setClassName(rootClassName);
        beanClass.addConstructor();
        beanClass.addImportClass("com.fasterxml.jackson.annotation.JsonProperty");
        beanClass.addExtendAttr(Class.ExtendAttrCode.MESSAGE_ANNOTATION_TYPE,"json");

        List<XmlNode> childrenXmlNode = rootXmlNode.getChildrenXmlNode();
        if(childrenXmlNode != null) {
            for (XmlNode childXmlNode : childrenXmlNode) {
                Field field ;
                String xmlNodeName = childXmlNode.getNodeName();
                if(childXmlNode.getChildrenXmlNode().size() == 0 &&
                        (childXmlNode.getAttrMap() == null || childXmlNode.getAttrMap().size() == 0 )) {
                    if(childXmlNode.isSingleton()) {
                        field = new Field("String",
                                ResourceWrapper.JavaUtil.getJavaVarName(childXmlNode.getNodeName()));
                    }else {
                        field = new Field("List<String>",
                                ResourceWrapper.JavaUtil.getJavaVarName(childXmlNode.getNodeName()) + "List");
//                        beanClass.addImportClass("com.thoughtworks.xstream.annotations.XStreamImplicit");
                        beanClass.addImportClass("java.util.List");
//                        field.addFieldAnno("@XStreamImplicit");
                    }

                }else if(childXmlNode.isSingleton()) {
                    field = new Field(ResourceWrapper.JavaUtil.getJavaClassName(childXmlNode.getNodeName()),
                            ResourceWrapper.JavaUtil.getJavaVarName(childXmlNode.getNodeName()));
                    descriptor.executeFieldExtend(field,childXmlNode);
                }else {
                    field = new Field("List<" + ResourceWrapper.JavaUtil.getJavaClassName(childXmlNode.getNodeName()) + ">",
                            ResourceWrapper.JavaUtil.getJavaVarName(childXmlNode.getNodeName()) + "List");
//                    beanClass.addImportClass("com.thoughtworks.xstream.annotations.XStreamImplicit");
                    beanClass.addImportClass("java.util.List");
                    descriptor.executeFieldExtend(field,childXmlNode);
//                    field.addFieldAnno("@XStreamImplicit");
                }
                field.addFieldAnno("@JsonProperty(\"" + xmlNodeName + "\")");
                beanClass.addField(field);

                if(childXmlNode.isGenerated()) {
                    continue;
                }
                childXmlNode.setGenerated(true);
            }
        }
        return beanClass;
    }


    /**
     * 获取子元素
     * @param data
     * @return
     */
    private static String getSubElementName(Object data) {
        if(data instanceof List) {
            return (String) ((List) data).get(1);
        }

        return null;
    }


    /**
     * 生成类文件
     * @param rootClassName
     * @param data
     * @param isRoot
     */
    private static void generateClassByJson(GenerateDescriptor descriptor,  String rootClassName, Object data, boolean isRoot, Map<String,Integer> nameRepeatCache){
        Class beanClass = new Class();
        beanClass.setSrcFilePath(descriptor.getJavaRootPath());
        beanClass.setClassPackage(descriptor.getJavaPackage());
        Integer integer = nameRepeatCache.get(rootClassName);
        beanClass.setClassName(rootClassName + (integer==null ? "" : integer));
        nameRepeatCache.put(rootClassName, (integer == null ? 1 : ++integer));
        beanClass.addConstructor();

        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        if(data instanceof Map) {
            dataMap = (Map<String, Object>) data;
        }else if(data instanceof List){
            dataMap = (Map<String, Object>) ((List) data).get(0);
        }else {
            return ;
        }
        beanClass.addImportClass("com.fasterxml.jackson.annotation.JsonProperty");
        for (String fieldName : dataMap.keySet()) {
            Integer integer2 = nameRepeatCache.get(ResourceWrapper.JavaUtil.getJavaClassName(fieldName));
            Field field = getField(fieldName + (integer2 == null ? "" : integer2), dataMap.get(fieldName));
            field.addFieldAnno("@JsonProperty(\"" + fieldName + "\")");

            beanClass.addField(field);
            if(!"String".equals(field.getType())) {
                if(field.getType().startsWith("List<") && !beanClass.getImportClassList().contains("java.util.List")) {
                       beanClass.addImportClass("java.util.List");
                }
                if(isRoot) {
                    beanClass.addImportClass(descriptor.getJavaPackage() + "." + ResourceWrapper.JavaUtil.getJavaClassName(rootClassName).toLowerCase() + ".*");
                }

                GenerateDescriptor newDescriptor = descriptor;
                try {
                    newDescriptor = (GenerateDescriptor) BeanUtils.cloneBean(descriptor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                newDescriptor.setJavaPackage(newDescriptor.getJavaPackage() +
                        (isRoot ? ("." + ResourceWrapper.JavaUtil.getJavaClassName(rootClassName).toLowerCase()) : ""));
                generateClassByJson(newDescriptor,
                        ResourceWrapper.JavaUtil.getJavaClassName(fieldName), dataMap.get(fieldName), false, nameRepeatCache);
            }
        }

        Map map = new HashMap();
        map.put("CLASS", beanClass);
        String content = VelocityUtil.produceTemplateContent(descriptor.getTemplatePath(), map);
        System.out.println(content);
        FileUtils.writeFile(beanClass.getFilePath(), content);
    }

    /**
     * 获取字段定义
     * @param fieldName
     * @param data
     * @param subElementName
     * @return
     */
    private static Field getField(String fieldName, Object data, String subElementName) {
        if(data instanceof Map) {
            return new Field(ResourceWrapper.JavaUtil.getJavaClassName(fieldName),ResourceWrapper.JavaUtil.getJavaVarName(fieldName));
        }else if(data instanceof List){
            if(subElementName != null) {
                return new Field("List<" + ResourceWrapper.JavaUtil.getJavaClassName(subElementName) + ">", ResourceWrapper.JavaUtil.getJavaVarName(subElementName) + "List");
            }else {
                return new Field("List<" + ResourceWrapper.JavaUtil.getJavaClassName(fieldName) + ">", ResourceWrapper.JavaUtil.getJavaVarName(fieldName) + "List");
            }

        }else {
            return new Field("String",ResourceWrapper.JavaUtil.getJavaVarName(fieldName));
        }
    }

    /**
     * 获取字段定义
     * @param fieldName
     * @param data
     * @return
     */
    private static Field getField(String fieldName, Object data) {
        if(data instanceof Map) {
            return new Field(ResourceWrapper.JavaUtil.getJavaClassName(fieldName),ResourceWrapper.JavaUtil.getJavaVarName(fieldName));
        }else if(data instanceof List){
            return new Field("List<" + ResourceWrapper.JavaUtil.getJavaClassName(fieldName) + ">", ResourceWrapper.JavaUtil.getJavaVarName(fieldName) + "List");
        }else {
            return new Field("String",ResourceWrapper.JavaUtil.getJavaVarName(fieldName));
        }
    }

    private static void generateClass(String packagePath, Map<String, Object> mergeMap) {


    }

    private static boolean checkElementIsArray(Element element) {
        List elements = element.elements();
        if(elements.size() > 1) {
            Element element1 = (Element) elements.get(0);
            Element element2 = (Element) elements.get(1);
            if(element1.getName().equals(element2.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解析XML节点信息
     * @param element
     * @return
     */
    @Deprecated
    private static Object parseXmlNode(Element element) {
        if(checkElementIsArray(element)) {
            List result = new ArrayList();
            String xmlElementName = null;
            for (Object o : element.elements()) {
                Element subElement = (Element) o;
                xmlElementName = subElement.getName();//子元素名称
                Map<String, Object> fieldMap = (Map<String, Object>)parseXmlNode(subElement);
                //result列表第一个元素存放子元素信息
                mergeField(result, fieldMap);
            }
            //result列表第二个元素存放子元素节点名称
            result.add(xmlElementName);
            //result列表第二个元素存放元素节点属性
            result.add(getAttrMap(element));
            return result;
        }

        Map<String, Object> fieldMap = new LinkedHashMap<String, Object>();
        for (Object o : element.elements()) {
            Element subElement = (Element) o;
            fieldMap.put(subElement.getName(), parseXmlNode(subElement));
        }

        if(fieldMap.size() == 0) {
            return element.getTextTrim();
        }

        return fieldMap;
    }

    /**
     * 解析XML节点信息
     * @param element
     * @return
     */
    private static XmlNode parseXmlNodeNew(Element element) {

        XmlNode xmlNode = new XmlNode();
        xmlNode.setNodeName(element.getName());
        xmlNode.setAttrMap(getAttrMap(element));

        //添加节点属性
        for (Object attr : element.attributes()) {
            Attribute attribute= (Attribute) attr;
            xmlNode.addNodeAttr(attribute.getName(), attribute.getValue());
        }

        //添加子节点信息
        for (Object o : element.elements()) {
            XmlNode subXmlNode = parseXmlNodeNew((Element) o);
            xmlNode.addOrMergeChildNode(subXmlNode);
            subXmlNode.setParentXmlNode(xmlNode);
        }

        if(element.elements().size() == 0) {
            xmlNode.setNodeText(element.getTextTrim());
        }
        return xmlNode;
    }

    /**
     * 解析XML节点信息
     * @param element
     * @return
     */
    public static XmlNode parseXmlNodeData(Element element) {

        XmlNode xmlNode = new XmlNode();
        xmlNode.setNodeName(element.getName());
        xmlNode.setAttrMap(getAttrMap(element));

        //添加子节点信息
        for (Object o : element.elements()) {
            XmlNode subXmlNode = parseXmlNodeData((Element) o);
            xmlNode.addNode(subXmlNode);
            subXmlNode.setParentXmlNode(xmlNode);
        }

        if(element.elements().size() == 0) {
            xmlNode.setNodeText(element.getTextTrim());
        }
        return xmlNode;
    }

    /**
     * 解析Json节点信息
     * @param jsonNode
     * @return
     */
    public static Object parseJsonNode(JsonNode jsonNode) {

        if(jsonNode.isArray()) {
            List result = new ArrayList();
            for (JsonNode subNode : jsonNode) {
                Map<String, Object> fieldMap = (Map<String, Object>)parseJsonNode(subNode);
                mergeField(result, fieldMap);
            }
            return result;
        }

        Map<String, Object> fieldMap = new LinkedHashMap<String, Object>();
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            fieldMap.put(field.getKey(), parseJsonNode(field.getValue()));
        }

        if(fieldMap.size() == 0) {
            return jsonNode.asText();
        }
        return fieldMap;
    }

    /**
     * 解析Json节点信息
     * @param jsonNode
     * @return
     */
    private static XmlNode parseXmlNode(String key, JsonNode jsonNode) {

        XmlNode xmlNode = new XmlNode();
        xmlNode.setNodeName(key);
        if(jsonNode.isArray()) {
            xmlNode.setIsSingleton(false);//这一句是不能少的，因为可能jsonNode子元素为空，这个时候就需要用到默认的Node节点
            boolean flag = false;//防止合并后子树节点父节点还是挂靠老的父节点，而老的父节点已经被合并弃用
            for (JsonNode subNode : jsonNode) {
                XmlNode subXmlNode = parseXmlNode(key,subNode);
                if(flag == false) {
                    xmlNode = subXmlNode;
                    xmlNode.setNodeName(key);
                    xmlNode.setIsSingleton(false);
                    flag = true;
                }else {
                    xmlNode.mergeOutSide(subXmlNode);
                }
            }
            return xmlNode;
        }


        boolean flag = false;
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            flag = true;
            Map.Entry<String, JsonNode> field = fields.next();
            JsonNode subJsonNode = field.getValue();
            XmlNode subXmlNode = parseXmlNode(field.getKey(), subJsonNode);
//            subXmlNode.setNodeName(field.getKey());
            xmlNode.addOrMergeChildNode(subXmlNode);
            subXmlNode.setParentXmlNode(xmlNode);
        }



        if(!flag) {
            xmlNode.setNodeText(jsonNode.asText());
        }
        return xmlNode;
    }



    private static Map<String,String> getAttrMap(Element element) {
        Map attrMap = new LinkedHashMap();
        for (Object attr : element.attributes()) {
            Attribute attribute= (Attribute) attr;
            attrMap.put(attribute.getName(),attribute.getValue());
        }
        return attrMap;
    }

    /**
     * 属性合并
     * @param result
     * @param fieldMap
     */
    private static void mergeField(List result, Map<String, Object> fieldMap) {
        if(result == null || result.size() == 0) {
            result.add(fieldMap);
        }else {
            ((Map<String, Object>) result.get(0)).putAll(fieldMap);
        }
    }
}
