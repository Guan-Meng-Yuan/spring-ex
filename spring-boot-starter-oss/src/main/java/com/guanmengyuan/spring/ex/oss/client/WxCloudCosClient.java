package com.guanmengyuan.spring.ex.oss.client;

import com.dtflys.forest.annotation.Get;
import com.guanmengyuan.spring.ex.oss.dto.CosSecretInfo;

public interface WxCloudCosClient {

    @Get(WxCloudApiConstant.COS_AUTH)
    CosSecretInfo getCosSecretInfo();
}
