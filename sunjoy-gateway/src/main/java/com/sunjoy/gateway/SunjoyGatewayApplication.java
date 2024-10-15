package com.sunjoy.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 网关启动程序
 *
 * @author sunjoy
 */
@EnableConfigurationProperties
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SunjoyGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(SunjoyGatewayApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  尚久网关启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}