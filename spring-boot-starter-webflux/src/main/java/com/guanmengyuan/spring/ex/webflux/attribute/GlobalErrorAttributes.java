package com.guanmengyuan.spring.ex.webflux.attribute;

import java.util.Map;

import org.dromara.hutool.core.bean.BeanUtil;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.guanmengyuan.spring.ex.common.model.dto.res.Res;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);
        log.error("web error , {}", error.getMessage(), error);
        return BeanUtil.beanToMap(Res.error(error, request.exchange()), false, true);
    }
}
