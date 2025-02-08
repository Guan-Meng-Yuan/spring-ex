package com.guanmengyuan.spring.ex.common.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.activerecord.Model;
import com.mybatisflex.core.keygen.KeyGenerators;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.util.Date;

@SuppressWarnings({"unchecked", "unused", "UnusedReturnValue"})
@MappedSuperclass
@Getter
@EqualsAndHashCode(callSuper = true)
public class BaseDomain<T extends Model<T>> extends Model<T> {
    @Id
    @com.mybatisflex.annotation.Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @Comment("id")
    private Long id;

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

    @com.mybatisflex.annotation.Column(onInsertValue = "now()")
    @Comment("创建时间")
    private Date createTime;
    @com.mybatisflex.annotation.Column(onUpdateValue = "now()")
    @Comment("更新时间")
    private Date updateTime;

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


}
