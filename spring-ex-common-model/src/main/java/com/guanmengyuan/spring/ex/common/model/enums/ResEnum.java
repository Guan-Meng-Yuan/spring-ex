package com.guanmengyuan.spring.ex.common.model.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应枚举
 * 
 * 响应码设计规范：
 * 1. 成功响应：0
 * 2. 客户端错误：1xxxx (参数错误、认证错误、权限错误等)
 * 3. 服务端错误：2xxxx (业务逻辑错误、系统错误等)
 * 4. 第三方服务错误：3xxxx (外部API调用错误等)
 * 
 * 具体分类：
 * - 10001-10099: 参数验证错误
 * - 10100-10199: 认证相关错误
 * - 10200-10299: 权限相关错误
 * - 10300-10399: 资源相关错误
 * - 20001-20099: 业务逻辑错误
 * - 20100-20199: 数据操作错误
 * - 20200-20299: 系统配置错误
 * - 20300-20399: 系统异常错误
 * - 30001-30099: 第三方服务错误
 */
@AllArgsConstructor
@Getter
public enum ResEnum implements BaseResEnum {
    
    // ==================== 成功响应 ====================
    /**
     * 操作成功
     */
    SUCCESS(HttpStatus.OK, "ok", "操作成功", "0"),
    
    // ==================== 客户端错误 (1xxxx) ====================
    
    // 参数验证错误 (10001-10099)
    /**
     * 参数不能为空
     */
    PARAM_EMPTY(HttpStatus.BAD_REQUEST, "parameter is empty", "参数不能为空", "10001"),
    /**
     * 参数格式错误
     */
    PARAM_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "parameter format error", "参数格式错误", "10002"),
    /**
     * 参数类型错误
     */
    PARAM_TYPE_ERROR(HttpStatus.BAD_REQUEST, "parameter type error", "参数类型错误", "10003"),
    /**
     * 参数长度超限
     */
    PARAM_LENGTH_ERROR(HttpStatus.BAD_REQUEST, "parameter length error", "参数长度超限", "10004"),
    /**
     * 参数值无效
     */
    PARAM_VALUE_INVALID(HttpStatus.BAD_REQUEST, "parameter value invalid", "参数值无效", "10005"),
    /**
     * ID不能为空
     */
    ID_EMPTY(HttpStatus.BAD_REQUEST, "ID cannot be empty", "ID不能为空", "10006"),
    /**
     * 分页参数错误
     */
    PAGE_PARAM_ERROR(HttpStatus.BAD_REQUEST, "page parameter error", "分页参数错误", "10007"),
    /**
     * 枚举参数不支持
     */
    ENUM_NOT_SUPPORT(HttpStatus.BAD_REQUEST, "enum not support", "枚举参数不支持", "10008"),
    
    // 认证相关错误 (10100-10199)
    /**
     * 未登录
     */
    NOT_LOGIN(HttpStatus.UNAUTHORIZED, "not login", "未登录", "10100"),
    /**
     * Token失效
     */
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "token expired", "登录已过期", "10101"),
    /**
     * Token无效
     */
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "token invalid", "Token无效", "10102"),
    /**
     * Token缺失
     */
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "token missing", "Token缺失", "10103"),
    /**
     * 用户名或密码错误
     */
    USERNAME_PASSWORD_ERROR(HttpStatus.UNAUTHORIZED, "username or password error", "用户名或密码错误", "10104"),
    /**
     * 验证码错误
     */
    CAPTCHA_ERROR(HttpStatus.UNAUTHORIZED, "captcha error", "验证码错误", "10105"),
    /**
     * 账号被锁定
     */
    ACCOUNT_LOCKED(HttpStatus.UNAUTHORIZED, "account locked", "账号被锁定", "10106"),
    /**
     * 账号被禁用
     */
    ACCOUNT_DISABLED(HttpStatus.UNAUTHORIZED, "account disabled", "账号被禁用", "10107"),
    
    // 权限相关错误 (10200-10299)
    /**
     * 无权限访问
     */
    NO_PERMISSION(HttpStatus.FORBIDDEN, "no permission", "暂无权限", "10200"),
    /**
     * 权限不足
     */
    INSUFFICIENT_PERMISSION(HttpStatus.FORBIDDEN, "insufficient permission", "权限不足", "10201"),
    /**
     * 角色不存在
     */
    ROLE_NOT_EXIST(HttpStatus.FORBIDDEN, "role not exist", "角色不存在", "10202"),
    /**
     * 操作被拒绝
     */
    OPERATION_DENIED(HttpStatus.FORBIDDEN, "operation denied", "操作被拒绝", "10203"),
    
    // 资源相关错误 (10300-10399)
    /**
     * 资源不存在
     */
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "resource not found", "资源不存在", "10300"),
    /**
     * 资源已被删除
     */
    RESOURCE_DELETED(HttpStatus.NOT_FOUND, "resource deleted", "资源已被删除", "10301"),
    /**
     * 资源已被占用
     */
    RESOURCE_OCCUPIED(HttpStatus.CONFLICT, "resource occupied", "资源已被占用", "10302"),
    /**
     * 资源状态错误
     */
    RESOURCE_STATUS_ERROR(HttpStatus.BAD_REQUEST, "resource status error", "资源状态错误", "10303"),
    /**
     * 文件不存在
     */
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "file not found", "文件不存在", "10304"),
    /**
     * 文件上传失败
     */
    FILE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "file upload error", "文件上传失败", "10305"),
    /**
     * 文件格式不支持
     */
    FILE_FORMAT_NOT_SUPPORT(HttpStatus.BAD_REQUEST, "file format not support", "文件格式不支持", "10306"),
    /**
     * 文件大小超限
     */
    FILE_SIZE_EXCEED(HttpStatus.BAD_REQUEST, "file size exceed", "文件大小超限", "10307"),
    
    // ==================== 服务端错误 (2xxxx) ====================
    
    // 业务逻辑错误 (20001-20099)
    /**
     * 业务操作失败
     */
    BUSINESS_ERROR(HttpStatus.BAD_REQUEST, "business error", "业务操作失败", "20001"),
    /**
     * 数据重复
     */
    DATA_DUPLICATE(HttpStatus.CONFLICT, "data duplicate", "数据重复", "20002"),
    /**
     * 数据状态错误
     */
    DATA_STATUS_ERROR(HttpStatus.BAD_REQUEST, "data status error", "数据状态错误", "20003"),
    /**
     * 数据关联错误
     */
    DATA_RELATION_ERROR(HttpStatus.BAD_REQUEST, "data relation error", "数据关联错误", "20004"),
    /**
     * 操作频率过高
     */
    OPERATION_FREQUENT(HttpStatus.TOO_MANY_REQUESTS, "operation too frequent", "操作频率过高", "20005"),
    /**
     * 操作超时
     */
    OPERATION_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "operation timeout", "操作超时", "20006"),
    /**
     * 业务规则冲突
     */
    BUSINESS_RULE_CONFLICT(HttpStatus.CONFLICT, "business rule conflict", "业务规则冲突", "20007"),
    
    // 数据操作错误 (20100-20199)
    /**
     * 数据库连接失败
     */
    DB_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "database connection error", "数据库连接失败", "20100"),
    /**
     * 数据库操作失败
     */
    DB_OPERATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "database operation error", "数据库操作失败", "20101"),
    /**
     * 数据查询失败
     */
    DB_QUERY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "database query error", "数据查询失败", "20102"),
    /**
     * 数据插入失败
     */
    DB_INSERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "database insert error", "数据插入失败", "20103"),
    /**
     * 数据更新失败
     */
    DB_UPDATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "database update error", "数据更新失败", "20104"),
    /**
     * 数据删除失败
     */
    DB_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "database delete error", "数据删除失败", "20105"),
    /**
     * 数据事务失败
     */
    DB_TRANSACTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "database transaction error", "数据事务失败", "20106"),
    
    // 系统配置错误 (20200-20299)
    /**
     * 系统配置错误
     */
    CONFIG_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "system config error", "系统配置错误", "20200"),
    /**
     * 缓存配置错误
     */
    CACHE_CONFIG_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "cache config error", "缓存配置错误", "20201"),
    /**
     * 消息队列配置错误
     */
    MQ_CONFIG_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "message queue config error", "消息队列配置错误", "20202"),
    
    // 系统异常错误 (20300-20399)
    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal server error", "系统内部错误", "20300"),
    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "service unavailable", "服务不可用", "20301"),
    /**
     * 系统维护中
     */
    SYSTEM_MAINTENANCE(HttpStatus.SERVICE_UNAVAILABLE, "system maintenance", "系统维护中", "20302"),
    /**
     * 内存不足
     */
    OUT_OF_MEMORY(HttpStatus.INTERNAL_SERVER_ERROR, "out of memory", "内存不足", "20303"),
    /**
     * 磁盘空间不足
     */
    DISK_SPACE_FULL(HttpStatus.INTERNAL_SERVER_ERROR, "disk space full", "磁盘空间不足", "20304"),
    
    // ==================== 第三方服务错误 (3xxxx) ====================
    
    // 第三方API错误 (30001-30099)
    /**
     * 第三方服务调用失败
     */
    THIRD_PARTY_API_ERROR(HttpStatus.BAD_GATEWAY, "third party api error", "第三方服务调用失败", "30001"),
    /**
     * 第三方服务超时
     */
    THIRD_PARTY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "third party timeout", "第三方服务超时", "30002"),
    /**
     * 第三方服务不可用
     */
    THIRD_PARTY_UNAVAILABLE(HttpStatus.BAD_GATEWAY, "third party unavailable", "第三方服务不可用", "30003"),
    /**
     * 第三方服务认证失败
     */
    THIRD_PARTY_AUTH_ERROR(HttpStatus.UNAUTHORIZED, "third party auth error", "第三方服务认证失败", "30004"),
    /**
     * 第三方服务限流
     */
    THIRD_PARTY_RATE_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "third party rate limit", "第三方服务限流", "30005"),
    
    // 支付相关错误 (30100-30199)
    /**
     * 支付失败
     */
    PAYMENT_FAILED(HttpStatus.BAD_REQUEST, "payment failed", "支付失败", "30100"),
    /**
     * 支付超时
     */
    PAYMENT_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "payment timeout", "支付超时", "30101"),
    /**
     * 支付金额错误
     */
    PAYMENT_AMOUNT_ERROR(HttpStatus.BAD_REQUEST, "payment amount error", "支付金额错误", "30102"),
    /**
     * 支付渠道错误
     */
    PAYMENT_CHANNEL_ERROR(HttpStatus.BAD_REQUEST, "payment channel error", "支付渠道错误", "30103"),
    
    // 短信/邮件服务错误 (30200-30299)
    /**
     * 短信发送失败
     */
    SMS_SEND_ERROR(HttpStatus.BAD_REQUEST, "sms send error", "短信发送失败", "30200"),
    /**
     * 邮件发送失败
     */
    EMAIL_SEND_ERROR(HttpStatus.BAD_REQUEST, "email send error", "邮件发送失败", "30201"),
    /**
     * 验证码发送失败
     */
    VERIFY_CODE_SEND_ERROR(HttpStatus.BAD_REQUEST, "verify code send error", "验证码发送失败", "30202"),
    
    // 文件存储服务错误 (30300-30399)
    /**
     * 文件上传服务错误
     */
    FILE_STORAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "file storage error", "文件存储服务错误", "30300"),
    /**
     * 文件下载服务错误
     */
    FILE_DOWNLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "file download error", "文件下载服务错误", "30301"),
    /**
     * 文件删除服务错误
     */
    FILE_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "file delete error", "文件删除服务错误", "30302"),
    ;

    private final HttpStatusCode httpStatusCode;
    private final String message;
    private final String tips;
    private final String code;
}
