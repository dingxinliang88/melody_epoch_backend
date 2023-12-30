package io.github.dingxinliang88.manager;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 限流管理类
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Component
@Slf4j
@ConfigurationProperties(prefix = "melody.limiter")
public class RateLimiterManager {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 执行限流，设定每秒最多访问十次
     *
     * @param key 限流key
     * @return true - 可以操作
     */
    public boolean doRateLimit(String key) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);

        rateLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.SECONDS);

        // 每一个操作来，申请一个令牌
        return rateLimiter.tryAcquire(1);
    }

}