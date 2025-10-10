package com.guanmengyuan.spring.ex.common.model.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.NonNull;

import com.guanmengyuan.spring.ex.common.model.enums.ParamEnum;
import com.guanmengyuan.spring.ex.common.model.enums.ResEnum;
import com.guanmengyuan.spring.ex.common.model.exception.ServiceException;

// no lombok needed

/**
 * 接收任意枚举参数的转换
 */
public class AnyToEnumConverterFactory implements ConverterFactory<String, ParamEnum<?>> {
    /**
     * 默认构造
     */
    public AnyToEnumConverterFactory() {
    }

    @Override
    @NonNull
    public <T extends ParamEnum<?>> Converter<String, T> getConverter(@NonNull Class<T> targetType) {
        if (!targetType.isEnum()) {
            throw new ServiceException(ResEnum.ENUM_NOT_SUPPORT);
        }
        @SuppressWarnings("unchecked")
        Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) targetType;
        return new StringToParamEnumConverter<>(targetType, enumClass);
    }

    private static final class StringToParamEnumConverter<T extends ParamEnum> implements Converter<String, T> {
        private final Class<T> targetType;
        private final Class<? extends Enum<?>> enumType;

        private StringToParamEnumConverter(Class<T> targetType, Class<? extends Enum<?>> enumType) {
            this.targetType = targetType;
            this.enumType = enumType;
        }

        @Override
        public T convert(@NonNull String source) {
            if (source.isEmpty()) {
                return null;
            }
            Object resolved = ParamEnum.ofString((Class) enumType, source.trim());
            return targetType.cast(resolved);
        }
    }
}
