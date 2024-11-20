package com.sunjoy.mqtt.service;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/20
 */
public interface MqttSubscriber extends IMqttMessageListener {
    void onSubscribe(MqttClient client);

    String getTopic();
}