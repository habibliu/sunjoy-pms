package com.sunjoy.parkctrl;

import com.sunjoy.common.security.annotation.EnableCustomConfig;
import com.sunjoy.common.security.annotation.EnableRyFeignClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 车辆出入场管控服务
 *
 * @author Habib
 * @date 2024/11/4
 */
@Slf4j
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class SunjoyParkCtrlApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext contex = SpringApplication.run(SunjoyParkCtrlApplication.class);
        log.info("车辆出入场管控服务启动成功");
    }
}