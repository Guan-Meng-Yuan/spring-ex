package com.guanmengyuan.spring.ex.common.model.dto.res;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guanmengyuan.spring.ex.common.model.enums.ResEnum;
import com.guanmengyuan.spring.ex.common.model.exception.ServiceException;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
import lombok.Data;

/**
 * 统一响应dto
 *
 * @param <T> 任意类型
 */
@Data
public class R<T> implements Serializable {

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
    /**
     * http code
     */
    @JsonIgnore
    private HttpStatusCode httpStatusCode;

    /**
     * 正常响应无数据
     *
     * @param traceId 请求ID
     * @return 无数据的正常响应
     */
    public static R<?> successNoData(String traceId) {
        R<?> r = successNoData();
        r.setTraceId(traceId);
        return r;
    }

    /**
     * 正常响应无数据
     *
     * @return 无数据的响应
     */
    public static R<?> successNoData() {
        R<?> r = new R<>();
        r.setSuccess(Boolean.TRUE);
        setResEnum(r, ResEnum.SUCCESS);
        return r;
    }

    /**
     * 错误响应
     *
     * @param error    错误接口
     * @param exchange exchange实例
     * @return 错误响应
     */
    public static R<?> error(Throwable error, ServerWebExchange exchange) {
        R<?> r = new R<>();
        r.setSuccess(Boolean.FALSE);
        r.setTraceId(exchange.getRequest().getId());
        r.setMessage(StrUtil.format("request path error,path:{},errorMessage:{}", exchange.getRequest().getPath(),
                error.getMessage()));
        r.setTips(ResEnum.INTERNAL_SERVER_ERROR.getTips());
        if (error instanceof ResponseStatusException responseStatusException) {
            setRes(error, responseStatusException, r);
        } else {
            r.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return r;
    }

    private static void setRes(Throwable error, ResponseStatusException responseStatusException, R<?> r) {
        HttpStatusCode statusCode = responseStatusException.getStatusCode();
        r.setHttpStatusCode(statusCode);
        if (error instanceof ServiceException serviceException) {
            r.setTips(serviceException.getTips());
        } else if (error instanceof WebExchangeBindException webExchangeBindException) {
            List<ObjectError> allErrors = webExchangeBindException.getAllErrors();
            if (CollUtil.isNotEmpty(allErrors)) {
                r.setTips(StrUtil.defaultIfBlank(allErrors.get(0).getDefaultMessage(),
                        ResEnum.INTERNAL_SERVER_ERROR.getTips()));
            }
        }
    }

    /**
     * 错误响应
     *
     * @param serviceException 业务异常
     * @return 错误响应
     */
    public static R<?> error(ServiceException serviceException) {
        R<?> r = new R<>();
        r.setMessage(serviceException.getMessage());
        r.setTips(serviceException.getTips());
        r.setHttpStatusCode(serviceException.getStatusCode());
        r.setSuccess(Boolean.FALSE);
        return r;
    }

    public static R<?> notFound(Throwable throwable) {
        R<?> r = new R<>();
        r.setTips(ResEnum.NOT_FOUND.getTips());
        r.setHttpStatusCode(ResEnum.NOT_FOUND.getHttpStatusCode());
        r.setMessage(StrUtil.defaultIfBlank(throwable.getMessage(), ResEnum.NOT_FOUND.getMessage()));
        r.setSuccess(Boolean.FALSE);
        return r;
    }

    /**
     * 错误响应
     *
     * @param throwable 异常接口
     * @return 错误响应
     */
    public static R<?> error(Throwable throwable) {
        R<?> r = new R<>();
        // 如果是内置业务异常
        if (throwable instanceof ServiceException serviceException) {
            r.setMessage(serviceException.getMessage());
            r.setTips(serviceException.getTips());
            r.setHttpStatusCode(serviceException.getStatusCode());
            // 如果是参数校验异常
        } else if (throwable instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            r.setTips(Objects.requireNonNull(methodArgumentNotValidException.getBindingResult().getFieldError())
                    .getDefaultMessage());
            r.setHttpStatusCode(HttpStatus.BAD_REQUEST);
            r.setMessage(throwable.getMessage());
            // 其他异常
        } else if (throwable instanceof SaTokenException saTokenException) {
            if (saTokenException instanceof NotLoginException) {
                r.setTips(ResEnum.NOT_LOGIN.getTips());
            } else {
                r.setTips(ResEnum.UNAUTHORIZED.getTips());
            }
            r.setHttpStatusCode(ResEnum.UNAUTHORIZED.getHttpStatusCode());
            r.setMessage(saTokenException.getMessage());
        } else {
            r.setTips(ResEnum.INTERNAL_SERVER_ERROR.getTips());
            r.setHttpStatusCode(ResEnum.INTERNAL_SERVER_ERROR.getHttpStatusCode());
            r.setMessage(throwable.getMessage());
        }
        r.setSuccess(Boolean.FALSE);
        return r;
    }

    /**
     * setRes
     *
     * @param r       res
     * @param resEnum 响应枚举
     */
    private static void setResEnum(R<?> r, ResEnum resEnum) {
        r.setMessage(resEnum.getMessage());
        r.setTips(resEnum.getTips());
        r.setHttpStatusCode(r.httpStatusCode);
    }

    /**
     * 成功响应
     *
     * @param result  数据
     * @param traceId 请求ID
     * @param <T>     泛型类型
     * @return 响应结果
     */
    public static <T> R<T> ok(T result, String traceId) {
        R<T> r = ok(result);
        r.setTraceId(traceId);
        return r;
    }

    /**
     * 成功响应
     *
     * @param result 数据
     * @param <T>    数据类型
     * @return 成功的响应
     */
    public static <T> R<T> ok(T result) {
        R<T> r = new R<>();
        r.setData(result);
        r.setSuccess(Boolean.TRUE);
        setResEnum(r, ResEnum.SUCCESS);
        return r;
    }

}
