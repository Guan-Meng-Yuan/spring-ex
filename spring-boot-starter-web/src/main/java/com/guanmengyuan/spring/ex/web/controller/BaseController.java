package com.guanmengyuan.spring.ex.web.controller;

import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;
import com.guanmengyuan.spring.ex.common.model.dto.req.PageReq;
import com.guanmengyuan.spring.ex.common.model.valid.group.SaveGroup;
import com.guanmengyuan.spring.ex.common.model.valid.group.UpdateGroup;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import jakarta.annotation.Resource;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class BaseController<S extends IService<T>, T extends BaseDomain<T>> {
    @Resource
    private S service;

    /**
     * 分页数据
     *
     * @param page  分页参数
     * @param param 条件参数
     * @return 分页数据
     */
    @GetMapping("page")
    public Page<T> page(@ParameterObject PageReq page, @ParameterObject T param) {
        return service.page(Page.of(page.getCurrent(), page.getPageSize()), param.toQueryWrapper());
    }

    /**
     * 单条数据
     *
     * @param id id
     * @return 数据
     */
    @GetMapping("{id}")
    public T getOneById(@PathVariable String id) {
        return service.getById(id);
    }

    /**
     * 数据列表
     *
     * @param param 条件参数
     * @return 数据列表
     */
    @GetMapping
    public List<T> list(@ParameterObject T param) {
        return service.list(param.toQueryWrapper());
    }

    /**
     * 删除数据
     *
     * @param ids id集合
     * @return 删除结果
     */
    @DeleteMapping("{ids}")
    public Boolean removeByIds(@PathVariable List<String> ids) {
        return service.removeByIds(ids);
    }

    /**
     * 新增数据
     *
     * @param param 数据参数
     * @return 新增结果
     */
    @PostMapping
    public Boolean save(@Validated(SaveGroup.class) @RequestBody T param) {
        return param.save();
    }

    /**
     * 更新数据
     *
     * @param param 数据参数
     * @return 更新结果
     */
    @PutMapping
    public Boolean update(@Validated(UpdateGroup.class) @RequestBody T param) {
        return param.updateById();
    }
}
