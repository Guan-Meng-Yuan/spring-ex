package com.guanmengyuan.spring.ex.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 验证码结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaResult implements Serializable {
    /**
     * 验证码code
     */
    private String code;
    /**
     * 验证码图片
     */
    private String img;
}
