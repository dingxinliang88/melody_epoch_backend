package io.github.dingxinliang88.biz;

import lombok.Getter;

/**
 * 状态码
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
public enum StatusCode {
    SUCCESS(0, "Success"),
    CREATED(20100, "已创建"),
    ACCEPTED(20200, "请求已接受，正在处理"),
    DUPLICATE_DATA(41000, "重复数据"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    NOT_LOGIN_ERROR(40500, "未登录"),
    PARAMS_ERROR(40000, "请求参数不合法"),
    FORBIDDEN(40300, "拒绝请求"),
    NO_AUTH_ERROR(40100, "无权访问"),
    BAD_REQUEST(40000, "无效的请求"),
    UNAUTHORIZED(40100, "无权访问"),
    METHOD_NOT_ALLOWED(40500, "不允许使用该方法请求"),
    CONFLICT(40900, "请求冲突"),
    TOO_MANY_REQUEST(42900, "请求频繁"),
    ACCOUNT_ALREADY_EXISTS(43003, "账号已存在"),
    PASSWORD_NOT_MATCH(43000, "两次密码不匹配"),
    CODE_NOT_MATCH(43001, "验证码不匹配"),
    ROLE_NOT_FOUND(43002, "角色不存在"),
    SYSTEM_ERROR(50000, "系统内部错误"),
    OPERATION_ERROR(50100, "操作错误"),
    INTERNAL_SERVER_ERROR(50000, "服务器内部错误"),
    NOT_IMPLEMENTED(50100, "未实现的功能"),
    BAD_GATEWAY(50200, "网关错误"),
    SERVICE_UNAVAILABLE(50300, "服务不可用");

    private final int code;
    private final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
