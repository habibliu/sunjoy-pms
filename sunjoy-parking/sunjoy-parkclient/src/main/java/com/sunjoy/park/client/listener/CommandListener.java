package com.sunjoy.park.client.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.common.core.utils.SpringUtils;
import com.sunjoy.mqtt.domain.DeviceDirectivePayload;
import com.sunjoy.mqtt.service.MqttSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

/**
 * 来自服务器的命令监听类
 *
 * @author Habib
 * @date 2024/11/25
 */
@Slf4j
public class CommandListener implements MqttSubscriber {

    private final String topic;

    public CommandListener(String topic) {
        this.topic = topic;
    }

    @Override
    public void onSubscribe(MqttClient client) {
        try {
            client.subscribe(getTopic(), 1, this);
            log.info("Subscribed to topic:{} ", this.getTopic());
        } catch (MqttException e) {
            log.error("Subscribe failed", e);
        }
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        long start = System.currentTimeMillis();
        log.info("接收到来自于平台的指令：{}，内容是:{}", topic, message.toString());
        //todo 根据指令信息取出Payload,并转成具体的对象
        DeviceDirectivePayload payload = convertMqttMessageToPayload(message);
        payload.getDeviceCodes().forEach(deviceCode -> {

        });
        //todo 循环取出通道与指令有关的设备，向设备发送指令

        log.info("指令处理完毕，耗时:{}毫秒!", System.currentTimeMillis() - start);
    }

    private DeviceDirectivePayload convertMqttMessageToPayload(MqttMessage mqttMessage) {
        try {

            ObjectMapper objectMapper = SpringUtils.getBean(ObjectMapper.class);
            // 获取消息负载并转换为字符串
            String payload = new String(mqttMessage.getPayload(), StandardCharsets.UTF_8);
            // 使用 ObjectMapper 将 JSON 字符串转换为 Playload 对象
            return objectMapper.readValue(payload, DeviceDirectivePayload.class);
        } catch (Exception e) {
            log.error("转换对象:{},失败:{}", mqttMessage, (Object) e.getStackTrace());

            return null;
        }
    }
}