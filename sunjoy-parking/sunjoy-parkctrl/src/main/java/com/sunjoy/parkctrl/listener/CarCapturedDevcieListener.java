package com.sunjoy.parkctrl.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.mqtt.domain.Playload;
import com.sunjoy.mqtt.service.BaseMqttMessageListener;
import com.sunjoy.mqtt.service.MqttService;
import com.sunjoy.parking.entity.PmsLaneDevice;
import com.sunjoy.parking.utils.MqttTopics;
import com.sunjoy.parking.utils.RedisKeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 车辆在车场被捕捉到的监听器
 *
 * @author Habib
 * @date 2024/11/4
 */
@Slf4j
@Service
public class CarCapturedDevcieListener implements BaseMqttMessageListener {
    /**
     * 本地缓存，加速处理速度
     */
    private static List<PmsLaneDevice> localDeviceLanceCache = new ArrayList<PmsLaneDevice>();
    private MqttService mqttService;
    @Autowired
    private RedisService redisService;

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        // 处理接收到的消息
        log.info("Received topic:　{}　message:{}} ", topic, message);
        //转为车辆出入场对象，并安排对应的服务类处理
        ObjectMapper objectMapper = new ObjectMapper();
        Playload playload = objectMapper.readValue(message, Playload.class);
        //根据设备ID,将收到的消息放到消息队列，由对应的服务处理
        String handlerTopic = matchHandlerTopic(playload);
        if (handlerTopic == null) {
            log.error("没有对应的处理类接收！");
        }
        mqttService.publish(handlerTopic, message);
        log.info("dispatch to {} topic consummer handle!", handlerTopic);
    }

    /**
     * 根据设备与通道的关系，找出车辆的行驶方向，是出场还是入场，从而确认处理主题
     *
     * @param playload
     * @return
     */
    private String matchHandlerTopic(Playload playload) {
        List<PmsLaneDevice> deviceLaneList = localDeviceLanceCache.stream().filter(item -> Objects.equals(item.getDeviceId(), playload.getDeviceId())).collect(Collectors.toList());
        if (deviceLaneList.isEmpty()) {
            deviceLaneList = this.redisService.getCacheList(RedisKeyConstants.PARK_LANE_DEVICE);
            deviceLaneList = deviceLaneList.stream().filter(item -> Objects.equals(item.getDeviceId(), playload.getDeviceId())).collect(Collectors.toList());
            if (!deviceLaneList.isEmpty()) {
                localDeviceLanceCache.addAll(deviceLaneList);
            }
        }
        if (!deviceLaneList.isEmpty()) {
            String direction = null;
            if (deviceLaneList.size() == 1) {
                direction = deviceLaneList.get(0).getDirection();
            } else {
                direction = "0";
            }
            return "0".equals(direction) ? MqttTopics.TOPIC_CAR_DEPARTED : MqttTopics.TOPIC_CAR_ARRIVED;

        }

        return null;
    }
}