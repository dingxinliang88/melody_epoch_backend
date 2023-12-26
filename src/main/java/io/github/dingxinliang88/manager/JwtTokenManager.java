package io.github.dingxinliang88.manager;

import io.github.dingxinliang88.pojo.dto.JwtToken;
import io.github.dingxinliang88.pojo.po.User;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static io.github.dingxinliang88.constants.UserConstant.*;

/**
 * JWT token 管理器
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Component
public class JwtTokenManager {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenManager.class);

    @Resource
    private RedisUtil redisUtil;

    @Value("${jwt.secret_key}")
    private String secretKey;

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String genAccessToken(UserLoginVO user) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getUserId()));
        claims.put("type", user.getType());
        claims.put("nickname", user.getNickname());
        final Date NOW = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(NOW)
                .setExpiration(new Date(NOW.getTime() + TOKEN_EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
        String key = ACCESS_TOKEN_PREFIX + user.getUserId();
        redisUtil.set(key, token, TOKEN_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        return token;
    }


    public String genRefreshToken(UserLoginVO user) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getUserId()));
        final Date NOW = new Date();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(NOW)
                .setExpiration(new Date(NOW.getTime() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        String key = REFRESH_TOKEN_PREFIX + user.getUserId();
        redisUtil.set(key, refreshToken, REFRESH_TOKEN_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        return refreshToken;
    }

    public UserLoginVO validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userIdStr = claims.getSubject();
            Integer type = claims.get("type", Integer.class);
            String nickname = claims.get("nickname", String.class);

            String storedToken = String.valueOf(redisUtil.get(ACCESS_TOKEN_PREFIX + userIdStr));

            if (storedToken != null && storedToken.equals(token)) {
                // 验证通过
                return UserLoginVO.builder()
                        .userId(Integer.parseInt(userIdStr))
                        .type(type)
                        .nickname(nickname)
                        .build();
            }
        } catch (Exception e) {
            logger.error("invalid token: ", e);
        }

        return null;
    }

    public void revokeToken(UserLoginVO user) {
        redisUtil.delete(ACCESS_TOKEN_PREFIX + user.getUserId());
        redisUtil.delete(REFRESH_TOKEN_PREFIX + user.getUserId());
    }

    public void revokeToken(User user) {
        redisUtil.delete(ACCESS_TOKEN_PREFIX + user.getUserId());
        redisUtil.delete(REFRESH_TOKEN_PREFIX + user.getUserId());
    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build().parseClaimsJws(token)
                .getBody().getExpiration();

        return expiration.before(new Date());
    }

    public void cleanExpiredTokens() {
        Objects.requireNonNull(redisUtil.keys("user:*")).forEach(key -> {
            if (key.startsWith(ACCESS_TOKEN_PREFIX) || key.startsWith(REFRESH_TOKEN_PREFIX)) {
                String token = String.valueOf(redisUtil.get(key));
                if (token != null && isTokenExpired(token)) {
                    redisUtil.delete(key);
                }
            }
        });
    }


    public void save2Redis(JwtToken jwtToken, UserLoginVO user) {
        String token = jwtToken.getToken();
        String refreshToken = jwtToken.getRefreshToken();
        redisUtil.set(ACCESS_TOKEN_PREFIX + user.getUserId(), token, TOKEN_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        redisUtil.set(REFRESH_TOKEN_PREFIX + user.getUserId(), refreshToken, REFRESH_TOKEN_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }
}
