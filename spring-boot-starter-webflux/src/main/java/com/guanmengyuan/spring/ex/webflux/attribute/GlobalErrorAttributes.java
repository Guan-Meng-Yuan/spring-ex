package com.guanmengyuan.spring.ex.webflux.attribute;

import com.guanmengyuan.spring.ex.common.model.dto.res.Res;
import org.dromara.hutool.core.bean.BeanUtil;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);
        error.printStackTrace();
        return BeanUtil.beanToMap(Res.error(error, request.exchange()), false, true);
    }
}
