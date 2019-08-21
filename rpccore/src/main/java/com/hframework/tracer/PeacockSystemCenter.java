package com.hframework.tracer;

import java.util.ArrayList;
import java.util.List;

public class PeacockSystemCenter {
    public static String nodeCode = null;
    public static NodeType nodeType = NodeType.UNKNOWN;
    public static List<String> maybePrograms = new ArrayList<>();
    public static String mainProgram = null;
    public enum NodeType{
        UNKNOWN((byte)0), API((byte)1),DOC((byte)2);
        byte index;
        NodeType(byte index){
            this.index = index;
        }

        public static NodeType parse(byte index) {
            if(API.index == index) {
                return API;
            }else if(DOC.index == index) {
                return DOC;
            }else {
                return UNKNOWN;
            }
        }

        public boolean isApi(){
            return this == API;
        }

        public boolean isDoc(){
            return this == DOC;
        }
        public boolean isManager(){
            return this == UNKNOWN;
        }
    }

}
