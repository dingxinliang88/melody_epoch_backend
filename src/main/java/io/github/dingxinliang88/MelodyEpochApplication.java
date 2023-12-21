package io.github.dingxinliang88;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * APP
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@EnableScheduling
@MapperScan(basePackages = {"io.github.dingxinliang88.mapper"})
@SpringBootApplication
public class MelodyEpochApplication {
    public static void main(String[] args) {
        SpringApplication.run(MelodyEpochApplication.class, args);
    }
}
