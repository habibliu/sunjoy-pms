package com.sunjoy.parkmodel;

import com.sunjoy.common.security.annotation.EnableCustomConfig;
import com.sunjoy.common.security.annotation.EnableRyFeignClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 车场模型及基本资料服务启动类
 *
 * @author Habib
 * @date 2024/10/17
 */
@Slf4j
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class SunjoyParkModelApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext contex = SpringApplication.run(SunjoyParkModelApplication.class);
        log.info("车场模型及基本资料服务启动成功");
    }
}