package com.guanmengyuan.spring.ex.common.model.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.NonNull;

import com.guanmengyuan.spring.ex.common.model.enums.ParamEnum;
import com.guanmengyuan.spring.ex.common.model.enums.ResEnum;
import com.guanmengyuan.spring.ex.common.model.exception.ServiceException;

import lombok.SneakyThrows;

/**
 * 接收任意枚举参数的转换
 */
public class AnyToEnumConverterFactory implements ConverterFactory<String, ParamEnum> {
    /**
     * 默认构造
     */
    public AnyToEnumConverterFactory() {
    }

    @Override
    @NonNull
    public <T extends ParamEnum> Converter<String, T> getConverter(@NonNull Class<T> targetType) {
        return new AnyToEnum<>(targetType);
    }

    private record AnyToEnum<T extends ParamEnum>(Class<T> enumType) implements Converter<String, T> {
        @SuppressWarnings("unchecked")
        @Override
        @SneakyThrows
        public T convert(@NonNull String source) {
            if (source.isEmpty()) {
                return null;
            }
            if (!enumType.isEnum()) {
                throw new ServiceException(ResEnum.ENUM_NOT_SUPPORT);
            }
            return (T) enumType.getEnumConstants()[0].getInstanceBy(source.trim());
        }
    }
}
