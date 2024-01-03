package io.github.dingxinliang88.manager;

import io.github.dingxinliang88.mapper.UserMapper;
import io.github.dingxinliang88.pojo.po.User;
import io.github.dingxinliang88.utils.RedisUtil;
import io.github.dingxinliang88.utils.SysUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

import static io.github.dingxinliang88.constants.UserConstant.*;

/**
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Component
public class SensitiveHandler {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserMapper userMapper;

    /**
     * 敏感词计数器
     *
     * @param userId    user id
     * @param condition condition
     */
    public void handleAccSensitive(Integer userId, boolean condition) {
        if (!condition) {
            return;
        }
        // 敏感词触发条件
        CompletableFuture.runAsync(() -> {
            String key = SENSITIVE_ACC_PREFIX + userId;
            if (redisUtil.get(key) == null) {
                synchronized (userId.toString().intern()) {
                    if (redisUtil.get(key) == null) {
                        // 一个月时间，触发敏感词数量为5次
                        redisUtil.setExpiredDays(key, SENSITIVE_INIT, EXPIRE_ONE_MONTH);
                    }
                }
            }
            // 次数加一
            redisUtil.increment(key, 1);
        }, USER_SENSITIVE_ACC_POOL);
    }

    /**
     * 执行封号
     *
     * @param userId user id
     */
    public void doBanUser(Integer userId) {
        User user = userMapper.queryByUserId(userId);
        Integer bannedType = SysUtil.genBannedType(user.getType());
        userMapper.updateTypeByUserId(user.getUserId(), bannedType);
    }


}
