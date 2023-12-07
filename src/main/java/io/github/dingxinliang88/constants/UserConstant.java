package io.github.dingxinliang88.constants;

import cn.hutool.core.collection.CollectionUtil;

import java.util.Set;

import static io.github.dingxinliang88.pojo.enums.UserRoleType.*;

/**
 * 用户常量类
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public interface UserConstant {

    // region login and register
    int SALT_LEN = 16;

    int ACC_MIN_LEN = 5;
    int ACC_MAX_LEN = 16;

    int PWD_MIN_LEN = 8;
    int PWD_MAX_LEN = 16;

    String DEFAULT_NICK_NAME_PREFIX = "user_";
    int NICK_NAME_LEN = 20;

    String LOGIN_STATE_KEY = "user:login:state";
    // endregion

    // user role type
    Set<Integer> USER_ROLE_SET = CollectionUtil.newHashSet(ADMIN.getType(), MEMBER.getType(), FAN.getType());

    // endregion
}
