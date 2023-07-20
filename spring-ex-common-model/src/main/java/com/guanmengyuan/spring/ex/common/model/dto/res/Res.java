package com.guanmengyuan.spring.ex.common.model.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.guanmengyuan.spring.ex.common.model.enums.BaseResEnum;
import com.guanmengyuan.spring.ex.common.model.enums.ResEnum;
import com.guanmengyuan.spring.ex.common.model.exception.ServiceException;
import com.mybatisflex.core.paginate.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    private String traceId;

    @JsonIgnore
    private HttpStatusCode httpStatusCode;

    public static Res<?> successNoData(String traceId) {
        Res<?> res = new Res<>();
        res.setSuccess(Boolean.TRUE);
        res.setTraceId(traceId);
        setResEnum(res, ResEnum.SUCCESS);
        return res;
    }

    public static <T> Res<PageRes<T>> page(Page<T> page, String traceId) {
        Res<PageRes<T>> res = new Res<>();
        PageRes<T> pageRes = new PageRes<>(page.getRecords(), page.getPageNumber(), page.getPageSize(),
                page.getTotalRow(), page.hasNext());
        res.setData(pageRes);
        res.setTraceId(traceId);
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
        res.setTips("网络异常");
        if (error instanceof ResponseStatusException responseStatusException) {
            HttpStatusCode statusCode = responseStatusException.getStatusCode();
            res.setHttpStatusCode(statusCode);
            if (error instanceof ServiceException serviceException) {
                res.setTips(serviceException.getTips());
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
        Res<T> res = new Res<>();
        res.setData(data);
        res.setTraceId(traceId);
        res.setSuccess(Boolean.TRUE);
        setResEnum(res, ResEnum.SUCCESS);
        return res;
    }


    public static void cast(BaseResEnum resEnum) {
        throw new ServiceException(resEnum);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageRes<T> {
        /**
         * 分页数据
         */
        private List<T> list;
        /**
         * 当前页
         */
        private Integer pageNumber;
        /**
         * 每页条数
         */
        private Integer pageSize;
        /**
         * 总数
         */
        private Long totalRow;
        /**
         * 是否有下一页
         */
        private Boolean hasNext;
    }

}
