package com.guanmengyuan.spring.ex.web.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Custom serializer for BigDecimal that strips trailing zeros
 * and writes the value as a plain string (no scientific notation).
 */
public class BigDecimalStripTrailingZerosSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        BigDecimal normalized = value.stripTrailingZeros();
        gen.writeString(normalized.toPlainString());
    }
}