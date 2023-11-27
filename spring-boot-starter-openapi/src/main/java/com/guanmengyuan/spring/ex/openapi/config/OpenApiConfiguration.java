package com.guanmengyuan.spring.ex.openapi.config;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.guanmengyuan.spring.ex.common.model.dto.res.Res;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 接口文档自动配置类
 */
@Configuration
@EnableConfigurationProperties(OpenApiConfigProperties.class)
@RequiredArgsConstructor
public class OpenApiConfiguration implements GlobalOpenApiCustomizer {
    private final OpenApiConfigProperties openApiConfigProperties;

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        Info info = new Info();
        info.setVersion(openApiConfigProperties.getVersion());
        info.setDescription(openApiConfigProperties.getDescription());
        info.setTitle(openApiConfigProperties.getTitle());
        Contact contact = new Contact();
        contact.setName(openApiConfigProperties.getName());
        contact.setEmail(openApiConfigProperties.getEmail());
        info.setContact(contact);
        openAPI.setInfo(info);
        return openAPI;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void customise(OpenAPI openApi) {
        ModelConverters modelConverters = ModelConverters.getInstance();
        Components components = openApi.getComponents();
        Paths paths = openApi.getPaths();
        List<Tag> tags = openApi.getTags();
        Map<String, String> tagMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(tags)) {
            tagMap = CollUtil.fieldValueAsMap(tags, "name", "description");
            tags.forEach(tag -> tag.setName(tag.getDescription()));
        }

        // 设置全局响应类

        Schema resSchema = modelConverters.readAllAsResolvedSchema(new AnnotatedType(Res.class)).schema;
        resSchema.setDescription("全局响应");
        resSchema.setType("object");
        resSchema.set$id("res");
        Map<String, Schema> resProperties = resSchema.getProperties();
        Schema dataSchema = resProperties.get("data");
        dataSchema.setType("object");
        components.addSchemas("Res<T>", resSchema);

        Map<String, String> finalTagMap = tagMap;
        paths.forEach((s, pathItem) -> {
            List<Operation> operations = pathItem.readOperations();
            Predicate<Parameter> nameStartWith = parameter -> StrUtil.startWithAny(parameter.getName(), "queryWrapper",
                    "totalRow", "records", "totalPage");
            operations.forEach(operation -> {
                List<Parameter> operationParameters = operation.getParameters();
                if (CollUtil.isNotEmpty(operationParameters)) {
                    CollUtil.removeWithAddIf(operationParameters, nameStartWith);
                }
                List<String> operationTags = operation.getTags();
                if (CollUtil.size(operationTags) == 1) {
                    String tag = operationTags.get(0);
                    if (finalTagMap.containsKey(tag)) {
                        operation.setTags(ListUtil.of(finalTagMap.get(tag)));
                    }
                }
            });
        });

    }
}
