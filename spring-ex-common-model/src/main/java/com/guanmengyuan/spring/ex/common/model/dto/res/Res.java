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

import cn.dev33.satoken.exception.SaTokenException;
import lombok.Data;

/**
 * 统一响应dto
 *
 * @param <T> 任意类型
 */
@Data
public class Res<T> implements Serializable {
    /**
     * 是否成功<br/>
     * true 操作成功<br/>
     * false 操作失败
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
    public static Res<?> successNoData(String traceId) {
        Res<?> res = successNoData();
        res.setTraceId(traceId);
        return res;
    }

    /**
     * 正常响应无数据
     *
     * @return 无数据的响应
     */
    public static Res<?> successNoData() {
        Res<?> res = new Res<>();
        res.setSuccess(Boolean.TRUE);
        setResEnum(res, ResEnum.SUCCESS);
        return res;
    }

    /**
     * 错误响应
     *
     * @param error    错误接口
     * @param exchange exchange实例
     * @return 错误响应
     */
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
                res.setTips(StrUtil.defaultIfBlank(allErrors.get(0).getDefaultMessage(),
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
    public static Res<?> error(ServiceException serviceException) {
        Res<?> res = new Res<>();
        res.setMessage(serviceException.getMessage());
        res.setTips(serviceException.getTips());
        res.setHttpStatusCode(serviceException.getStatusCode());
        res.setSuccess(Boolean.FALSE);
        return res;
    }

    /**
     * 错误响应
     *
     * @param throwable 异常接口
     * @return 错误响应
     */
    public static Res<?> error(Throwable throwable) {
        Res<?> res = new Res<>();
        // 如果是内置业务异常
        if (throwable instanceof ServiceException serviceException) {
            res.setMessage(serviceException.getMessage());
            res.setTips(serviceException.getTips());
            res.setHttpStatusCode(serviceException.getStatusCode());
            // 如果是参数校验异常
        } else if (throwable instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            res.setTips(Objects.requireNonNull(methodArgumentNotValidException.getBindingResult().getFieldError())
                    .getDefaultMessage());
            res.setHttpStatusCode(HttpStatus.BAD_REQUEST);
            res.setMessage(throwable.getMessage());
            // 其他异常
        } else if (throwable instanceof SaTokenException saTokenException) {
            res.setTips(ResEnum.UNAUTHORIZED.getTips());
            res.setHttpStatusCode(ResEnum.UNAUTHORIZED.getHttpStatusCode());
            res.setMessage(saTokenException.getMessage());
        } else {
            res.setTips(ResEnum.INTERNAL_SERVER_ERROR.getTips());
            res.setHttpStatusCode(ResEnum.INTERNAL_SERVER_ERROR.getHttpStatusCode());
            res.setMessage(throwable.getMessage());
        }
        res.setSuccess(Boolean.FALSE);
        return res;
    }

    /**
     * setRes
     *
     * @param res     res
     * @param resEnum 响应枚举
     */
    private static void setResEnum(Res<?> res, ResEnum resEnum) {
        res.setMessage(resEnum.getMessage());
        res.setTips(resEnum.getTips());
        res.setHttpStatusCode(res.httpStatusCode);
    }

    /**
     * 成功响应
     *
     * @param data    数据
     * @param traceId 请求ID
     * @param <T>     泛型类型
     * @return 响应结果
     */
    public static <T> Res<T> success(T data, String traceId) {
        Res<T> res = success(data);
        res.setTraceId(traceId);
        return res;
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 成功的响应
     */
    public static <T> Res<T> success(T data) {
        Res<T> res = new Res<>();
        res.setData(data);
        res.setSuccess(Boolean.TRUE);
        setResEnum(res, ResEnum.SUCCESS);
        return res;
    }

}
