package com.sunjoy.parkctrl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

/**
 * 全局支持 XML
 *
 * @author Habib
 * @date 2024/11/26
 */
@Configuration
public class WebConfig {
    @Bean
    public HttpMessageConverter<Object> xmlHttpMessageConverter() {
        return new MappingJackson2XmlHttpMessageConverter();
    }
}