package com.hframework.smartweb.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

/**
 * Created by zhangquanhong on 2017/3/1.
 */
public class SmartMessage<T> {

    private String message;
    private MessageType messageType;

    public SmartMessage(String message, MessageType messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    public static SmartMessage valueOf(String message, MessageType messageType) {
        return new SmartMessage(message, messageType);
    }

    public static enum MessageType{
        Object, Json, Xml;
    }

    public Object getObject(){
        if(MessageType.Json.equals(messageType)) {
            return JSON.parse(message, JSON.DEFAULT_PARSER_FEATURE |= Feature.OrderedField.getMask());
        }
        return message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
