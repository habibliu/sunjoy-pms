package com.sunjoy.parkbill;

import com.sunjoy.common.security.annotation.EnableCustomConfig;
import com.sunjoy.common.security.annotation.EnableRyFeignClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 车场计费服务
 *
 * @author Habib
 * @date 2024/11/18
 */
@Slf4j
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class PmsSunjoyBillingApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext contex = SpringApplication.run(PmsSunjoyBillingApplication.class);
        log.info("车辆计费服务启动成功");
    }
}