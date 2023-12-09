package io.github.dingxinliang88.aspect.auth;

import cn.hutool.core.util.StrUtil;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.manager.JwtTokenManager;
import io.github.dingxinliang88.pojo.vo.UserLoginVO;
import io.github.dingxinliang88.utils.RedisUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import io.github.dingxinliang88.utils.UserHolder;
import lombok.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static io.github.dingxinliang88.constants.UserConstant.*;

/**
 * 刷新token拦截器
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private final JwtTokenManager jwtTokenManager;

    private final RedisUtil redisUtil;

    public RefreshTokenInterceptor(JwtTokenManager jwtTokenManager, RedisUtil redisUtil) {
        this.jwtTokenManager = jwtTokenManager;
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 从请求头中获取 JWT access_token
        String token = request.getHeader("Authorization");
        ThrowUtil.throwIf(StrUtil.isEmpty(token), StatusCode.NOT_LOGIN_ERROR, "Missing Token");

        // 解析校验token
        UserLoginVO userLoginVO = jwtTokenManager.validateToken(token);
        ThrowUtil.throwIf(userLoginVO == null, StatusCode.NOT_LOGIN_ERROR, "请重新登录！");
        if (jwtTokenManager.isTokenExpired(token)) {
            // token 过期，通过refresh_token生成全新的access_token
            String refreshToken = String.valueOf(redisUtil.get(REFRESH_TOKEN_PREFIX + userLoginVO.getUserId()));
            ThrowUtil.throwIf(jwtTokenManager.isTokenExpired(refreshToken), StatusCode.NOT_LOGIN_ERROR, "请重新登录!");

            // 生成新的 access_token
            String accessToken = jwtTokenManager.genAccessToken(userLoginVO);
            redisUtil.set(ACCESS_TOKEN_PREFIX + userLoginVO.getUserId(), accessToken, TOKEN_EXPIRATION_TIME, TimeUnit.MILLISECONDS);

            response.setHeader("Authorization", accessToken);
            UserHolder.setUser(userLoginVO);
        } else {
            // token 未过期
            UserHolder.setUser(userLoginVO);
        }

        return true;
    }
}
