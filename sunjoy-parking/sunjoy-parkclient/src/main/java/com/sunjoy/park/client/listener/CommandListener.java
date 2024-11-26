package com.sunjoy.park.client.listener;

import com.sunjoy.mqtt.service.MqttSubscriber;
import com.sunjoy.park.client.service.TextToSpeechService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 来自服务器的命令监听类
 *
 * @author Habib
 * @date 2024/11/25
 */
@Service
public class CommandListener implements MqttSubscriber {
    @Autowired
    private TextToSpeechService textToSpeechService;


    @Override
    public void onSubscribe(MqttClient client) {

    }

    @Override
    public String getTopic() {
        return "";
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }
}