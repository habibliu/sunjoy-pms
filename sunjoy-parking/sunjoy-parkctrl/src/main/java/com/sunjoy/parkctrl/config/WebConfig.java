package com.sunjoy.parkctrl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局支持 XML
 *
 * @author Habib
 * @date 2024/11/26
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public HttpMessageConverter<Object> xmlHttpMessageConverter() {
        return new MappingJackson2XmlHttpMessageConverter();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许所有路径
        registry.addMapping("/**")
                // 允许的源
                .allowedOrigins("http://localhost")
                // 允许的请求方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 允许发送凭证（如 cookies）
                .allowCredentials(true);
    }
}