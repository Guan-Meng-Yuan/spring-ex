package com.guanmengyuan.spring.ex.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CaptchaType {
    /**
     * 线段干扰
     */
    LINE,
    /**
     * 圆圈干扰
     */
    CIRCLE,
    /**
     * 扭曲干扰
     */
    SHEAR,
    /**
     * gif
     */
    GIF,
    /**
     * 自定义
     */
    CUSTOM,
    ;
}
