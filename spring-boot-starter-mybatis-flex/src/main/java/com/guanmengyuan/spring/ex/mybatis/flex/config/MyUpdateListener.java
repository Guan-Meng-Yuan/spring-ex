package com.guanmengyuan.spring.ex.mybatis.flex.config;

import cn.dev33.satoken.stp.StpUtil;
import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;
import com.mybatisflex.annotation.UpdateListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyUpdateListener implements UpdateListener {
    @Override
    public void onUpdate(Object o) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            if (o instanceof BaseDomain<?> baseDomain) {
                baseDomain.setUpdateUserId(userId);
            }
        } catch (Exception e) {
            log.warn("saToken get loginId unsuccessful");
        }

    }
}
