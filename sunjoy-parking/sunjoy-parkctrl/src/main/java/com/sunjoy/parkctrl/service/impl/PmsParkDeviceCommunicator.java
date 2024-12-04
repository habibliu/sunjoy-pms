package com.sunjoy.parkctrl.service.impl;

import com.sunjoy.common.core.utils.uuid.UUID;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.mqtt.config.SunjoyMqttClient;
import com.sunjoy.mqtt.domain.DeviceDirectivePayload;
import com.sunjoy.parkctrl.service.IPmsParkTransactionService;
import com.sunjoy.parking.entity.PmsDevice;
import com.sunjoy.parking.entity.PmsParkTransaction;
import com.sunjoy.parking.enums.DeviceFunctionEnum;
import com.sunjoy.parking.utils.MqttTopics;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parking.vo.VehiclePassage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Autowired
    private IPmsParkTransactionService pmsParkTransactionService;

    /**
     * 根据出入场流水事务ID开闸
     *
     * @param transId
     */
    public void notifyToReleaseVehicle(Long transId) {
        PmsParkTransaction parkTransaction = pmsParkTransactionService.pickupParkTransactionRecord(transId);
        if (null != parkTransaction) {
            VehiclePassage vehiclePassage = null;
            if (null != parkTransaction.getExitLaneId()) {
                //出场支付
                vehiclePassage = redisService.getCacheObject(RedisKeyConstants.PARK_VEHICLE_IN_LANE + parkTransaction.getExitLaneId());
            } else {
                //入场支付
                vehiclePassage = redisService.getCacheObject(RedisKeyConstants.PARK_VEHICLE_IN_LANE + parkTransaction.getEntryLaneId());
            }
            //通知车场开闸放行
            if (null != vehiclePassage) {
                if (!vehiclePassage.getLicensePlate().equals(parkTransaction.getLicensePlate())) {
                    log.warn("当前通道上识别的车是{}，不是订单支付的车{}，不开闸", vehiclePassage.getLicensePlate(), parkTransaction.getLicensePlate());
                    return;
                }
                try {
                    this.releaseVehicle(vehiclePassage);
                } catch (Exception e) {
                    log.error("通道:{}开闸失败,原因:!", vehiclePassage.getParkLane().getLaneName(), e.fillInStackTrace());
                }
            } else {
                log.warn("车辆{}不在通道上，不开闸!", parkTransaction.getLicensePlate());
            }
        }
    }


    /**
     * 开闸放行
     *
     * @param vehiclePassage 车辆事务ID
     */
    public void releaseVehicle(VehiclePassage vehiclePassage) throws MqttException {
        log.info("通知设备开闸放行.....");
        //todo 找出
        DeviceDirectivePayload payload = new DeviceDirectivePayload();
        payload.setUuid(UUID.fastUUID().toString().replace("-", ""));
        payload.setEventTime(LocalDateTime.now());
        payload.setMessage(vehiclePassage.getNotifyMessage());
        List<PmsDevice> laneDevices = vehiclePassage.getLaneDevices();
        List<String> deviceCodes = new ArrayList<>();
        laneDevices.forEach(device -> {

            if (device.getFunctions().contains(DeviceFunctionEnum.REL.getCode()) ||
                device.getFunctions().contains(DeviceFunctionEnum.DIS.getCode())) {
                if (deviceCodes.stream().noneMatch(item -> item.equals(device.getDeviceCode()))) {
                    deviceCodes.add(device.getDeviceCode());
                }
            }

        });
        //发送MQTT信息到对应的通道上
        parkClientMqttClient.publish(MqttTopics.TOPIC_LANE_DIRECTIVE + vehiclePassage.getLaneId(), payload);
        //开闸成功，清空通道的车辆信息
        String key = RedisKeyConstants.PARK_VEHICLE_IN_LANE + vehiclePassage.getLaneId();
        this.redisService.deleteObject(key);
        log.info("通知设备开闸放行成功!");
        log.info("清空通道{}缓存{}", vehiclePassage.getLaneId(), key);
    }

    /**
     * 显示二维护码
     *
     * @param vehiclePassage
     */
    public void displayQrCode(VehiclePassage vehiclePassage) {
        //todo 显示二维护码逻辑
    }
}