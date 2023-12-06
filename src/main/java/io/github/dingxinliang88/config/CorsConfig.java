package io.github.dingxinliang88.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                // 覆盖所有请求
                .addMapping("/*")
                // 允许发送cookie
                .allowCredentials(true)
                // TODO 根据项目实际情况修改放行域名
                // .allowedOrigins("http://127.0.0.1:5173", "http://codejuzi.icu", "http://xxx.xxx.xxx")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*");

    }
}
