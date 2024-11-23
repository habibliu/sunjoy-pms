package com.sunjoy.parkctrl.listener;

import com.sunjoy.common.core.enums.YesNoEnum;
import com.sunjoy.common.core.exception.CheckedException;
import com.sunjoy.common.core.utils.DateUtils;
import com.sunjoy.common.core.utils.SpringUtils;
import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.mqtt.domain.Payload;
import com.sunjoy.mqtt.domain.VehicleArrivedPayload;
import com.sunjoy.parkctrl.rule.AccessRuleFactory;
import com.sunjoy.parkctrl.rule.IAccessRule;
import com.sunjoy.parkctrl.service.IBillingService;
import com.sunjoy.parking.entity.*;
import com.sunjoy.parking.enums.DirectionEnum;
import com.sunjoy.parking.enums.ReleaseModeEnum;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parking.vo.BillingDependency;
import com.sunjoy.parking.vo.BillingResult;
import com.sunjoy.parking.vo.VehiclePassage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/20
 */
@Slf4j
abstract public class BaseVehicleArrivedHandler implements Runnable {


    protected VehicleArrivedPayload payload;

    abstract RedisService getRedisService();

    abstract AccessRuleFactory getAccessRuleFactory();


    /**
     * 保存出入场流水记录
     * 由子类实现
     *
     * @param vehiclePassage
     */
    abstract void saveVehicleTransctionRecord(VehiclePassage vehiclePassage);

    abstract protected PmsParkTransaction getEntryRecord(Long parkId, String licensePlate);

    protected void setPayLoad(VehicleArrivedPayload payload) {
        this.payload = payload;
    }

    @Transactional
    @Override
    public void run() {

        handle();

    }


    protected void handle() {
        long start = System.currentTimeMillis();

        if (processingMessageIds(this.payload.getUuid())) {
            log.warn("其他服务正在处理{}，退出处理!", payload.toString());
            return;
        }
        String direction = DirectionEnum.fromValue(this.payload.getDirection()).getDescription();
        log.info("请在处理车辆{}的{}事件....", this.payload.getLicensePlate(), direction);
        //身份确认
        VehiclePassage vehiclePassage = identify(payload);

        log.info("完成车辆{}身份确认:{} 耗时:{}毫秒", payload.getLicensePlate(), vehiclePassage.getVehicleService() != null ? vehiclePassage.getVehicleService().getServiceName() : "临停车", System.currentTimeMillis() - start);
        //通行规则

        if (!isAccessAllowed(vehiclePassage)) {
            log.warn("通行规则校验完毕，不通过! 耗时:{}毫秒", System.currentTimeMillis() - start);
            //告知司机不能通行的原因
            notifyDriver(vehiclePassage);
            //处理完成
            return;
        }
        log.warn("通行规则校验完毕，通过! 耗时:{}毫秒", System.currentTimeMillis() - start);
        //收费处理,要通道设置要收费放行才计费
        if (vehiclePassage.getParkLane().getRap().equals(YesNoEnum.Yes.getCode())) {
            if (billing(vehiclePassage)) {
                log.info("计费完毕，本次停车时长：{}小时{}分钟, 总费用：{},耗时:{}毫秒", vehiclePassage.getStayDuration() / 60, vehiclePassage.getStayDuration() % 60, vehiclePassage.getParkingFee(), System.currentTimeMillis() - start);
                BillingResult billingResult = vehiclePassage.getBillingResult();
                log.info(billingResult.getBillingResultDescription());
                //如果产生费用，需要通知司机支付，并等待司机支付完才开闸
                notifyDriverToPay(vehiclePassage);
                log.info("已通知车主缴费！,耗时:{}毫秒", System.currentTimeMillis() - start);
                //处理完成
                return;
            } else {
                log.warn("车辆{}计费失败，原因:{}", vehiclePassage.getLicensePlate(), vehiclePassage.getNotifyMessage());
                //todo 计费失败，比如没有入场记录等，要通知相关人事处理，或者根据系统规则是否自动放行，如是要这样做，建议放到规则上处理,针对无入场记录的规则处理
                return;
            }

        }
        log.warn("{}无需缴费! 耗时:{}毫秒", vehiclePassage.getParkLane().getRap().equals(YesNoEnum.Yes.getCode()) ? "计费完毕，" : "", System.currentTimeMillis() - start);
        //放行
        releaseVehicle(vehiclePassage);
        log.info("车辆{}放行{}，车场：{}，耗时:{}毫秒", vehiclePassage.getLicensePlate(), direction, vehiclePassage.getPark().getParkName(), System.currentTimeMillis() - start);
        //记录出场信息,异步处理
        saveVehicleTransctionRecord(vehiclePassage);
        long end = System.currentTimeMillis();
        log.info("数据记录保存完成，车辆{}{}处理完毕,总耗时:{}毫秒！", vehiclePassage.getLicensePlate(), direction, System.currentTimeMillis() - start);
    }

    /**
     * 识别车辆身份，并完善出入场所需的相关信息
     *
     * @param payload
     * @return
     */
    protected VehiclePassage identify(Payload payload) {
        VehiclePassage vehiclePassage = new VehiclePassage();

        BeanUtils.copyBeanProp(vehiclePassage, payload);
        //明确出入场方向
        vehiclePassage.setEventTime(payload.getEventTime());
        //完善通道、车场
        fillRelationData(vehiclePassage);
        //车辆身份确认:临时车、各种名单车、各种预付费车
        setVehicleIdentify(vehiclePassage);
        vehiclePassage.setDirection(vehiclePassage.getParkLane().getDirection());
        return vehiclePassage;
    }

    /**
     * 设置车辆身份,即收费标准
     *
     * @param vehiclePassage 车辆通行信息载体类
     */
    private void setVehicleIdentify(VehiclePassage vehiclePassage) {
        //确认是否登记车辆
        List<PmsVehicleService> vehicleServices = this.getRedisService().getCacheList(RedisKeyConstants.PARK_VEHICLE_SERVICE + vehiclePassage.getPark().getParkId());
        if (!vehicleServices.isEmpty()) {
            vehicleServices.stream().filter(item -> Objects.equals(item.getLicensePlate(), vehiclePassage.getLicensePlate())
                                                    && Objects.equals(item.getParkId(), vehiclePassage.getPark().getParkId()))
                    .max(Comparator.comparing(PmsVehicleService::getEndDate))
                    .ifPresent(vehiclePassage::setVehicleService);
        } else {//临停车，取车场的默认的临停车收费标准
            List<PmsParkService> parkServiceList = getRedisService().getCacheObject(RedisKeyConstants.PARK_SERVICE + vehiclePassage.getPark().getParkId());
            if (!parkServiceList.isEmpty()) {
                PmsParkService parkService = parkServiceList.stream().filter(item -> YesNoEnum.Yes.getCode().equals(item.getDefaultUnregisted())).findFirst().orElse(null);
                if (null != parkService) {
                    vehiclePassage.setParkService(parkService);
                }
            }
        }
    }

    /**
     * 填充入场需要的相关车辆模型信息
     *
     * @param vehiclePassage
     */
    private void fillRelationData(VehiclePassage vehiclePassage) {
        List<PmsLaneDevice> allDeviceLaneList = this.getRedisService().getCacheList(RedisKeyConstants.PARK_LANE_DEVICE);
        PmsLaneDevice laneDevice = allDeviceLaneList.stream().filter(item -> Objects.equals(item.getDeviceId(), vehiclePassage.getDeviceId())).findFirst().orElse(null);
        if (null != laneDevice) {
            //默认取出第一个通道设备关系

            vehiclePassage.setLaneId(laneDevice.getLaneId());
            //取出车场通道关系,可能有2条记录，因为通道可以绑定2个车场, 当前是入场，找入场方向的记录
            List<PmsParkLane> parkLaneList = this.getRedisService().getCacheList(RedisKeyConstants.PARK_LANE_REL);
            PmsParkLane matchPmsParkLane = parkLaneList.stream().filter(item -> Objects.equals(item.getLaneId(), laneDevice.getLaneId())
                                                                                && Objects.equals(item.getParkId(), laneDevice.getParkId())).findFirst().orElse(null);
            if (matchPmsParkLane == null) {
                throw new CheckedException("通道[" + laneDevice.getLaneId() + "],车场[" + laneDevice.getParkId() + "]没有匹配的车场通道关系!");
            }
            vehiclePassage.setParkLane(matchPmsParkLane);
            //设置车场
            PmsPark matchPark = this.getRedisService().getCacheObject(RedisKeyConstants.PARK_INFO + matchPmsParkLane.getParkId());
            if (matchPark == null) {
                throw new CheckedException("通道[" + laneDevice.getLaneId() + "],车场[" + laneDevice.getParkId() + "]没有匹配的车场通道关系!");
            }
            vehiclePassage.setPark(matchPark);
            //取出通道绑定的所有设备ID
            List<Long> deviceIds = allDeviceLaneList.stream()
                    .filter(device -> device.getLaneId().equals(vehiclePassage.getLaneId()))
                    .map(PmsLaneDevice::getDeviceId)
                    .toList();
            //根据ID找出所有设备
            List<PmsDevice> allDevices = getRedisService().getCacheList(RedisKeyConstants.PARK_DEVICE);
            List<PmsDevice> matchingDevices = allDevices.stream()
                    .filter(device -> deviceIds.contains(device.getDeviceId()))
                    .collect(Collectors.toList());
            vehiclePassage.setLaneDevices(matchingDevices);
        }
    }

    /**
     * 开闸放行
     *
     * @param vehiclePassage
     */
    protected void releaseVehicle(VehiclePassage vehiclePassage) {
        vehiclePassage.setReleaseMode(ReleaseModeEnum.NORMAL.getCode());
        vehiclePassage.setReleaseTime(LocalDateTime.now());
        log.info("{},车辆放行成功！", DateUtils.getDate(), vehiclePassage.getLicensePlate());
        //todo 实现逻辑
    }

    /**
     * 计费
     * 调用计费服务计算应收费用，暂时只考虑出场计费，入场计费在入场类中override实现
     *
     * @param vehiclePassage
     * @return
     */
    protected boolean billing(VehiclePassage vehiclePassage) {
        log.info("车辆{}计费中....", vehiclePassage.getLicensePlate());
        //如果是出场，取出入场记录
        if (vehiclePassage.getDirection().equals(DirectionEnum.EXIT.getValue())) {
            PmsParkTransaction entryRecord = this.getEntryRecord(vehiclePassage.getPark().getParkId(), vehiclePassage.getLicensePlate());
            if (null == entryRecord) {
                //todo 应该通知岗亭或者
                vehiclePassage.setNotifyMessage("无入场记录！");
                return false;
            }
            //构建计费对象
            BillingDependency dependency = new BillingDependency();
            if (null != vehiclePassage.getVehicleService()) {
                dependency.setServiceId(vehiclePassage.getVehicleService().getServiceId());
            } else {
                dependency.setServiceId(vehiclePassage.getParkService().getServiceId());
            }
            dependency.setParkId(vehiclePassage.getPark().getParkId());
            dependency.setLicensePlate(vehiclePassage.getLicensePlate());
            dependency.setStartTime(entryRecord.getEntryTime());
            dependency.setEndTime(vehiclePassage.getEventTime());
            //调用计费服务
            IBillingService billingService = SpringUtils.getBean(IBillingService.class);
            BillingResult billingResult = billingService.calculate(dependency);
            //设置计费结果
            vehiclePassage.setStayDuration(billingResult.getBillingDuration());
            vehiclePassage.setBillingResult(billingResult);
            vehiclePassage.setParkingFee(billingResult.getRealAmount().floatValue());
            return true;
        }
        return false;
    }

    /**
     * 通知司机支付
     *
     * @param vehiclePassage
     */
    protected void notifyDriverToPay(VehiclePassage vehiclePassage) {
        log.info("通知");
        //todo 实现逻辑
    }

    /**
     * 通知到通道的相应设备
     *
     * @param vehiclePassage
     */
    protected void notifyDriver(VehiclePassage vehiclePassage) {
        //todo 实现逻辑
        //如果有
        if (vehiclePassage.getNotifyMethods() != null && vehiclePassage.getNotifyMethods().length > 0) {

        }
    }

    /**
     * 判断消息是否已被其他节点处理，如没有没，就放到缓存中，
     *
     * @param messageId
     * @return
     */
    protected boolean processingMessageIds(String messageId) {
        String key = RedisKeyConstants.PARK_VEHICLE_ARRIVED + messageId;
        boolean processed = null != getRedisService().getCacheObject(key);
        if (!processed) {
            //缓存30秒
            getRedisService().setCacheObject(key, messageId, 20L, TimeUnit.SECONDS);
        }
        return processed;
    }

    /**
     * 遍历相关规则，确定车辆是否适合放行
     *
     * @param vehiclePassage
     * @return
     */
    protected boolean isAccessAllowed(VehiclePassage vehiclePassage) {
        List<IAccessRule> accessRules = getAccessRuleFactory().getRules(vehiclePassage.getPark().getParkId(), DirectionEnum.fromValue(vehiclePassage.getDirection()));
        log.info("获取到车场{}的{}规则有{}条！", vehiclePassage.getPark().getParkName(), vehiclePassage.getDirection().equals(DirectionEnum.ENTRY.getValue()) ? "入场" : "出场", accessRules.size());
        for (IAccessRule rule : accessRules) {
            //校验不通过，并且是禁止通行的，就不允许通行
            StringBuilder logString = new StringBuilder();
            logString.append("车辆").append(vehiclePassage.getLicensePlate());
            logString.append("在车场").append(vehiclePassage.getPark().getParkName());
            logString.append("校验规则:").append(rule.getRuleName());

            if (!rule.isAllowed(vehiclePassage) && rule.isForbidden()) {
                vehiclePassage.setNotifyMessage(rule.getNotifyMessage());
                vehiclePassage.setNotifyMethods(rule.getNotifyMethods());
                logString.append(",校验结果：校验不通过");
                log.warn(logString.toString());
                // 一旦有规则不允许，返回 false
                return false;
            }
            logString.append(",校验结果：校验通过!");
            log.info(logString.toString());

        }
        // 所有规则均允许
        return true;
    }

}