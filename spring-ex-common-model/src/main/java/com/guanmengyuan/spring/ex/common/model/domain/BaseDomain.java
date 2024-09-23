package com.guanmengyuan.spring.ex.common.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guanmengyuan.spring.ex.common.model.valid.group.UpdateGroup;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.activerecord.Model;
import com.mybatisflex.core.keygen.KeyGenerators;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 顶级父类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuppressWarnings("unchecked")
public class BaseDomain<T extends Model<T>> extends Model<T> {
    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @NotNull(groups = UpdateGroup.class, message = "ID不能为空")
    private String id;
    /**
     * 创建时间
     */
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
    @Column(isLogicDelete = true, value = "is_deleted")
    private Boolean deleted;

    public T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public T setCreateTime(Date createTime) {
        this.createTime = createTime;
        return (T) this;
    }

    public T setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return (T) this;
    }

    public T setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return (T) this;
    }

}
