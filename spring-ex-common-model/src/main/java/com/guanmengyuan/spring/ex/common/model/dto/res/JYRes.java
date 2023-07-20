package com.guanmengyuan.spring.ex.common.model.dto.res;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 聚英统一响应
 * 
 * @author guanmengyuan
 */
@Data
public class JYRes<T> implements Serializable {
    private Boolean success;
    private Integer code;
    private String message;
    private T data;
    private Object extras;
    private Timestamp timestamp;
}
