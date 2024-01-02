package io.github.dingxinliang88.biz;

import lombok.Getter;

/**
 * 状态码
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
public enum StatusCode {
    SUCCESS(0, "Success"),
    DUPLICATE_DATA(41000, "重复数据"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    NOT_LOGIN_ERROR(40500, "未登录"),
    PARAMS_ERROR(40000, "请求参数不合法"),
    NO_AUTH_ERROR(40100, "无权访问"),
    BAD_REQUEST(40000, "无效的请求"),
    TOO_MANY_REQUEST(42900, "请求频繁"),
    ACCOUNT_ALREADY_EXISTS(43003, "账号已存在"),
    PASSWORD_NOT_MATCH(43000, "两次密码不匹配"),
    CODE_NOT_MATCH(43001, "验证码不匹配"),
    SYSTEM_ERROR(50000, "系统内部错误"),
    ;

    private final int code;
    private final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
