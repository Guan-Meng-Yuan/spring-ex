package com.guanmengyuan.spring.ex.common.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.activerecord.Model;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 顶级父类
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseDomain<T extends Model<T>> extends Model<T> {
    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private String id;

    /**
     * 创建时间
     */
    @Column(onInsertValue = "now()")
    private Date createTime;
    /**
     * 更新时间
     */
    @Column(onUpdateValue = "now()")
    private Date updateTime;

    /**
     * 是否已删除 逻辑删除
     */
    @JsonIgnore
    @Column(isLogicDelete = true, value = "is_deleted", onInsertValue = "0")
    private Boolean deleted;
}
