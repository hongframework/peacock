package com.hframework.smartweb;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2016/5/24.
 */
public class APIErrorType {

    public static final APIErrorType SUCCESS = new APIErrorType("0","成功");
    public static final APIErrorType ERROR = new APIErrorType("-1","系统异常");
    public static final APIErrorType UNKNOW = new APIErrorType("-1","未知错误");
    public static final APIErrorType SERVER_ERROR = new APIErrorType("-1","系统异常");
    public static final APIErrorType ACCOUNT_TRY_TOO_MUCH = new APIErrorType("-2","尝试次数过多");
    public static final APIErrorType ILLEGAL_INPUT = new APIErrorType("-3","非法输入");
    public static final APIErrorType PARAMETER_ERROR = new APIErrorType("99","参数错误");
    public static final APIErrorType ILLEGAL_VISIT = new APIErrorType("-4","未被授权，不能访问！");

    public static final APIErrorType RECODE_IS_NOT_EXISTS = new APIErrorType("1001","没有查询到对应记录！");

    private String errorCode;
    private String errorMsg;

    private static Map<String, APIErrorType> errorCodeMap = new HashMap<String, APIErrorType>();

    private APIErrorType(String errorCode, String errorMsg) {

        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static boolean isSuccess(String code) {
        return SUCCESS.getErrorCode().equals(code);
    }

    public static APIErrorType get(String code) {
        if(errorCodeMap.get(code) != null) {
            return errorCodeMap.get(code);
        }else {
            return new APIErrorType(code,"TODO");
        }
    }

    public static APIErrorType get(String code, String errorMsg) {
        if(errorCodeMap.get(code) != null) {
            return errorCodeMap.get(code);
        }else {
            return new APIErrorType(code,errorMsg);
        }
    }
}
