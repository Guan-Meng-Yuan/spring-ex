package com.guanmengyuan.spring.ex.web.config;

import com.guanmengyuan.spring.ex.common.model.dto.res.R;
import com.guanmengyuan.spring.ex.common.model.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.bean.BeanUtil;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class GlobalErrorController extends BasicErrorController {
    public GlobalErrorController(ServerProperties serverProperties) {
        super(new DefaultErrorAttributes(), serverProperties.getError());
    }

    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Map<String, Object> model = Collections
                .unmodifiableMap(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML)));
        response.setStatus(status.value());

        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        ServiceException serviceException = new ServiceException(status, body.get("error").toString(), "网络异常");
        log.error("request error:{}", body.get("path") + ":" + body.get("error"), serviceException);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return (modelAndView != null) ? modelAndView
                : new ModelAndView(new MappingJackson2JsonView())
                .addAllObjects(BeanUtil.beanToMap(R.error(serviceException), false, true));
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        ServiceException serviceException = new ServiceException(status, body.get("error").toString(), "网络异常");
        log.error("request error:{}", body.get("path") + ":" + body.get("error"), serviceException);
        return new ResponseEntity<>(BeanUtil.beanToMap(R.error(serviceException)), Objects.requireNonNull(status));
    }
}
