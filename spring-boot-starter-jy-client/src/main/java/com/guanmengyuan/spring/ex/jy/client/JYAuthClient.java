package com.guanmengyuan.spring.ex.jy.client;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.guanmengyuan.spring.ex.common.model.dto.req.JYToken;
import com.guanmengyuan.spring.ex.common.model.dto.res.JYRes;
import com.guanmengyuan.spring.ex.jy.config.JYProperties;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 聚英认证客户端
 *
 * @author guanmengyuan
 */
@RetrofitClient(baseUrl = "${jy.auth-url}")
public interface JYAuthClient {
    @POST("user/login-app")
    JYRes<JYToken> login(@Body JYProperties jyProperties);
}
