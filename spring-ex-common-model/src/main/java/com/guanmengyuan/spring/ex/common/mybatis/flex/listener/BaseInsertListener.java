package com.guanmengyuan.spring.ex.common.mybatis.flex.listener;

import cn.hutool.core.date.DateUtil;
import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;
import com.mybatisflex.annotation.InsertListener;

public class BaseInsertListener implements InsertListener {
    @SuppressWarnings("rawtypes")
    @Override
    public void onInsert(Object entity) {
        BaseDomain baseDomain = (BaseDomain) entity;
        if (null == baseDomain.getCreateTime()) {
            baseDomain.setCreateTime(DateUtil.date());
        }
    }
}
