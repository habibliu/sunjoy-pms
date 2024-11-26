package com.sunjoy.parkctrl.service.impl;

import com.sunjoy.common.core.utils.uuid.UUID;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.mqtt.config.SunjoyMqttClient;
import com.sunjoy.mqtt.domain.VehicleReleasePayload;
import com.sunjoy.parking.entity.PmsDevice;
import com.sunjoy.parking.utils.MqttTopics;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parking.vo.VehiclePassage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备通讯器
 * 负责与设备端通讯
 *
 * @author Habib
 * @date 2024/11/25
 */
@Slf4j
@Service
public class PmsParkDeviceCommunicator {
    @Autowired
    private RedisService redisService;

    @Autowired
    private SunjoyMqttClient parkClientMqttClient;

    /**
     * 开闸放行
     *
     * @param vehiclePassage 车辆事务ID
     */
    void releaseVehicle(VehiclePassage vehiclePassage) throws MqttException {
        log.info("通知设备开闸放行.....");
        //todo 找出
        VehicleReleasePayload payload = new VehicleReleasePayload();
        payload.setUuid(UUID.fastUUID().toString().replace("-", ""));
        payload.setEventTime(LocalDateTime.now());
        List<PmsDevice> laneDevices = vehiclePassage.getLaneDevices();
        laneDevices.forEach(device -> {

        });
        //todo 发送MQTT信息
        parkClientMqttClient.publish(MqttTopics.TOPIC_VEHICLE_RELEASE, payload);
        //开闸成功，清空通道的车辆信息
        String key = RedisKeyConstants.PARK_VEHICLE_IN_LANE + vehiclePassage.getLaneId();
        this.redisService.deleteObject(key);
        log.info("通知设备开闸放行成功!");
        log.info("清空通道{}缓存{}", vehiclePassage.getLaneId(), key);
    }
}