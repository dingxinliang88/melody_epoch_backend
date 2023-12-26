package io.github.dingxinliang88.config;

import io.github.dingxinliang88.aspect.auth.RefreshTokenInterceptor;
import io.github.dingxinliang88.manager.JwtTokenManager;
import io.github.dingxinliang88.utils.RedisUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 跨域配置
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private JwtTokenManager jwtTokenManager;

    
    public void addCorsMappings(CorsRegistry registry) {
        registry
                // 覆盖所有请求
                .addMapping("/**")
                // 允许发送cookie
                .allowCredentials(true)
                // .allowedOrigins("http://127.0.0.1:5173", "http://codejuzi.icu", "http://xxx.xxx.xxx")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");

    }

    
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(new RefreshTokenInterceptor(jwtTokenManager, redisUtil))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/**/acc_login/**",
                        "/**/email_login/**",
                        "/**/email/captcha",
                        "/**/acc_reg/**",
                        "/**/email_reg/**",
                        "/**/doc.html/**",
                        "/static/**",
                        "/**/v3/api-docs/**",
                        "/**/v2/api-docs/**",
                        "/**/swagger-ui/**",
                        "/**/swagger-ui.html/**",
                        "/**/error",
                        "/**/test/**",
                        "/**/favicon.ico",
                        "/**/swagger-resources/**",
                        "/**/webjars/**"
                );
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
