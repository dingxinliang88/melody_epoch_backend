package io.github.dingxinliang88.pojo.enums;

import lombok.Getter;

/**
 * 用户角色类型
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Getter
public enum UserRoleType {

    /**
     * 001 -> member; => 101 -> baned member;
     * 010 -> fan; => 110 -> baned fan;
     * 000 -> admin;
     */
    ADMIN(0b000, "admin"),
    MEMBER(0b001, "member"),
    FAN(0b010, "fan"),
    BANNED(0b100, "banned");

    final Integer type;

    final String desc;

    UserRoleType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static UserRoleType getByDesc(String desc) {
        for (UserRoleType roleType : UserRoleType.values()) {
            if (roleType.getDesc().equals(desc)) {
                return roleType;
            }
        }
        return BANNED;
    }
}
