package com.sunjoy.park.client;

import com.sunjoy.common.security.annotation.EnableCustomConfig;
import com.sunjoy.park.client.filter.CorsFilter;
import com.sunjoy.park.client.service.SysUserService;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Class description
 *
 * @author Habib
 * &#064;date  2024/11/19
 */
@Slf4j
@EnableCustomConfig
@SpringBootApplication(scanBasePackages = "com.sunjoy")
public class SunjoyParkClientApplication implements ServletContextInitializer {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SunjoyParkClientApplication.class);

        SysUserService sysUserService = context.getBean(SysUserService.class);
        sysUserService.initUser();
        log.info("车辆客户端服务启动成功");
    }

    /**
     * 注册跨域过滤器，并设置过滤规则
     *
     * @param servletContext the {@code ServletContext} to initialize
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        FilterRegistration.Dynamic corsFilter = servletContext.addFilter("corsFilter", new CorsFilter());
        corsFilter.addMappingForUrlPatterns(null, false, "/*");
    }
}