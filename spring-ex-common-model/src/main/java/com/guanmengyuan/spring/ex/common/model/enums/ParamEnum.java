package com.guanmengyuan.spring.ex.common.model.enums;

/**
 * 参数枚举接口
 * 
 * @param <K> 键值类型
 */
public interface ParamEnum<K> {
    /**
     * 获取用于匹配的键值
     *
     * @return 键值
     */
    K getValue();

    /**
     * 通用静态工厂方法：根据键值获取对应的枚举常量
     *
     * @param enumClass 目标枚举类
     * @param value     键值
     * @return 匹配到的枚举常量
     * @param <K> 键值类型
     * @param <E> 枚举类型，需实现 ParamEnum
     * @throws IllegalArgumentException 当未找到匹配项时抛出
     */
    static <K, E extends Enum<E> & ParamEnum<K>> E of(Class<E> enumClass, K value) {
        if (value == null) {
            return null;
        }
        E[] constants = enumClass.getEnumConstants();
        if (constants == null) {
            throw new IllegalArgumentException(enumClass.getName() + " is not an enum type");
        }
        for (E constant : constants) {
            if (java.util.Objects.equals(constant.getValue(), value)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value + " for enum " + enumClass.getSimpleName());
    }

    /**
     * 将字符串转换为目标枚举（支持常见基础类型键：String、Integer、Long、Short、Byte、Boolean；
     * 若无法识别键类型，则回退按常量名匹配，忽略大小写）
     *
     * @param enumClass 目标枚举类
     * @param source    文本值
     * @return 匹配到的枚举常量
     * @param <E> 枚举类型，实现 {@code ParamEnum<?>}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    static <E extends Enum<E> & ParamEnum<?>> E ofString(Class<E> enumClass, String source) {
        if (source == null) {
            return null;
        }
        String text = source.trim();
        if (text.isEmpty()) {
            return null;
        }
        E[] constants = enumClass.getEnumConstants();
        if (constants == null || constants.length == 0) {
            throw new IllegalArgumentException(enumClass.getName() + " has no enum constants");
        }
        Object sample = constants[0].getValue();
        if (sample == null) {
            // 无法确定键类型，直接按名称匹配
            for (E e : constants) {
                if (e.name().equalsIgnoreCase(text)) {
                    return e;
                }
            }
            throw new IllegalArgumentException("Unknown name: " + text + " for enum " + enumClass.getSimpleName());
        }
        Object parsed;
        if (sample instanceof String) {
            parsed = text;
        } else if (sample instanceof Integer) {
            parsed = Integer.valueOf(text);
        } else if (sample instanceof Long) {
            parsed = Long.valueOf(text);
        } else if (sample instanceof Short) {
            parsed = Short.valueOf(text);
        } else if (sample instanceof Byte) {
            parsed = Byte.valueOf(text);
        } else if (sample instanceof Boolean) {
            parsed = Boolean.valueOf(text);
        } else {
            // 回退：按常量名匹配
            for (E e : constants) {
                if (e.name().equalsIgnoreCase(text)) {
                    return e;
                }
            }
            throw new IllegalArgumentException(
                    "Unsupported key type (" + sample.getClass().getSimpleName() + ") for enum "
                            + enumClass.getSimpleName());
        }
        return (E) of((Class) enumClass, parsed);
    }
}
