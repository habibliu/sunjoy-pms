package com.sunjoy.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 文件服务
 * 
 * @author sunjoy
 */
@Slf4j
//@EnableDiscoveryClient 旧版本的注解，不再需要使用
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SunjoyFileApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SunjoyFileApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  文件服务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" );
    }
}
