package com.guanmengyuan.spring.ex.common.model.dto.res;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guanmengyuan.spring.ex.common.model.enums.ResEnum;
import com.guanmengyuan.spring.ex.common.model.exception.ServiceException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import java.io.Serializable;
import java.util.List;

/**
 * 统一响应dto
 *
 * @param <T> 任意类型
 */
@Data
public class Res<T> implements Serializable {
    /**
     * 是否成功<br/>true 操作成功<br/>false 操作失败
     */
    private Boolean success;

    /**
     * 响应消息
     */
    @JsonIgnore
    private String message;
    /**
     * 响应数据,所有的数据响应都会在外层包装此类型
     */
    private T data;

    /**
     * 用户提示
     */
    private String tips;

    /**
     * 请求ID
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String traceId;

    @JsonIgnore
    private HttpStatusCode httpStatusCode;

    public static Res<?> successNoData(String traceId) {
        Res<?> res = successNoData();
        res.setTraceId(traceId);
        return res;
    }


    public static Res<?> successNoData() {
        Res<?> res = new Res<>();
        res.setSuccess(Boolean.TRUE);
        setResEnum(res, ResEnum.SUCCESS);
        return res;
    }


    public static Res<?> error(Throwable error, ServerWebExchange exchange) {
        Res<?> res = new Res<>();
        res.setSuccess(Boolean.FALSE);
        res.setTraceId(exchange.getRequest().getId());
        res.setMessage(StrUtil.format("request path error,path:{},errorMessage:{}", exchange.getRequest().getPath(),
                error.getMessage()));
        res.setTips(ResEnum.INTERNAL_SERVER_ERROR.getTips());
        if (error instanceof ResponseStatusException responseStatusException) {
            setRes(error, responseStatusException, res);
        } else {
            res.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return res;
    }

    private static void setRes(Throwable error, ResponseStatusException responseStatusException, Res<?> res) {
        HttpStatusCode statusCode = responseStatusException.getStatusCode();
        res.setHttpStatusCode(statusCode);
        if (error instanceof ServiceException serviceException) {
            res.setTips(serviceException.getTips());
        } else if (error instanceof WebExchangeBindException webExchangeBindException) {
            List<ObjectError> allErrors = webExchangeBindException.getAllErrors();
            if (CollUtil.isNotEmpty(allErrors)) {
                res.setTips(StrUtil.emptyToDefault(allErrors.get(0).getDefaultMessage(), ResEnum.INTERNAL_SERVER_ERROR.getTips()));
            }
        }
    }

    public static Res<?> error(ServiceException serviceException) {
        Res<?> res = new Res<>();
        res.setMessage(serviceException.getMessage());
        res.setTips(serviceException.getTips());
        res.setHttpStatusCode(serviceException.getStatusCode());
        res.setSuccess(Boolean.FALSE);
        return res;
    }

    public static Res<?> error(Throwable throwable) {
        Res<?> res = new Res<>();
        if (throwable instanceof ServiceException serviceException) {
            res.setMessage(serviceException.getMessage());
            res.setTips(serviceException.getTips());
            res.setHttpStatusCode(serviceException.getStatusCode());
        } else {
            res.setTips(ResEnum.INTERNAL_SERVER_ERROR.getTips());
            res.setHttpStatusCode(ResEnum.INTERNAL_SERVER_ERROR.getHttpStatusCode());
            res.setMessage(throwable.getMessage());
        }

        res.setSuccess(Boolean.FALSE);
        return res;
    }

    public static Res<?> error(Throwable error, String requestId) {
        Res<?> res = new Res<>();
        res.setSuccess(Boolean.FALSE);
        res.setTraceId(requestId);
        res.setTips(ResEnum.INTERNAL_SERVER_ERROR.getTips());
        if (error instanceof ResponseStatusException responseStatusException) {
            setRes(error, responseStatusException, res);
        } else if (error instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            List<ObjectError> allErrors = methodArgumentNotValidException.getAllErrors();
            if (CollUtil.isNotEmpty(allErrors)) {
                res.setTips(StrUtil.emptyToDefault(allErrors.get(0).getDefaultMessage(), ResEnum.INTERNAL_SERVER_ERROR.getTips()));
            }
        } else {
            res.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return res;
    }


    @SuppressWarnings("SameParameterValue")
    private static void setResEnum(Res<?> res, ResEnum resEnum) {
        res.setMessage(resEnum.getMessage());
        res.setTips(resEnum.getTips());
        res.setHttpStatusCode(res.httpStatusCode);
    }

    public static <T> Res<T> success(T data, String traceId) {
        Res<T> res = success(data);
        res.setTraceId(traceId);
        return res;
    }

    public static <T> Res<T> success(T data) {
        Res<T> res = new Res<>();
        res.setData(data);
        res.setSuccess(Boolean.TRUE);
        setResEnum(res, ResEnum.SUCCESS);
        return res;
    }


}
