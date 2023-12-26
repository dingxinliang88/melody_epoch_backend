package io.github.dingxinliang88.aspect.auth;


import java.lang.annotation.*;

/**
 * 权限校验注解
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthCheck {

    /**
     * @return 角色
     */
    String role();
}