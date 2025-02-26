package com.guanmengyuan.spring.ex.common.model.domain;

import java.util.Date;

import org.hibernate.annotations.Comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.activerecord.Model;
import com.mybatisflex.core.keygen.KeyGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@SuppressWarnings({ "unchecked", "UnusedReturnValue" })
@MappedSuperclass
@Getter
@EqualsAndHashCode(callSuper = true)
public class BaseDomain<T extends Model<T>> extends Model<T> {
    @Id
    @com.mybatisflex.annotation.Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @Comment("id")
    private Long id;
    @com.mybatisflex.annotation.Column(onInsertValue = "now()")
    @Comment("创建时间")
    private Date createTime;

    @Comment("创建人")
    private Long createUserId;

    @com.mybatisflex.annotation.Column(onUpdateValue = "now()")
    @Comment("更新时间")
    private Date updateTime;
    @Comment("更新人")
    private Long updateUserId;
    @Column(name = "is_deleted")
    @Comment("逻辑删除")
    @com.mybatisflex.annotation.Column(value = "is_deleted", isLogicDelete = true)
    @JsonIgnore
    private Boolean deleted;
    @Comment("删除时间")
    @JsonIgnore
    private Date deleteTime;
    @Comment("删除人")
    @JsonIgnore
    private Long deleteUserId;

    public T setId(Long id) {
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

    public T setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
        return (T) this;
    }

    public T setDeleteUserId(Long deleteUserId) {
        this.deleteUserId = deleteUserId;
        return (T) this;
    }

    public T setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
        return (T) this;
    }

    public T setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
        return (T) this;
    }

}
