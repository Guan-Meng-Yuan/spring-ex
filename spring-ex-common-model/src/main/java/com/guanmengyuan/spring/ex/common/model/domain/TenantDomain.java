package com.guanmengyuan.spring.ex.common.model.domain;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.core.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户下的baseDomain
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantDomain<T extends Model<T>> extends BaseDomain<T> {
    /**
     * 租户ID
     */
    @Column(tenantId = true)
    private String tenantId;
}
