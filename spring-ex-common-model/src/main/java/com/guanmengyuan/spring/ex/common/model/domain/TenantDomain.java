package com.guanmengyuan.spring.ex.common.model.domain;

import org.hibernate.annotations.Comment;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.core.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 租户下的baseDomain
 *
 * @param <T> 继承的子类类型
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@SuppressWarnings({ "unchecked", "UnusedReturnValue", "unused" })
public class TenantDomain<T extends Model<T>> extends BaseDomain<T> {
    /**
     * 默认构造
     */
    public TenantDomain() {
    }

    /**
     * 租户ID
     */
    @Column(tenantId = true)
    @Comment("租户ID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String tenantId;

    /**
     * 设置租户ID
     * 
     * @param tenantId 租户id
     * @return this
     */
    public T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }
}
