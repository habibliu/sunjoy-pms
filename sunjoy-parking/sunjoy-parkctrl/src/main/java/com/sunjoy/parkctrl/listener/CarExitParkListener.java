package com.sunjoy.parkctrl.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.mqtt.domain.Playload;
import com.sunjoy.mqtt.service.BaseMqttMessageListener;
import com.sunjoy.parking.utils.RedisKeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/4
 */
@Slf4j
@Service
public class CarExitParkListener implements BaseMqttMessageListener {

    @Autowired
    private RedisService redisService;

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        // 处理接收到的消息
        log.info("Received message: " + message);
        //转为车辆出入场对象，并安排对应的服务类处理
        ObjectMapper objectMapper = new ObjectMapper();
        Playload playload = objectMapper.readValue(message, Playload.class);

        // 检查是否已经处理过
        if (processedMessageIds(playload.getUuid())) {
            log.info("车辆入场消息:{}其他节点已处理", playload.getUuid());
            return; // 已处理，直接返回
        }

        handle(playload);
    }

    private void handle(Playload playload) {
        Long laneId = (Long) playload.getData().get("laneId");
    }

    /**
     * 判断消息是否已被其他节点处理，如没有没，就放到缓存中，
     *
     * @param messageId
     * @return
     */
    private boolean processedMessageIds(String messageId) {
        String key = RedisKeyConstants.PARK_CAR_EXIT + messageId;
        boolean processed = null != redisService.getCacheObject(key);
        if (!processed) {
            //缓存30秒
            redisService.setCacheObject(key, messageId, 30L, TimeUnit.SECONDS);
        }
        return processed;
    }
}