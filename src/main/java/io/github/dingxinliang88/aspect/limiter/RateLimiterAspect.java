package io.github.dingxinliang88.aspect.limiter;

import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.exception.BizException;
import io.github.dingxinliang88.manager.RateLimiterManager;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.utils.SysUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 限流AOP
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Aspect
@Component
public class RateLimiterAspect {

    @Resource
    private RateLimiterManager limiterManager;

    @Before("@annotation(io.github.dingxinliang88.aspect.limiter.MelodyRateLimiter)")
    public void doAuthCheck() {
        UserLoginVO currUser = SysUtil.getCurrUser();
        boolean limitRes = limiterManager.doRateLimit(currUser.getUserId().toString());
        if (!limitRes) {
            throw new BizException(StatusCode.TOO_MANY_REQUEST, "请求过于频繁，请稍后重试");
        }
    }
}
