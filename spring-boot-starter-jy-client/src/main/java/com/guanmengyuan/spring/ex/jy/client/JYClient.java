package com.guanmengyuan.spring.ex.jy.client;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.github.lianjiatech.retrofit.spring.boot.retry.Retry;
import com.guanmengyuan.spring.ex.common.model.dto.req.JYOpr;
import com.guanmengyuan.spring.ex.common.model.dto.res.JYRes;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 聚英电子客户端
 *
 * @author guanmengyuan
 */
@RetrofitClient(baseUrl = "${jy.api-url}")
@Retry
public interface JYClient {
    @POST("equip-opr/equip-close-all")
    JYRes<String> closeAll(@Body JYOpr jyOpr);
}
