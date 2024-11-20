package com.sunjoy.mqtt.service;

import com.sunjoy.mqtt.config.SunjoyMqttClient;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Mqtt服务类
 *
 * @author Habib
 * @date 2024/11/4
 */
@Slf4j
@Service
public class MqttService {
    @Autowired
    private SunjoyMqttClient mqttConnectionPool;

    /**
     * 方法用于发布消息
     */

    public void publish(String topic, String payload) throws MqttException {
        // MqttMessage message = new MqttMessage(payload.getBytes());
        // 设置 QoS
        //message.setQos(2);
        mqttConnectionPool.publish(topic, payload);
        log.info("MqttService publish message: {} to {}", payload, topic);
    }

    /**
     * 方法用于订阅主题
     *
     * @param topic
     * @param messageListener
     */
    public void subscribe(String topic, IMqttMessageListener messageListener) throws MqttException {
        mqttConnectionPool.subscribe(topic, messageListener);
    }
}