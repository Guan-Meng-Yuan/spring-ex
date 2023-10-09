package com.guanmengyuan.spring.ex.common.model.enums;

/**
 * 参数枚举接口
 */
public interface ParamEnum {
    /**
     * 获取枚举实例
     *
     * @param type 类型字段值
     * @return 枚举实例
     */
    ParamEnum getInstanceByType(String type);
}
