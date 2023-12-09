package io.github.dingxinliang88.utils;

import io.github.dingxinliang88.pojo.vo.UserLoginVO;

/**
 * 用户信息管理
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
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
