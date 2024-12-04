package com.sunjoy.park.client.config;

import com.sunjoy.mqtt.config.SunjoyMqttClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/19
 */
@Configuration
@Data
@Slf4j
@ConfigurationProperties(prefix = "mqtt")
public class ParkClientConfig {
    private String brokerUrl;
    private String clientName;

    @Bean
    public SunjoyMqttClient parkClientMqttClient() throws MqttException {
        //String brokerUrl = "tcp://192.168.2.11:1883"; // EMQX 的地址
        //String clientId = MqttAsyncClient.generateClientId();
        return new SunjoyMqttClient(brokerUrl, clientName);

    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ThreadPoolTaskExecutor clientExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }


}