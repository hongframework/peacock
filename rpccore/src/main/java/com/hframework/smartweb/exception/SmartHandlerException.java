package com.hframework.smartweb.exception;


import com.hframework.smartweb.APIErrorType;
import com.hframework.smartweb.APIResultVO;
import com.hframework.smartweb.DynResultVO;

/**
 * Created by zhangquanhong on 2017/2/22.
 */
public class SmartHandlerException extends RuntimeException {

    private APIErrorType errorType;
    private String errorMsg;

    public SmartHandlerException(Throwable cause) {
        super(cause);
    }
    public SmartHandlerException(String message) {
        super(message);
        this.errorMsg = message;
    }

    public SmartHandlerException(APIErrorType errorType) {
        this.errorType = errorType;
        this.errorMsg = errorType.getErrorMsg();
    }

    public SmartHandlerException(APIErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
        this.errorMsg = message;
    }

    public APIErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(APIErrorType errorType) {
        this.errorType = errorType;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public APIResultVO getAPIResultVO(){
        return APIResultVO.error(errorType, errorMsg);
    }
}
