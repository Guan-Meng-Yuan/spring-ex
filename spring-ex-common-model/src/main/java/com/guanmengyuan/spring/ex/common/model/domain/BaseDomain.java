package com.guanmengyuan.spring.ex.common.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 顶级父类
 */

@Data
public class BaseDomain implements Serializable {
    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private String id;

    /**
     * 创建时间
     */
    @Column(onInsertValue = "now()")
    private Timestamp createTime;
    /**
     * 更新时间
     */
    @Column(onUpdateValue = "now()")
    private Timestamp updateTime;

    /**
     * 是否已删除 逻辑删除
     */
    @JsonIgnore
    @Column(isLogicDelete = true, value = "is_deleted", onInsertValue = "0")
    private Boolean deleted;
}