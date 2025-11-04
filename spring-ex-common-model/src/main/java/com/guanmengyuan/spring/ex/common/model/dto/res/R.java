package com.guanmengyuan.spring.ex.common.model.dto.res;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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
import cn.hutool.v7.core.collection.CollUtil;
import cn.hutool.v7.core.text.StrUtil;
import lombok.Data;

/**
 * 统一响应dto
 *
 * @param <T> 任意类型
 */
@Data
public class R<T> implements Serializable {
    /**
     * 默认构造
     */
    public R() {
    }

    /**
     * 响应码
     */
    private String code;

    /**
     * 是否成功
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
        R<?> r = okNoData();
        r.setTraceId(traceId);
        return r;
    }

    /**
     * 正常响应无数据
     *
     * @return 无数据的响应
     */
    public static R<?> okNoData() {
        R<?> r = new R<>();
        r.setSuccess(Boolean.TRUE);
        r.setCode(ResEnum.SUCCESS.getCode());
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
        r.setCode(ResEnum.INTERNAL_SERVER_ERROR.getCode());
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
            r.setCode(serviceException.getCode());
        } else if (error instanceof WebExchangeBindException webExchangeBindException) {
            List<ObjectError> allErrors = webExchangeBindException.getAllErrors();
            if (CollUtil.isNotEmpty(allErrors)) {
                r.setTips(StrUtil.defaultIfBlank(allErrors.get(0).getDefaultMessage(),
                        ResEnum.PARAM_EMPTY.getTips()));
                r.setCode(ResEnum.PARAM_EMPTY.getCode());
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
        r.setCode(serviceException.getCode());
        r.setHttpStatusCode(serviceException.getStatusCode());
        r.setSuccess(Boolean.FALSE);
        return r;
    }

    /**
     * 404响应
     *
     * @param throwable 404异常
     * @return this
     */
    public static R<?> notFound(Throwable throwable) {
        R<?> r = new R<>();
        r.setTips(ResEnum.RESOURCE_NOT_FOUND.getTips());
        r.setCode(ResEnum.RESOURCE_NOT_FOUND.getCode());
        r.setHttpStatusCode(ResEnum.RESOURCE_NOT_FOUND.getHttpStatusCode());
        r.setMessage(StrUtil.defaultIfBlank(throwable.getMessage(), ResEnum.RESOURCE_NOT_FOUND.getMessage()));
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
        switch (throwable) {
            // 如果是内置业务异常
            case ServiceException serviceException -> {
                r.setMessage(serviceException.getMessage());
                r.setTips(serviceException.getTips());
                r.setCode(serviceException.getCode());
                r.setHttpStatusCode(serviceException.getStatusCode());
            }
            // 如果是参数校验异常
            case MethodArgumentNotValidException methodArgumentNotValidException -> {
                r.setTips(Objects.requireNonNull(methodArgumentNotValidException.getBindingResult().getFieldError())
                        .getDefaultMessage());
                r.setCode(ResEnum.PARAM_EMPTY.getCode());
                r.setHttpStatusCode(HttpStatus.BAD_REQUEST);
                r.setMessage(throwable.getMessage());
            }
            // SaToken异常
            case SaTokenException saTokenException -> {
                if (saTokenException instanceof NotLoginException) {
                    r.setTips(ResEnum.NOT_LOGIN.getTips());
                    r.setCode(ResEnum.NOT_LOGIN.getCode());
                } else {
                    r.setTips(ResEnum.NO_PERMISSION.getTips());
                    r.setCode(ResEnum.NO_PERMISSION.getCode());
                }
                r.setHttpStatusCode(ResEnum.NOT_LOGIN.getHttpStatusCode());
                r.setMessage(saTokenException.getMessage());
            }
            case null, default -> {
                r.setTips(ResEnum.INTERNAL_SERVER_ERROR.getTips());
                r.setCode(ResEnum.INTERNAL_SERVER_ERROR.getCode());
                r.setHttpStatusCode(ResEnum.INTERNAL_SERVER_ERROR.getHttpStatusCode());
                if (throwable != null) {
                    r.setMessage(throwable.getMessage());
                }
            }
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
        r.setCode(resEnum.getCode());
        r.setMessage(resEnum.getMessage());
        r.setTips(resEnum.getTips());
        r.setHttpStatusCode(resEnum.getHttpStatusCode());
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

    /**
     * 根据ResEnum创建错误响应
     *
     * @param resEnum 响应枚举
     * @param <T>     泛型类型
     * @return 错误响应
     */
    public static <T> R<T> error(ResEnum resEnum) {
        R<T> r = new R<>();
        r.setSuccess(Boolean.FALSE);
        setResEnum(r, resEnum);
        return r;
    }

    /**
     * 根据ResEnum创建错误响应（带自定义消息）
     *
     * @param resEnum  响应枚举
     * @param message  自定义消息
     * @param <T>      泛型类型
     * @return 错误响应
     */
    public static <T> R<T> error(ResEnum resEnum, String message) {
        R<T> r = new R<>();
        r.setSuccess(Boolean.FALSE);
        r.setCode(resEnum.getCode());
        r.setMessage(message);
        r.setTips(resEnum.getTips());
        r.setHttpStatusCode(resEnum.getHttpStatusCode());
        return r;
    }

    /**
     * 根据ResEnum创建错误响应（带自定义消息和提示）
     *
     * @param resEnum  响应枚举
     * @param message  自定义消息
     * @param tips     自定义提示
     * @param <T>      泛型类型
     * @return 错误响应
     */
    public static <T> R<T> error(ResEnum resEnum, String message, String tips) {
        R<T> r = new R<>();
        r.setSuccess(Boolean.FALSE);
        r.setCode(resEnum.getCode());
        r.setMessage(message);
        r.setTips(tips);
        r.setHttpStatusCode(resEnum.getHttpStatusCode());
        return r;
    }

}
