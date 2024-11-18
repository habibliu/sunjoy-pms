package com.sunjoy.parkctrl.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.common.core.utils.DateUtils;
import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.mqtt.domain.Playload;
import com.sunjoy.mqtt.service.BaseMqttMessageListener;
import com.sunjoy.parkctrl.rule.AccessRuleFactory;
import com.sunjoy.parkctrl.rule.IAccessRule;
import com.sunjoy.parking.entity.PmsDevice;
import com.sunjoy.parking.entity.PmsLaneDevice;
import com.sunjoy.parking.entity.PmsParkLane;
import com.sunjoy.parking.entity.PmsVehicleService;
import com.sunjoy.parking.enums.DirectionEnum;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parking.vo.VehiclePassage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 监听车辆入场信号，并处理车辆身份识别、通行鉴权、计费、放行等动作
 *
 * @author Habib
 * @date 2024/11/4
 */
@Slf4j
@Service
public class CarEntryParkListenter implements BaseMqttMessageListener {

    @Autowired
    private RedisService redisService;
    @Autowired
    private AccessRuleFactory accessRuleFactory;

    //@Log(title = "车辆入场", businessType = BusinessType.INSERT,operatorType = OperatorType.DEVICE)
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
            log.info("车辆入场消息:{}其他节点已处理，不处理！", playload.getUuid());
            return; // 已处理，直接返回
        }

        handle(playload);

    }

    /**
     * 处理车辆入场
     *
     * @param playload
     */
    private void handle(Playload playload) {
        //Long laneId = (Long) playload.getData().get("laneId");
        //身份确认
        VehiclePassage vehiclePassage = identify(playload);
        //通行规则
        List<IAccessRule> accessRules = accessRuleFactory.getRules(vehiclePassage.getPark().getParkId(), DirectionEnum.ENTRY);
        if (!isAccessAllowed(vehiclePassage, accessRules)) {
            //告知司机不能通行的原因
            notifyDriver(vehiclePassage);
            //处理完成
            return;
        }
        //收费处理
        if (billing(vehiclePassage)) {
            //如果产生费用，需要通知司机支付，并等待司机支付完才开闸
            notifyDriverToPay(vehiclePassage);
            //处理完成
            return;
        }
        //放行
        releaseVehicle(vehiclePassage);
        //记录入场信息
        saveVehicleEntryRecord(vehiclePassage);
    }

    /**
     * 开闸放行
     *
     * @param vehiclePassage
     */
    void releaseVehicle(VehiclePassage vehiclePassage) {
        log.info("{},车辆{}进入到{}车场成功！", DateUtils.getDate(), vehiclePassage.getLicensePlate(), vehiclePassage.getPark().getParkName());
    }

    /**
     * 计费
     * 调用计费服务计算应收费用
     *
     * @param vehiclePassage
     * @return
     */
    private boolean billing(VehiclePassage vehiclePassage) {
        log.info("车辆{}计费中....", vehiclePassage.getLicensePlate());
        return false;
    }

    /**
     * 保存车辆入场记录
     *
     * @param vehiclePassage
     */
    private void saveVehicleEntryRecord(VehiclePassage vehiclePassage) {

    }

    /**
     * 通知司机支付
     *
     * @param vehiclePassage
     */
    private void notifyDriverToPay(VehiclePassage vehiclePassage) {
        log.info("通知");
    }

    /**
     * 通知到通道的相应设备
     *
     * @param vehiclePassage
     */
    private void notifyDriver(VehiclePassage vehiclePassage) {
        //如果有
        if (vehiclePassage.getNotifyMethods() != null && vehiclePassage.getNotifyMethods().length > 0) {

        }
    }

    /**
     * 遍历相关规则，确定车辆是否适合放行
     *
     * @param vehiclePassage
     * @param accessRules
     * @return
     */
    private boolean isAccessAllowed(VehiclePassage vehiclePassage, List<IAccessRule> accessRules) {
        for (IAccessRule rule : accessRules) {
            //校验不通过，并且是禁止通行的，就不允许通行
            StringBuilder logString = new StringBuilder();
            logString.append("车辆").append(vehiclePassage.getLicensePlate());
            logString.append("在车场").append(vehiclePassage.getPark().getParkName());
            logString.append("校验规则:").append(rule.getRuleName());

            if (!rule.isAllowed(vehiclePassage) && rule.isForbidden()) {
                vehiclePassage.setNotifyMessage(rule.getNotifyMessage());
                vehiclePassage.setNotifyMethods(rule.getNotifyMethods());
                logString.append(",结果：校验不通过");
                log.warn(logString.toString());
                return false; // 一旦有规则不允许，返回 false
            }
            logString.append(",结果：校验通过!");

        }
        return true; // 所有规则均允许
    }

    /**
     * 识别车辆身份，并完善入场所需的相关信息
     *
     * @param playload
     * @return
     */
    private VehiclePassage identify(Playload playload) {
        VehiclePassage vehiclePassage = new VehiclePassage();

        BeanUtils.copyBeanProp(vehiclePassage, playload);
        //明确出入场方向
        vehiclePassage.setDirection(DirectionEnum.ENTRY.getValue());
        //完善通道、车场
        fillRelationData(vehiclePassage);
        //车辆身份确认:临时车、各种名单车、各种预付费车
        setVehicleIdentify(vehiclePassage);

        return vehiclePassage;
    }

    /**
     * 设置车辆身份
     *
     * @param vehiclePassage
     */
    private void setVehicleIdentify(VehiclePassage vehiclePassage) {
        //确认是否登记车辆
        List<PmsVehicleService> vehicleServices = this.redisService.getCacheList(RedisKeyConstants.PARK_VEHICLE_SERVICE);
        if (!vehicleServices.isEmpty()) {
            vehicleServices.stream().filter(item -> Objects.equals(item.getLicensePlate(), vehiclePassage.getLicensePlate())
                                                    && Objects.equals(item.getParkId(), vehiclePassage.getPark().getParkId()))
                    .max(Comparator.comparing(PmsVehicleService::getEndDate))
                    .ifPresent(item -> {
                        vehiclePassage.setVehicleService(item);
                    });
        }
    }

    /**
     * 填充入场需要的相关车辆模型信息
     *
     * @param vehiclePassage
     */
    private void fillRelationData(VehiclePassage vehiclePassage) {
        List<PmsLaneDevice> allDeviceLaneList = this.redisService.getCacheList(RedisKeyConstants.PARK_LANE_DEVICE);
        List<PmsLaneDevice> deviceLaneList = allDeviceLaneList.stream().filter(item -> Objects.equals(item.getDeviceId(), vehiclePassage.getDeviceId())).collect(Collectors.toList());
        if (!deviceLaneList.isEmpty()) {
            //默认取出第一个通道设备关系
            PmsLaneDevice laneDevice = deviceLaneList.get(0);

            vehiclePassage.setLaneId(deviceLaneList.get(0).getLaneId());
            //取出车场通道关系
            List<PmsParkLane> parkLaneList = this.redisService.getCacheList(RedisKeyConstants.PARK_LANE_REL);
            List<PmsParkLane> matchParkLaneList = parkLaneList.stream().filter(item -> Objects.equals(item.getLaneId(), laneDevice.getLaneId()) && Objects.equals(item.getParkId(), laneDevice.getParkId())).collect(Collectors.toList());
            if (!matchParkLaneList.isEmpty()) {
                vehiclePassage.setParkLane(matchParkLaneList.get(0));

            }
            //取出通道绑定的所有设备ID
            List<Long> deviceIds = allDeviceLaneList.stream()
                    .filter(device -> device.getLaneId().equals(vehiclePassage.getLaneId())) // 筛选 laneId = 2
                    .map(PmsLaneDevice::getDeviceId) // 提取 deviceId
                    .collect(Collectors.toList()); // 收集到列表中
            //根据ID找出所有设备
            List<PmsDevice> allDevices = redisService.getCacheList(RedisKeyConstants.PARK_DEVICE);
            List<PmsDevice> matchingDevices = allDevices.stream()
                    .filter(device -> deviceIds.contains(device.getDeviceId())) // 筛选匹配的设备
                    .collect(Collectors.toList()); // 收集到列表中
            vehiclePassage.setLaneDevices(matchingDevices);
        }
    }

    /**
     * 判断消息是否已被其他节点处理，如没有没，就放到缓存中，
     *
     * @param messageId
     * @return
     */
    private boolean processedMessageIds(String messageId) {
        String key = RedisKeyConstants.PARK_CAR_ENTRY + messageId;
        boolean processed = null != redisService.getCacheObject(key);
        if (!processed) {
            //缓存30秒
            redisService.setCacheObject(key, messageId, 20L, TimeUnit.SECONDS);
        }
        return processed;
    }

}