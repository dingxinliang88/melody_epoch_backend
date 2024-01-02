package io.github.dingxinliang88.constants;

import cn.hutool.core.lang.UUID;

import java.util.concurrent.*;

/**
 * 用户常量类
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
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

    String ACCESS_TOKEN_PREFIX = "user:access_token:";
    String REFRESH_TOKEN_PREFIX = "user:refresh_token:";
    long TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24h => ms
    long REFRESH_TOKEN_EXPIRATION_TIME = 30L * 24 * 60 * 60 * 1000; // 24h => ms

    // endregion

    // user role type
    String USER_AUTH_TYPE_PREFIX = "user:auth:";

    // endregion

    // region access sensitive threshold

    String SENSITIVE_ACC_PREFIX = "user:sensitive:";

    int SENSITIVE_THRESHOLD = 5;

    int SENSITIVE_INIT = 0;

    int EXPIRE_ONE_MONTH = 30;

    ExecutorService USER_SENSITIVE_ACC_POOL = new ThreadPoolExecutor(
            1, 2, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(50),
            r -> new Thread(r, "user-sensitive-acc-" + UUID.randomUUID().toString(true)), new ThreadPoolExecutor.AbortPolicy()
    );

    // endregion
}
