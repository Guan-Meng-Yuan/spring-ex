package com.guanmengyuan.spring.ex.webflux.controller;


import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;
import com.guanmengyuan.spring.ex.common.model.dto.res.Res;
import com.guanmengyuan.spring.ex.common.model.enums.ResEnum;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.core.table.TableDefs;
import com.mybatisflex.core.table.TableInfoFactory;
import jakarta.annotation.Resource;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class BaseController<T extends BaseDomain<T>> {
    @Resource
    private IService<T> iService;

    /**
     * 集合
     *
     * @param param 条件参数
     * @return 集合
     */
    @GetMapping
    public List<T> list(@ParameterObject T param) {
        QueryColumn createTime = TableDefs.getQueryColumn(param.getClass(), TableInfoFactory.ofEntityClass(param.getClass()).getTableNameWithSchema(), "create_time");
        return param.orderBy(createTime.desc()).list();
    }

    /**
     * 获取详情
     *
     * @param id 主键id
     * @return 详情
     */
    @GetMapping("{id}")
    public T getOne(@PathVariable String id) {
        return iService.getById(id);
    }

    /**
     * 分页数据
     *
     * @param page  分页参数
     * @param param 条件参数
     * @return 分页数据
     */
    @GetMapping("page")
    //没有用这些注解,不是引入kenif了吗 只用了他的ui 没有它的增强功能 那还是用的swagger? springdoc

    public Page<T> page(@ParameterObject Page<T> page, @ParameterObject() T param) {
        QueryColumn createTime = TableDefs.getQueryColumn(param.getClass(), TableInfoFactory.ofEntityClass(param.getClass()).getTableNameWithSchema(), "create_time");
        return param.orderBy(createTime.desc()).page(page);
    }

    /**
     * 新增数据
     *
     * @param body 新增参数
     * @return 新增结果
     */
    @PostMapping
    public Boolean save(@RequestBody T body) {
        if (BeanUtil.isEmpty(body)) {
            Res.cast(ResEnum.BODY_CAN_NOT_BE_EMPTY);
        }
        return body.save();
    }

    /**
     * 修改数据
     *
     * @param body 修改参数
     * @return 修改结果
     */
    @PutMapping
    public Boolean updateById(@RequestBody T body) {
        if (BeanUtil.isEmpty(body)) {
            Res.cast(ResEnum.BODY_CAN_NOT_BE_EMPTY);
        }
        if (StrUtil.isEmpty(body.getId())) {
            Res.cast(ResEnum.ID_CAN_NOT_BE_EMPTY);
        }

        return body.updateById();
    }

    /**
     * 删除数据 支持批量删除
     *
     * @param ids ID集合 一个或多个
     * @return 删除结果
     */
    @DeleteMapping("{ids}")
    public Boolean removeByIds(@PathVariable List<String> ids) {
        return iService.removeByIds(ids);
    }
}