package com.sunjoy.park.client;

import com.sunjoy.common.security.annotation.EnableCustomConfig;
import com.sunjoy.common.security.annotation.EnableRyFeignClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Class description
 *
 * @author Habib
 * &#064;date  2024/11/19
 */
@Slf4j
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication(scanBasePackages = "com.sunjoy")
public class SunjoyParkClientApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext contex = SpringApplication.run(SunjoyParkClientApplication.class);
        log.info("车辆客户端服务启动成功");
    }
}