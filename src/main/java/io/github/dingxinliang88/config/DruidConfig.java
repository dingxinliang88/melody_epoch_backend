package io.github.dingxinliang88.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源Druid配置
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@ConfigurationProperties(prefix = "melody.druid")
@Configuration
@Setter
@Getter
public class DruidConfig {


    /**
     * druid dashboard username
     */
    private String username;

    /**
     * druid dashboard password
     */
    private String password;

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * 配置 Druid 监控管理后台的Servlet；
     * 内置 Servlet 容器时没有web.xml文件，所以使用 Spring Boot 的注册 Servlet 方式
     *
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> statViewServlet() {
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

        // 这些参数可以在 com.alibaba.druid.support.http.StatViewServlet
        // 的父类 com.alibaba.druid.support.http.ResourceServlet 中找到
        Map<String, String> initParams = new HashMap<>();
        initParams.put("loginUsername", username); // 后台管理界面的登录账号
        initParams.put("loginPassword", password); // 后台管理界面的登录密码

        // 后台允许谁可以访问
        // value - localhost：表示只有本机可以访问
        // 为空或者为null时，表示允许所有访问
        initParams.put("allow", "");

        // deny：Druid 后台拒绝谁访问
        // initParams.put("deny", "192.168.1.20");表示禁止此ip访问

        // 设置初始化参数
        bean.setInitParameters(initParams);
        return bean;
    }


    /**
     * 配置 Druid 监控 web 监控的 filter
     * WebStatFilter：用于配置Web和Druid数据源之间的管理关联监控统计
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> webStatFilter() {
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());

        // exclusions：设置哪些请求进行过滤排除掉，从而不进行统计
        Map<String, String> initParams = new HashMap<>();
        initParams.put("exclusions", "*.js,*.css,/druid/*,/jdbc/*");
        bean.setInitParameters(initParams);

        // "/*" 表示过滤所有请求
        bean.setUrlPatterns(Arrays.asList("/*", "/api/*", "/admin/*"));
        return bean;
    }
}
