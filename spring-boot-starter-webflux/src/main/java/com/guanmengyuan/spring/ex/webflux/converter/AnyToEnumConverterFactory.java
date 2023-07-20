package com.guanmengyuan.spring.ex.webflux.converter;

import com.guanmengyuan.spring.ex.common.model.dto.res.Res;
import com.guanmengyuan.spring.ex.common.model.enums.ParamEnum;
import com.guanmengyuan.spring.ex.common.model.enums.ResEnum;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class AnyToEnumConverterFactory implements ConverterFactory<String, ParamEnum> {


    @Override
    @NonNull
    public <T extends ParamEnum> Converter<String, T> getConverter(@NonNull Class<T> targetType) {
        return new AnyToEnum<>(targetType);
    }


    private record AnyToEnum<T extends ParamEnum>(Class<T> enumType) implements Converter<String, T> {
        @SuppressWarnings("unchecked")
        @Override
        @SneakyThrows
        public T convert(String source) {
            if (source.isEmpty()) {
                return null;
            }
            if (!enumType.isEnum()) {
                Res.cast(ResEnum.NOT_AN_ENUMERATION);
            }
            return (T) enumType.getEnumConstants()[0].getInstanceByType(source.trim());
        }
    }
}
