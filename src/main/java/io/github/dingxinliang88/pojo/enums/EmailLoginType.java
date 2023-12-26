package io.github.dingxinliang88.pojo.enums;

import lombok.Getter;

/**
 * 邮箱登录类型
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Getter
public enum EmailLoginType {

    CODE_LOGIN(0, "验证码登录"),
    PWD_LOGIN(1, "密码登录");

    final Integer code;
    final String desc;

    EmailLoginType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
