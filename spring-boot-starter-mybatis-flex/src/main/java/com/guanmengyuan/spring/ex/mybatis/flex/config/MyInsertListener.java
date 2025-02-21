package com.guanmengyuan.spring.ex.mybatis.flex.config;

import cn.dev33.satoken.stp.StpUtil;
import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;
import com.mybatisflex.annotation.InsertListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyInsertListener implements InsertListener {
    @Override
    public void onInsert(Object o) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            if (o instanceof BaseDomain<?> baseDomain) {
                baseDomain.setCreateUserId(userId);
            }
        } catch (Exception e) {
            log.warn("saToken get loginId unsuccessful");
        }
    }
}
