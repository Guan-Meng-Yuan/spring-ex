package com.guanmengyuan.spring.ex.test.model.domain;

import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户
 */
@Table("user")
@Data(staticConstructor = "create")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseDomain<User> {
    /**
     * 测试名称
     */
    private String name;
}
