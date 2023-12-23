package io.github.dingxinliang88.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.github.dingxinliang88.constants.CommonConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MP 配置
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * MP分页插件配置
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInterceptor.setMaxLimit(CommonConstant.MAX_PAGE_SIZE_LIMIT);
        interceptor.addInnerInterceptor(paginationInterceptor);
        return interceptor;
    }
}
