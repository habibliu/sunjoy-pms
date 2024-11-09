package com.sunjoy.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/4
 */
@Configuration
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfig {

    private String brokerUrl;////= "tcp://your-emqx-broker:1883"; // EMQX Broker URL
    private String clientId;// = "your-client-id";

    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient client = new MqttClient(brokerUrl, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        client.connect(options);
        return client;
    }


}