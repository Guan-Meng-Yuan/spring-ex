package com.guanmengyuan.spring.ex.openapi.config;


import com.guanmengyuan.spring.ex.common.model.dto.res.Res;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.providers.JavadocProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(OpenApiConfigProperties.class)
@RequiredArgsConstructor
public class OpenApiConfiguration implements GlobalOpenApiCustomizer {
    private final OpenApiConfigProperties openApiConfigProperties;
    private final JavadocProvider javadocProvider;
    private static final String PAGE_REGEX = "^Page[A-Z].*";

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
    @SuppressWarnings({"rawtypes", "unchecked"})
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

        //设置全局响应类
        Schema<Res> resSchema = modelConverters.readAllAsResolvedSchema(new AnnotatedType(Res.class)).schema;
        String resClassDoc = javadocProvider.getClassJavadoc(Res.class);
        resSchema.setDescription(resClassDoc);
        resSchema.setType("object");
        resSchema.set$id("res");
        Map<String, Schema> resProperties = resSchema.getProperties();
        Schema dataSchema = resProperties.get("data");
        dataSchema.setType("object");
        components.addSchemas("Res<T>", resSchema);

        Schema<Res.PageRes> pageRes = modelConverters.readAllAsResolvedSchema(new AnnotatedType(Res.PageRes.class)).schema;
        pageRes.set$id("pageRes");
        components.addSchemas("PageRes<T>", pageRes);

        Map<String, Schema> allSchema = components.getSchemas();
        if (MapUtil.isNotEmpty(allSchema)) {
            allSchema.forEach((key, schema) -> {
                if (ReUtil.isMatch(PAGE_REGEX, key) && !StrUtil.equalsAny(key, "PageRes<T>", "PageReq")) {
                    Schema pageSchema = allSchema.get(key);
                    Map<String, Schema> properties = pageSchema.getProperties();
                    Schema records = properties.get("records");
                    records.setDescription("分页数据");
                    properties.put("list", records);
                    Schema pageNumber = properties.get("pageNumber");
                    pageNumber.setDescription("当前页码");
                    properties.put("current", pageNumber);
                    properties.get("pageSize").setDescription("每页数据量");
                    Schema totalRow = properties.get("totalRow");
                    totalRow.setDescription("数据总量");
                    properties.put("total", totalRow);
                    Schema<Boolean> hasNextSchema = new Schema<>();
                    hasNextSchema.setType("boolean");
                    hasNextSchema.setDescription("是否有下一页");
                    properties.put("hasNext", hasNextSchema);
                    MapUtil.removeAny(properties, "pageNumber", "totalPage", "totalRow", "optimizeCountQuery", "empty", "records");
                }
            });
        }

        Map<String, String> finalTagMap = tagMap;
        paths.forEach((s, pathItem) -> {
            List<Operation> operations = pathItem.readOperations();
            operations.forEach(operation -> {
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
