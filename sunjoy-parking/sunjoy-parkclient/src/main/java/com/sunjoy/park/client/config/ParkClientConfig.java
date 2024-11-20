package com.sunjoy.park.client.config;

import com.sunjoy.mqtt.config.SunjoyMqttClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/19
 */
@Configuration
@Data
@Slf4j
public class ParkClientConfig {

    @Bean
    public SunjoyMqttClient parkClientMqttClient() throws MqttException {
        String brokerUrl = "tcp://192.168.2.11:1883"; // EMQX 的地址
        //String clientId = MqttAsyncClient.generateClientId();
        return new SunjoyMqttClient(brokerUrl, "黄金山小海停车场");

    }
}