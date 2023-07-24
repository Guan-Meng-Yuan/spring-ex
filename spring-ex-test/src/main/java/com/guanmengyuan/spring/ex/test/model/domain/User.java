package com.guanmengyuan.spring.ex.test.model.domain;

import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户
 */
@Table("user")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseDomain {
    /**
     * 测试名称
     */
    private String name;
}
