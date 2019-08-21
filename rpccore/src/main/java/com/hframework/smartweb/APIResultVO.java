package com.hframework.smartweb;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2016/5/24.
 */
public class APIResultVO<T> {

    private String resultCode;
    private String resultMessage;
    private T data;

    public  static <K> APIResultVO success(){
        APIResultVO APIResultVO = new APIResultVO(APIErrorType.SUCCESS);
        return APIResultVO;
    }
    public  static <K> APIResultVO success(K data){
        APIResultVO APIResultVO = new APIResultVO(APIErrorType.SUCCESS);
        APIResultVO.setData(data);
        return APIResultVO;
    }

    public  static <K> APIResultVO success(K data, String resultMessage){
        APIResultVO APIResultVO = new APIResultVO(APIErrorType.SUCCESS);
        APIResultVO.setData(data);
        APIResultVO.setResultMessage(resultMessage);
        return APIResultVO;
    }

    public static APIResultVO error(String code){
        return new APIResultVO(APIErrorType.get(code));
    }

    public static APIResultVO error(String code, String errorMsg){
        return new APIResultVO(APIErrorType.get(code,errorMsg));
    }


    public static APIResultVO error(APIErrorType apiErrorType){
        return new APIResultVO(apiErrorType);
    }

    public static APIResultVO error(APIErrorType apiErrorType, String errorMsg){
        APIResultVO error = APIResultVO.error(apiErrorType);
        error.setResultMessage(errorMsg);
        return  error;
    }

    private APIResultVO(){
        super();
    }
    private APIResultVO(APIErrorType resultCode){
        this.resultCode = resultCode.getErrorCode();
        this.resultMessage = resultCode.getErrorMsg();
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public APIResultVO add(String key, Object value) {
        if(data == null) {
            data = (T) new LinkedHashMap<String, Object>();
        }
        ((Map)data).put(key, value);
        return this;
    }

//    public boolean isSuccess() {
//        return APIErrorType.SUCCESS.getErrorCode().equals(this.getResultCode());
//    }

}
