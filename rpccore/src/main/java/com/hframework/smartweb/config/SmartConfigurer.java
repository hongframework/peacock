package com.hframework.smartweb.config;

import com.google.common.base.Enums;
import com.hframework.common.util.PathMatcherUtils;
import com.hframework.common.util.message.PropertyReader;

/**
 * Created by zhangquanhong on 2017/6/9.
 */
public class SmartConfigurer {

    private static final String MODE = "smartapi.mode";
    private static final String CHECKER_DISABLE = "smartapi.checker.disable";

    private static final String AUTH_USER = "smartapi.auth.user";
    private static final String AUTH_CLIENTID = "smartapi.auth.clientId";
    private static final String AUTH_KEY = "smartapi.auth.key";
    private static final String AUTH_PATH = "smartapi.auth.path";

    private static PropertyReader propertyReader =
            PropertyReader.read("smartapi.properties")
                    .merge("properties/smartapi.properties")
                    .addDefine(MODE, CHECKER_DISABLE, AUTH_USER,
                            AUTH_CLIENTID, AUTH_KEY, AUTH_PATH);

    public static Mode getMode(){
        return Enums.getIfPresent(Mode.class, propertyReader.get(MODE,"publish")).get();
    }

    public static boolean isDebugMode() {
        return getMode().isDebug();
    }

    public static boolean isCheckerDisable() {
        return propertyReader.get(CHECKER_DISABLE, "false").equals("true");
    }

    public static boolean notExistsClientId(String clientId) {
        return !propertyReader.getAsList(AUTH_CLIENTID).containsKey("." + clientId);
    }

    public static String getKey(String clientId) {
        return propertyReader.get(AUTH_KEY + "." + clientId);
    }

    public static boolean checkUrlAuth(String clientId, String url) {
        String path = propertyReader.get(AUTH_PATH);
        boolean matched = PathMatcherUtils.matches(path, url);
        if(matched) {
             path = propertyReader.get(AUTH_PATH + "." + clientId);
             matched = PathMatcherUtils.matches(path, url);
        }
        return matched;
    }

    public static enum Mode{
        debug,publish;
        public boolean isDebug(){
            return this.equals(debug);
        }
    }

}
