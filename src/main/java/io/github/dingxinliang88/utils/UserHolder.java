package io.github.dingxinliang88.utils;

import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;

/**
 * 用户信息管理
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
public class UserHolder {

    private static final ThreadLocal<UserLoginVO> USER_LOGIN_STATE = new ThreadLocal<>();

    public static UserLoginVO getUser() {
        return USER_LOGIN_STATE.get();
    }

    public static void setUser(UserLoginVO user) {
        USER_LOGIN_STATE.set(user);
    }

    public static void removeUser() {
        USER_LOGIN_STATE.remove();
    }

}
