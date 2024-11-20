package com.sunjoy.parkctrl.config;

import com.sunjoy.mqtt.config.SunjoyMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/19
 */
@Configuration
public class BeanConfig {

    private String brokerUrl = "tcp://192.168.2.11:1883";
    private String clientId = "尚久停车管理平台";

    @Bean
    @Scope("singleton")
    public SunjoyMqttClient parkClientMqttClient() throws MqttException {
        return new SunjoyMqttClient(brokerUrl, clientId);
    }

}