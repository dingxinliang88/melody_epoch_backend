package io.github.dingxinliang88.job;

import io.github.dingxinliang88.constants.AlbumConstant;
import io.github.dingxinliang88.constants.UserConstant;
import io.github.dingxinliang88.manager.JwtTokenManager;
import io.github.dingxinliang88.manager.SensitiveHandler;
import io.github.dingxinliang88.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 跟业务有关的Job
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Component
public class BizJob {

    private final Logger logger = LoggerFactory.getLogger(BizJob.class);

    private final JwtTokenManager jwtTokenManager;

    private final RedisUtil redisUtil;

    private final SensitiveHandler sensitiveHandler;

    @Autowired
    public BizJob(JwtTokenManager jwtTokenManager, RedisUtil redisUtil, SensitiveHandler sensitiveHandler) {
        this.jwtTokenManager = jwtTokenManager;
        this.redisUtil = redisUtil;
        this.sensitiveHandler = sensitiveHandler;
    }


    /**
     * 每12个小时清理过期的token
     */
    @Scheduled(cron = "0 0 */12 * * ?")
    public void cleanExpiredTokens() {
        jwtTokenManager.cleanExpiredTokens();
    }

    /**
     * 每1小时清理Top Album的缓存
     * 条件：期间打分过多（需要更新）
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void clearTopAlbumsCache() {
        try {
            int scoreNum = Integer.parseInt(redisUtil.get(AlbumConstant.TOP_ALBUM_CACHE_EPOCH_KEY).toString());
            if (scoreNum >= AlbumConstant.EPOCH) {
                redisUtil.delete(AlbumConstant.TOP_ALBUMS_KEY, AlbumConstant.TOP_ALBUM_CACHE_EPOCH_KEY);
                redisUtil.setExpiredHours(AlbumConstant.TOP_ALBUM_CACHE_EPOCH_KEY, AlbumConstant.INIT_EPOCH, AlbumConstant.TOP_ALBUMS_EXPIRE_TIME);
            }
        } catch (Exception e) {
            logger.error("clearTopAlbumsCache: ", e);
        }
    }

    /**
     * 每2小时处理敏感词触发过多的用户，执行封号处理
     */
    @Scheduled(cron = "0 0 */2 * * ?")
    public void handleSensitive() {
        String pattern = UserConstant.SENSITIVE_ACC_PREFIX + "*";
        try {
            Set<String> keys = redisUtil.keys(pattern);
            for (String key : keys) {
                int accSensitiveCount = Integer.parseInt(redisUtil.get(key).toString());
                if (accSensitiveCount >= UserConstant.SENSITIVE_THRESHOLD) {
                    // 执行封号处理
                    String userIdStr = key.substring(key.lastIndexOf(":") + 1);
                    Integer userId = Integer.parseInt(userIdStr);
                    sensitiveHandler.doBanUser(userId);
                }
            }
        } catch (Exception e) {
            logger.error("handleSensitive: ", e);
        }
    }
}
