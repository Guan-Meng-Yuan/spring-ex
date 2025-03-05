package com.guanmengyuan.spring.ex.common.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.activerecord.Model;
import com.mybatisflex.core.keygen.KeyGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.util.Date;

/**
 * 通用父类,提供公共字段
 *
 * @param <T> 泛型
 */
@SuppressWarnings({"unchecked", "UnusedReturnValue"})
@MappedSuperclass
@Getter
@EqualsAndHashCode(callSuper = true)
public class BaseDomain<T extends Model<T>> extends Model<T> {
    /**
     * 默认构造
     */
    public BaseDomain() {
    }

    /**
     * 主键id
     */
    @Id
    @com.mybatisflex.annotation.Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @Comment("id")
    private Long id;
    /**
     * 创建时间
     */
    @com.mybatisflex.annotation.Column(onInsertValue = "now()")
    @Comment("创建时间")
    private Date createTime;
    /**
     * 创建人
     */
    @Comment("创建人")
    private Long createUserId;
    /**
     * 更新时间
     */
    @com.mybatisflex.annotation.Column(onUpdateValue = "now()")
    @Comment("更新时间")
    private Date updateTime;
    /**
     * 更新人
     */
    @Comment("更新人")
    private Long updateUserId;
    /**
     * 是否已删除 逻辑删除
     */
    @Column(name = "is_deleted")
    @Comment("逻辑删除")
    @com.mybatisflex.annotation.Column(value = "is_deleted", isLogicDelete = true)
    @JsonIgnore
    private Boolean deleted;
    /**
     * 删除时间
     */
    @Comment("删除时间")
    @JsonIgnore
    private Date deleteTime;
    /**
     * 删除人
     */
    @Comment("删除人")
    @JsonIgnore
    private Long deleteUserId;

    /**
     * 设置主键
     *
     * @param id 主键
     * @return this
     */
    public T setId(Long id) {
        this.id = id;
        return (T) this;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     * @return this
     */
    public T setCreateTime(Date createTime) {
        this.createTime = createTime;
        return (T) this;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     * @return this
     */
    public T setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return (T) this;
    }

    /**
     * 设置是否已删除
     *
     * @param deleted 是否删除
     * @return this
     */
    public T setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return (T) this;
    }

    /**
     * 设置删除时间
     *
     * @param deleteTime 删除时间
     * @return this
     */
    public T setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
        return (T) this;
    }

    /**
     * 设置删除人id
     *
     * @param deleteUserId 删除人id
     * @return this
     */
    public T setDeleteUserId(Long deleteUserId) {
        this.deleteUserId = deleteUserId;
        return (T) this;
    }

    /**
     * 设置创建人id
     *
     * @param createUserId 创建人id
     * @return this
     */
    public T setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
        return (T) this;
    }

    /**
     * 设置更新人id
     *
     * @param updateUserId 更新人id
     * @return this
     */
    public T setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
        return (T) this;
    }

}
