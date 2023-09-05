package com.guanmengyuan.spring.ex.auth.util;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.guanmengyuan.spring.ex.auth.constant.CacheConstant;
import com.guanmengyuan.spring.ex.auth.enums.CaptchaType;
import com.guanmengyuan.spring.ex.auth.model.CaptchaResult;
import com.guanmengyuan.spring.ex.common.model.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class CaptchaUtils {
    private final RedisTemplate<String, String> redisTemplate;
    private static final TimedCache<String, String> IN_MEMORY_CACHE = CacheUtil.newTimedCache(Duration.ofMinutes(1).toMillis());
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 100;

    /**
     * 生成验证码
     *
     * @param captchaType   验证码类型
     * @param codeGenerator 验证码生成器 如果是 CUSTOM
     * @param width         验证码宽度
     * @param height        验证码高度
     * @return 验证码结果
     * @see CaptchaType#CUSTOM
     */
    public CaptchaResult captchaResult(CaptchaType captchaType, CodeGenerator codeGenerator, Integer width, Integer height) {

        Map<String, Object> captchaResult = MapUtil.newHashMap();
        String redisKeyID = IdUtil.getSnowflakeNextIdStr();
        String verifyKey = CacheConstant.CAPTCHA_KEY + redisKeyID;
        AbstractCaptcha captcha;
        switch (captchaType) {
            case LINE -> captcha = CaptchaUtil.createLineCaptcha(width, height);
            case GIF -> captcha = CaptchaUtil.createGifCaptcha(width, height);
            case SHEAR -> captcha = CaptchaUtil.createShearCaptcha(width, height);
            case CIRCLE -> captcha = CaptchaUtil.createCircleCaptcha(width, height);
            case CUSTOM -> {
                captcha = CaptchaUtil.createShearCaptcha(width, height);
                captcha.setGenerator(codeGenerator);
            }
            default ->
                    throw new ServiceException(HttpStatus.NOT_ACCEPTABLE, "captcha type not support", "验证码不支持");
        }


        captcha.createCode();
        String code = captcha.getCode();
        if (redisTemplate == null) {
            IN_MEMORY_CACHE.put(redisKeyID, code);
        } else {
            try {

                redisTemplate.opsForValue().set(verifyKey, code, 1, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.error("redis save captcha error", e);
                IN_MEMORY_CACHE.put(redisKeyID, code);
            }
        }
        captchaResult.put("code", redisKeyID);
        return new CaptchaResult(redisKeyID, captcha.getImageBase64Data());
    }

    /**
     * 生成验证码
     *
     * @param captchaType   验证码类型
     * @param codeGenerator 验证码生成器 如果是 CUSTOM
     * @return 验证码结果
     * @see CaptchaType#CUSTOM
     */

    public CaptchaResult captchaResult(CaptchaType captchaType, CodeGenerator codeGenerator) {
        return captchaResult(captchaType, codeGenerator, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public CaptchaResult captchaResult(CaptchaType captchaType) {
        if (captchaType.equals(CaptchaType.CUSTOM)) {
            throw new ServiceException(HttpStatus.NOT_ACCEPTABLE, "captcha type custom is need codeGenerator", "验证码类型不支持");
        }
        return captchaResult(captchaType, null);
    }

    /**
     * 生成验证码
     *
     * @return 验证码结果
     */
    public CaptchaResult captchaResult() {
        return captchaResult(CaptchaType.SHEAR);
    }

    public void verifyCaptcha(String key, String code) {
        String redisCode;
        try {
            redisCode = redisTemplate.opsForValue().getAndDelete(CacheConstant.CAPTCHA_KEY + key);
            if (StrUtil.isEmpty(redisCode)) {
                throw new ServiceException(HttpStatus.UNAUTHORIZED, "验证码已过期", "验证码已过期");
            }
            if (!StrUtil.equals(redisCode, code)) {
                throw new ServiceException(HttpStatus.UNAUTHORIZED, "验证码不正确", "验证码不正确");
            }
        } catch (Exception e) {
            redisCode = IN_MEMORY_CACHE.get(CacheConstant.CAPTCHA_KEY + key);
            if (StrUtil.isEmpty(redisCode)) {
                throw new ServiceException(HttpStatus.UNAUTHORIZED, "验证码已过期", "验证码已过期");
            }
            if (!StrUtil.equals(code, redisCode)) {
                throw new ServiceException(HttpStatus.UNAUTHORIZED, "验证码不正确", "验证码不正确");
            }

        } finally {
            IN_MEMORY_CACHE.remove(CacheConstant.CAPTCHA_KEY + key);
        }
    }

}
