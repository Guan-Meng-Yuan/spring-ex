package com.guanmengyuan.spring.ex.jy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 聚英电子配置
 *
 * @author guanmengyuan
 * @see <a href=
 *      "https://www.juyingiot.com/jycloud/#/appaudit/CreatApp?appaudit=%2Fjycloudmodel%2Fopeniot%2F%23%2F">聚英官网</a>
 */
@Data
@ConfigurationProperties(prefix = "jy")
public class JYProperties {
    /**
     * 聚英电子appKey
     */
    private String appKey;
    /**
     * 聚英电子app密钥
     */
    private String appSecret;
    /**
     * 聚英电子app密钥
     */
    private String appSecretKey;

    /**
     * 聚英电子api客户端类型,默认:svr
     */
    private String clientType = "svr";
    /**
     * 聚英电子认证apiUrl
     */
    private String authUrl = "http://oauthapi.iot02.com:60080/api/v1";
    /**
     * 聚英电子openApiUrl
     */
    private String apiUrl = "http://openapi.iot02.com:60092/api/v1";
}
