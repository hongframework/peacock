package com.hframework.smartweb;

import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2016/5/24.
 */
public class DynResultVO<T>  extends LinkedHashMap<String, Object>{

    public static ThreadLocal<String> codeKeyTL = new ThreadLocal<>();
    public static ThreadLocal<String> msgKeyTL = new ThreadLocal<>();
    public static ThreadLocal<String> dataKeyTL = new ThreadLocal<>();
    public static ThreadLocal<String> codeTypeTL = new ThreadLocal<>();

    public static void  setKey(String codeKey, String msgKey, String dataKey, String codeType) {
        codeKeyTL.set(codeKey);
        msgKeyTL.set(msgKey);
        dataKeyTL.set(dataKey);
        codeTypeTL.set(codeType);
    }

    public  static <K> DynResultVO success(){
        DynResultVO resultVO = new DynResultVO(APIErrorType.SUCCESS);
        return resultVO;
    }
    public  static <K> DynResultVO success(K data){
        DynResultVO resultVO = new DynResultVO(APIErrorType.SUCCESS);
        resultVO.setData(data);
        return resultVO;
    }

    public  static <K> DynResultVO success(K data, String resultMessage){
        DynResultVO resultVO = new DynResultVO(APIErrorType.SUCCESS);
        resultVO.setData(data);
        resultVO.setResultMessage(resultMessage);
        return resultVO;
    }

    public static DynResultVO error(String code){
        return new DynResultVO(APIErrorType.get(code));
    }

    public static DynResultVO error(String code, String errorMsg){
        return new DynResultVO(APIErrorType.get(code,errorMsg));
    }

    public static DynResultVO from(APIResultVO resultVO) {
        DynResultVO resultVO1 = new DynResultVO();
        resultVO1.setResultCode(resultVO.getResultCode());
        resultVO1.setResultMessage(resultVO.getResultMessage());
        resultVO1.setData(resultVO.getData());
        return resultVO1;
    }

    public static Object from(ResultData result) {
        DynResultVO resultVO1 = new DynResultVO();
        resultVO1.setResultCode(result.getResultCode());
        resultVO1.setResultMessage(result.getResultMessage());
        resultVO1.setData(result.getData());
        return resultVO1;
    }


    public static DynResultVO error(APIErrorType apiErrorType){
        return new DynResultVO(apiErrorType);
    }

    public static DynResultVO error(APIErrorType apiErrorType, String errorMsg){
        DynResultVO error = DynResultVO.error(apiErrorType);
        error.setResultMessage(errorMsg);
        return  error;
    }

    private DynResultVO(){
        super();
    }
    private DynResultVO(APIErrorType resultCode){
        this.put(getCodeKey(), "int".equals(codeTypeTL.get()) && resultCode.getErrorCode() != null ?
                Integer.valueOf(resultCode.getErrorCode()) : resultCode.getErrorCode());
        this.put(getMsgKey(),resultCode.getErrorMsg());
    }

    public String getCodeKey(){
        return StringUtils.isNotBlank(codeKeyTL.get()) ? codeKeyTL.get() : "resultCode";
    }

    public String getMsgKey(){
        return StringUtils.isNotBlank(msgKeyTL.get()) ? msgKeyTL.get() : "resultMessage";
    }

    public String getDataKey(){
        return StringUtils.isNotBlank(dataKeyTL.get()) ? dataKeyTL.get() : "data";
    }

    public Object getResultCode() {
        return this.get(getCodeKey());
    }

    public void setResultCode(String resultCode) {
        this.put(getCodeKey(),"int".equals(codeTypeTL.get()) ? Integer.valueOf(resultCode) : resultCode);
    }

    public String getResultMessage() {
        return String.valueOf(this.get(getMsgKey()));
    }

    public void setResultMessage(String resultMessage) {
        this.put(getMsgKey(),resultMessage);
    }

    public T getData() {
        return (T) this.get(getDataKey());
    }

    public void setData(T data) {
        this.put(getDataKey(),data);
    }

    public DynResultVO add(String key, Object value) {
        if(!this.containsKey(getDataKey())) {
            this.put(getDataKey(),(T) new LinkedHashMap<String, Object>());
        }
        ((Map)this.get(getDataKey())).put(key, value);
        return this;
    }



//    public boolean isSuccess() {
//        return APIErrorType.SUCCESS.getErrorCode().equals(this.getResultCode());
//    }

}
