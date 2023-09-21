package com.guanmengyuan.spring.ex.oss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author guanmengyuan
 */
@AllArgsConstructor
@Getter
public enum SecretMode {
    /**
     * 普通模式
     */
    USUALLY,
    /**
     * 临时模式
     */
    TEMPORARY,
    ;
}
