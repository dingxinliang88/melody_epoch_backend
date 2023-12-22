package io.github.dingxinliang88.job;

import io.github.dingxinliang88.constants.AlbumConstant;
import io.github.dingxinliang88.manager.JwtTokenManager;
import io.github.dingxinliang88.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 跟Redis有关的Job
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Component
public class RedisJob {

    private final Logger logger = LoggerFactory.getLogger(RedisJob.class);

    private final JwtTokenManager jwtTokenManager;

    private final RedisUtil redisUtil;

    public RedisJob(JwtTokenManager jwtTokenManager, RedisUtil redisUtil) {
        this.jwtTokenManager = jwtTokenManager;
        this.redisUtil = redisUtil;
    }

    @Autowired


    @Scheduled(cron = "0 0 */12 * * *")
    public void cleanExpiredTokens() {
        jwtTokenManager.cleanExpiredTokens();
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void clearTopAlbumsCache() {
        // Use a try-catch block to handle potential NumberFormatException
        try {
            int scoreNum = Integer.parseInt(redisUtil.get(AlbumConstant.TOP_ALBUM_CACHE_EPOCH_KEY).toString());

            if (scoreNum >= AlbumConstant.EPOCH) {
                // Use Redis batch deletion for efficiency
                redisUtil.delete(AlbumConstant.TOP_ALBUMS_KEY, AlbumConstant.TOP_ALBUM_CACHE_EPOCH_KEY);

                // Use a single call to set both keys and their expiration
                redisUtil.setExpiredHours(AlbumConstant.TOP_ALBUM_CACHE_EPOCH_KEY, AlbumConstant.INIT_EPOCH, AlbumConstant.TOP_ALBUMS_EXPIRE_TIME);
            }
        } catch (NumberFormatException e) {
            // Handle the NumberFormatException appropriately (e.g., log the error)
            logger.error("clearTopAlbumsCache: ", e);

        }

    }
}
