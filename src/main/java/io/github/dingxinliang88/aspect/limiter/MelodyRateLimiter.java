package io.github.dingxinliang88.aspect.limiter;

import java.lang.annotation.*;

/**
 * 标记注解，限流
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MelodyRateLimiter {
}
