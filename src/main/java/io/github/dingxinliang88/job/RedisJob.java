package io.github.dingxinliang88.job;

import io.github.dingxinliang88.manager.JwtTokenManager;
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

    private final JwtTokenManager jwtTokenManager;

    @Autowired
    public RedisJob(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Scheduled(cron = "0 */12 * * * *")
    public void cleanExpiredTokens() {
        jwtTokenManager.cleanExpiredTokens();
    }
}
