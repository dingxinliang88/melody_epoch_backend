package io.github.dingxinliang88.aspect.auth;

import java.lang.annotation.*;

/**
 * 登录方法切面
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LoginFunc {
}
