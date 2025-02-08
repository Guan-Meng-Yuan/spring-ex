package com.guanmengyuan.spring.ex.common.model.domain;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.core.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 租户下的baseDomain
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TenantDomain<T extends Model<T>> extends BaseDomain<T> {
    /**
     * 租户ID
     */
    @Column(tenantId = true)
    @Comment("租户ID")
    private String tenantId;
}
