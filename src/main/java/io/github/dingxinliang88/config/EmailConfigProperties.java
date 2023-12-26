package io.github.dingxinliang88.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 邮箱配置
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@ConfigurationProperties(prefix = "spring.email")
@Configuration
@Getter
@Setter
public class EmailConfigProperties {

    private String from = "codejuzi@qq.com";
}
