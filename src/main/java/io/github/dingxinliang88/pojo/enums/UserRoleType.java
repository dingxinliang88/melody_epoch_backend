package io.github.dingxinliang88.pojo.enums;

import lombok.Getter;

/**
 * 用户角色类型
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
public enum UserRoleType {

    // 0 - guest, 1 - admin , 2 - member, 3 - fan
    GUEST(0, "guest"),
    ADMIN(1, "admin"),
    MEMBER(2, "member"),
    FAN(3, "fan"),
    ;

    final Integer type;

    final String desc;

    UserRoleType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
