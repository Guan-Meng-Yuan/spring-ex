package com.guanmengyuan.spring.ex.web.controller;

import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;
import com.guanmengyuan.spring.ex.common.model.dto.req.PageReq;
import com.guanmengyuan.spring.ex.common.model.dto.res.R;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseController<T extends BaseDomain<T>> {

    private final IService<T> service;

    @GetMapping("all")
    public R<List<T>> all(T param) {
        QueryWrapper queryWrapper = QueryWrapper.create(param);
        return R.ok(service.list(queryWrapper));
    }

    @GetMapping
    public R<Page<T>> page(PageReq pageReq, T param) {
        return page(pageReq, QueryWrapper.create(param));
    }

    public R<Page<T>> page(PageReq pageReq, QueryWrapper queryWrapper) {
        return R.ok(service.page(Page.of(pageReq.getCurrent(), pageReq.getPageSize()), queryWrapper));
    }

    @PostMapping
    public R<Boolean> save(T param) {
        return R.ok(service.saveOrUpdate(param));
    }

    @DeleteMapping("{id}")
    public R<Boolean> remove(@PathVariable Long id) {
        return R.ok(service.removeById(id));
    }

    @GetMapping("{id}")
    public R<T> one(@PathVariable Long id) {
        return R.ok(service.getById(id));
    }
}
