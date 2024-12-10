package com.sunjoy.mqtt.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.mqtt.domain.Payload;
import com.sunjoy.mqtt.service.MqttSubscriber;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Mqtt客户端
 *
 * @author Habib
 * @date 2024/11/19
 */
@Slf4j
public class SunjoyMqttClient {
    private final String brokerUrl;
    private final ScheduledExecutorService executorService;
    /**
     * 实例化MqttClient的参数
     */
    private final MqttConnectOptions options;
    private final MqttClient mqttClient;
    /**
     * json对象及字符串转换类
     */
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 监听类
     */
    private List<MqttSubscriber> subscribers = new ArrayList<>();
    private boolean isReconnecting = false;

    public SunjoyMqttClient(String brokerUrl, String clientName) throws MqttException {
        this.brokerUrl = brokerUrl;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setKeepAliveInterval(30);
        options.setUserName(clientName);
        String clientId = MqttAsyncClient.generateClientId();
        mqttClient = new MqttClient(brokerUrl, clientId);
        connect();
        //startHeartbeat();
    }

    public void addSubscriber(MqttSubscriber subscriber) {
        subscribers.add(subscriber);
        //如果已经连接成功，就订阅
        if (this.mqttClient.isConnected()) {
            try {
                this.mqttClient.subscribe(subscriber.getTopic(), 2, subscriber);
            } catch (MqttException e) {
                log.error("subscribe failed", e);
            }
        }
    }

    private void connect() throws MqttException {

        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                log.error("Connection lost: " + cause.getMessage());

                reconnect();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                log.info("Message arrived: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                log.info("Delivery complete: " + token.getMessageId());
            }

            private void reconnect() {
                if (isReconnecting) {
                    return;
                }
                isReconnecting = true;
                while (!mqttClient.isConnected()) {
                    try {
                        log.info("Trying to connect to broker: {}", brokerUrl);
                        mqttClient.connect(options); // 使用之前设置的连接选项
                        log.info("Reconnected to broker {} successfully.", brokerUrl);
                        isReconnecting = false;
                        // 连接成功后重新订阅
                        for (MqttSubscriber subscriber : subscribers) {
                            subscriber.onSubscribe(mqttClient);
                        }
                    } catch (MqttException e) {
                        log.error("Reconnect failed: " + e.getMessage());
                        try {
                            Thread.sleep(5000); // 等待 5 秒后再重试
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt(); // 恢复中断状态
                        }
                    }
                }
            }
        });
        mqttClient.connect(options);
        // 连接成功后重新订阅
        for (MqttSubscriber subscriber : subscribers) {
            subscriber.onSubscribe(mqttClient);
        }
        log.info("Connected to broker: " + brokerUrl);
    }

    public void publish(String topic, Object playload) throws MqttException {
        String message = null;
        if (playload instanceof Payload) {
            try {
                message = objectMapper.writeValueAsString(playload);
            } catch (JsonProcessingException je) {
                message = playload.toString();
            }

        } else {

            message = playload.toString();

        }
        mqttClient.publish(topic, message.getBytes(), 2, true);
        log.info("success published: {} to :{} ", playload, topic);

    }


    public void subscribe(String topic, IMqttMessageListener messageListener) throws MqttException {
        mqttClient.subscribe(topic, 2, messageListener);
    }

    public void subscribe(String topic, IMqttMessageListener messageListener, int qos) throws MqttException {
        mqttClient.subscribe(topic, qos, messageListener);
    }


    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}