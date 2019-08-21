package com.hframework.springext.exception;

import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.smartweb.APIErrorType;
import com.hframework.smartweb.DynResultVO;
import com.hframework.smartweb.annotation.SmartResult;
import com.hframework.smartweb.exception.SmartHandlerException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/2/27.
 */
public class SmartHandlerExceptionResolver extends ExceptionHandlerExceptionResolver{
    private String defaultErrorView;

    public String getDefaultErrorView() {
        return defaultErrorView;
    }

    public void setDefaultErrorView(String defaultErrorView) {
        this.defaultErrorView = defaultErrorView;
    }

    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {

        if (handlerMethod == null || handlerMethod.getMethod() == null) {
            return null;
        }

        Method method = handlerMethod.getMethod();

        //如果定义了ExceptionHandler则返回相应的Map中的数据
        ModelAndView returnValue = super.doResolveHandlerMethodException(request, response, handlerMethod, exception);
        if(returnValue != null) {
            return returnValue;
        }

        if(isMessageBodyReturn(method)) {
            try {
                if (exception instanceof BusinessException) {
                    ResultCode resultCode = ((BusinessException) exception).getResultCode();
                    return handleResponseError(DynResultVO.error(resultCode.getErrorCode(), resultCode.getErrorMsg()), request, response);
                }else if (exception.getCause() instanceof BusinessException) {
                    ResultCode resultCode = ((BusinessException) exception.getCause()).getResultCode();
                    return handleResponseError(DynResultVO.error(resultCode.getErrorCode(), resultCode.getErrorMsg()), request, response);
                }else if (exception instanceof SmartHandlerException) {
                    return handleResponseError(DynResultVO.from(((SmartHandlerException) exception).getAPIResultVO()), request, response);
                }else if (exception.getCause() instanceof SmartHandlerException) {
                    return handleResponseError(DynResultVO.from(((SmartHandlerException) exception.getCause()).getAPIResultVO()), request, response);
                }else {
                    exception.printStackTrace();
                    return handleResponseError(DynResultVO.error(APIErrorType.SERVER_ERROR, exception.getMessage()), request, response);
                }
            } catch (Exception e) {
                return null;
            }
        }else {
            returnValue = new ModelAndView();
            returnValue.setViewName(defaultErrorView);
        }
        return returnValue;
    }

    private boolean isMessageBodyReturn(Method method) {
        ResponseBody responseBody = AnnotationUtils.findAnnotation(method, ResponseBody.class);
        SmartResult smartResult = AnnotationUtils.findAnnotation(method, SmartResult.class);
        ;
       return responseBody != null || smartResult != null || !ModelAndView.class.isAssignableFrom(method.getReturnType());
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private ModelAndView handleResponseBody(ModelAndView returnValue, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map value = returnValue.getModelMap();
        HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }
        MediaType.sortByQualityValue(acceptedMediaTypes);
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        Class<?> returnValueType = value.getClass();
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        if (messageConverters != null) {
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                for (HttpMessageConverter messageConverter : messageConverters) {
                    if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
                        messageConverter.write(value, acceptedMediaType, outputMessage);
                        return new ModelAndView();
                    }
                }
            }
        }
        if (logger.isWarnEnabled()) {
            logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and " + acceptedMediaTypes);
        }
        return null;
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private ModelAndView handleResponseError(DynResultVO returnVO, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }
        MediaType.sortByQualityValue(acceptedMediaTypes);
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
        Class<?> returnVOType = returnVO.getClass();
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        if (messageConverters != null) {
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                for (HttpMessageConverter messageConverter : messageConverters) {
                    if (messageConverter.canWrite(returnVOType, acceptedMediaType)) {
                        messageConverter.write(returnVO, acceptedMediaType, outputMessage);
                        return new ModelAndView();
                    }
                }
            }
        }
        if (logger.isWarnEnabled()) {
            logger.warn("Could not find HttpMessageConverter that supports return type [" + returnVOType + "] and " + acceptedMediaTypes);
        }
        return new ModelAndView();
    }

}