package com.guanmengyuan.spring.ex.mybatis.flex.config;

import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;
import com.mybatisflex.annotation.UpdateListener;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义更新监听
 */
@Slf4j
public class MyUpdateListener implements UpdateListener {
    /**
     * 默认构造
     */
    public MyUpdateListener() {
    }

    @Override
    public void onUpdate(Object o) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            if (o instanceof BaseDomain<?> baseDomain) {
                baseDomain.setUpdateUserId(userId);
            }
        } catch (Exception e) {
            log.debug("saToken get loginId unsuccessful");
        }

    }
}
