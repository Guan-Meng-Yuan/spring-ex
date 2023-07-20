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
import org.dromara.hutool.core.text.StrUtil;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
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
        Map<String, Schema> allSchema = components.getSchemas();
        if (MapUtil.isNotEmpty(allSchema)) {
            allSchema.forEach((key, schema) -> {
                if (StrUtil.startWith(key, "Page") && !StrUtil.equals(key, "PageReq")) {
                    Schema pageSchema = allSchema.get(key);
                    Map<String, Schema> properties = pageSchema.getProperties();
                    //修改集合
                    Schema records = properties.get("records");
                    records.setName("list");
                    records.setDescription("数据集合");
                    properties.put("list", records);
                    //修改pageNumber
                    Schema pageNumber = properties.get("pageNumber");
                    pageNumber.setDescription("当前页码");
                    //修改pageSize
                    Schema pageSize = properties.get("pageSize");
                    pageSize.setDescription("每页条数");
                    //修改totalRow
                    Schema totalRow = properties.get("totalRow");
                    totalRow.setDescription("数据总数");
                    Schema<Boolean> hasNextSchema = new Schema(openApi.getSpecVersion());
                    hasNextSchema.setDescription("是否有下一页");
                    hasNextSchema.setType("boolean");
                    properties.put("hasNext", hasNextSchema);
                    properties.remove("records");
                    properties.remove("totalPage");
                    properties.remove("optimizeCountQuery");
                    properties.remove("empty");
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
        //设置全局响应类
        Schema<Res> resSchema = modelConverters.readAllAsResolvedSchema(new AnnotatedType(Res.class)).schema;
        resSchema.setDescription("全局响应");
        resSchema.setType("object");
        Map<String, Schema> resProperties = resSchema.getProperties();
        Schema dataSchema = resProperties.get("data");
        dataSchema.setType("object");
        components.addSchemas("Res<T>", resSchema);
        //设置PageRes类
        Schema<Res.PageRes> pageResSchema = modelConverters.readAllAsResolvedSchema(new AnnotatedType(Res.PageRes.class)).schema;
        pageResSchema.setDescription("全局分页响应");
        pageResSchema.setType("object");
        Map<String, Schema> pageResProperties = pageResSchema.getProperties();
        Schema listSchema = pageResProperties.get("list");
        listSchema.setType("array");
        components.addSchemas("PageRes<T>", pageResSchema);

    }
}
