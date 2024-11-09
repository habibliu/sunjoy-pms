package com.sunjoy.mqtt.service;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/4
 */
@Service
public class MqttService {
    @Autowired
    private MqttClient mqttClient;

    /**
     * 方法用于发布消息
     */

    public void publish(String topic, String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(2); // 设置 QoS
        mqttClient.publish(topic, message);
    }

    /**
     * 方法用于订阅主题
     *
     * @param topic
     * @param messageListener
     */
    public void subscribe(String topic, IMqttMessageListener messageListener) throws MqttException {
        mqttClient.subscribe(topic, messageListener);
    }
}