package com.guanmengyuan.spring.cloud.example.model.domain;

import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * user
 */
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class User extends BaseDomain<User> {
    /**
     * 测试名称
     */
    private String name;

}