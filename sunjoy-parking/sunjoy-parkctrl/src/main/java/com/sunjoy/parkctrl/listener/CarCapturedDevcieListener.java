package com.sunjoy.parkctrl.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.common.core.utils.SpringUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.mqtt.domain.VehicleArrivedPayload;
import com.sunjoy.mqtt.service.MqttSubscriber;
import com.sunjoy.parking.entity.PmsLaneDevice;
import com.sunjoy.parking.enums.DirectionEnum;
import com.sunjoy.parking.utils.MqttTopics;
import com.sunjoy.parking.utils.RedisKeyConstants;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
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
@EnableAsync
@Service
public class CarCapturedDevcieListener implements MqttSubscriber {
    /**
     * 本地缓存，加速处理速度
     */
    private static final List<PmsLaneDevice> localDeviceLanceCache = new ArrayList<PmsLaneDevice>();
    @Autowired
    private ThreadPoolTaskExecutor pmsTaskExecutor;

    @Autowired
    private RedisService redisService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        // 处理接收到的消息
        log.info("Received topic:　{}　message:{}} ", topic, message);
        //转为车辆出入场对象，并安排对应的服务类处理

        VehicleArrivedPayload payload = convertMqttMessageToPayload(mqttMessage);
        //根据设备ID,将收到的消息放到消息队列，由对应的服务处理
        String handlerTopic = matchHandlerTopic(payload);
        if (handlerTopic == null) {
            log.warn("设备{}没有在平台注册，不处理！", payload.getDeviceId());
            return;
        }
        BaseVehicleArrivedHandler handler = null;
        if (MqttTopics.TOPIC_CAR_ARRIVED.equals(handlerTopic)) {
            payload.setDirection(DirectionEnum.ENTRY.getValue());
            handler = SpringUtils.getBean(VehicleEntryParkHandler.class);
            handler.setPayLoad(payload);

        } else {
            payload.setDirection(DirectionEnum.EXIT.getValue());
            handler = SpringUtils.getBean(VehicleExitParkHandler.class);


        }
        handler.setPayLoad(payload);
        pmsTaskExecutor.execute(handler);
    }

    public VehicleArrivedPayload convertMqttMessageToPayload(MqttMessage mqttMessage) {
        try {

            // 获取消息负载并转换为字符串
            String payload = new String(mqttMessage.getPayload(), StandardCharsets.UTF_8);
            // 使用 ObjectMapper 将 JSON 字符串转换为 Playload 对象
            return objectMapper.readValue(payload, VehicleArrivedPayload.class);
        } catch (Exception e) {
            log.error("转换对象:{},失败:{}", mqttMessage, (Object) e.getStackTrace());

            return null;
        }
    }

    /**
     * 根据设备与通道的关系，找出车辆的行驶方向，是出场还是入场，从而确认处理主题
     *
     * @param playload
     * @return
     */
    private String matchHandlerTopic(VehicleArrivedPayload playload) {
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
                direction = DirectionEnum.EXIT.getValue();
            }
            return DirectionEnum.EXIT.getValue().equals(direction) ? MqttTopics.TOPIC_CAR_DEPARTED : MqttTopics.TOPIC_CAR_ARRIVED;

        }

        return null;
    }

    /**
     * 订阅主题
     *
     * @param client
     */
    @Override
    public void onSubscribe(MqttClient client) {
        try {
            client.subscribe(getTopic(), 2, this);
            log.info("Subscribed to topic: " + MqttTopics.TOPIC_CAR_CAPTURED);
        } catch (MqttException e) {
            log.error("Subscribe failed", e);
        }
    }

    @Override
    public String getTopic() {
        return MqttTopics.TOPIC_CAR_CAPTURED;
    }
}