package com.guanmengyuan.spring.ex.jy.interceptor;


import com.github.lianjiatech.retrofit.spring.boot.retry.GlobalRetryProperty;
import com.github.lianjiatech.retrofit.spring.boot.retry.RetryInterceptor;
import com.github.lianjiatech.retrofit.spring.boot.retry.RetryRule;
import com.guanmengyuan.spring.ex.common.model.dto.req.JYToken;
import com.guanmengyuan.spring.ex.common.model.dto.res.JYRes;
import com.guanmengyuan.spring.ex.jy.config.JYProperties;
import com.guanmengyuan.spring.ex.jy.client.JYAuthClient;
import com.guanmengyuan.spring.ex.jy.constant.JYConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.dromara.hutool.extra.spring.SpringUtil;
import org.dromara.hutool.json.JSONUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Primary
@Component
public class JYRetryInterceptor extends RetryInterceptor {

    private static String token;
    private final JYProperties jyProperties;

    public JYRetryInterceptor(JYProperties jyProperties) {
        super(new GlobalRetryProperty());
        this.jyProperties = jyProperties;
    }

    @Override
    @SneakyThrows
    protected Response retryIntercept(int maxRetries, int intervalMs, RetryRule[] retryRules, Chain chain) {
        Request request = chain.request();
        while (maxRetries > 0) {
            request = request.newBuilder().header(JYConstant.AUTH_HEADER_NAME, JYConstant.AUTH_PREFIX + token).build();
            Response response = chain.proceed(request);
            ResponseBody body = response.body();
            BufferedSource source = null;
            if (null != body) {
                source = body.source();
            }
            String bodyJson = null;
            if (null != source) {
                try (Buffer clonedBuffer = source.getBuffer().clone()) {
                    bodyJson = clonedBuffer.readString(StandardCharsets.UTF_8);
                }
            }
            JYRes<?> jyRes = JSONUtil.toBean(bodyJson, JYRes.class);
            if (jyRes.getSuccess()) {
                return response;
            } else {
                log.error("聚英客户端请求失败,重试中...");
                ThreadUtil.safeSleep(1000);
                JYRes<JYToken> login = SpringUtil.getBean(JYAuthClient.class).login(jyProperties);
                token = login.getData().getAccessToken();
                maxRetries--;
                response.close();
            }
        }
        return chain.proceed(request);
    }

}
