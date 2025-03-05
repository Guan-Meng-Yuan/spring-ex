package com.guanmengyuan.spring.ex.common.model.domain;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.core.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

/**
 * 租户下的baseDomain
 *
 * @param <T> 继承的子类类型
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
    private String tenantId;
}
