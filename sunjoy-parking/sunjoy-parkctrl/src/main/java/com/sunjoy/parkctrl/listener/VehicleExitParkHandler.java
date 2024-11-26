package com.sunjoy.parkctrl.listener;

import com.alibaba.fastjson2.JSON;
import com.sunjoy.common.core.utils.SpringUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.parkctrl.rule.AccessRuleFactory;
import com.sunjoy.parkctrl.service.IBillingService;
import com.sunjoy.parkctrl.service.IPmsParkTransactionService;
import com.sunjoy.parking.entity.PmsParkTransaction;
import com.sunjoy.parking.enums.ReleaseModeEnum;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parking.vo.BillingDependency;
import com.sunjoy.parking.vo.BillingResult;
import com.sunjoy.parking.vo.VehiclePassage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/4
 */
@Slf4j
@Service
public class VehicleExitParkHandler extends BaseVehicleArrivedHandler {


    @Autowired
    private RedisService redisService;
    @Autowired
    private AccessRuleFactory accessRuleFactory;

    @Autowired
    private IPmsParkTransactionService pmsParkTransactionService;


    @Override
    RedisService getRedisService() {
        return this.redisService;
    }

    @Override
    AccessRuleFactory getAccessRuleFactory() {
        return this.accessRuleFactory;
    }


    @Override
    void saveVehicleTransctionRecord(VehiclePassage vehiclePassage) {
        PmsParkTransaction pmsParkTransaction = new PmsParkTransaction();
        PmsParkTransaction entryRecord = getEntryRecord(vehiclePassage.getPark().getParkId(), vehiclePassage.getLicensePlate());
        boolean insert = false;
        if (null != entryRecord) {
            //更新
            pmsParkTransaction.setTransId(entryRecord.getTransId());
            pmsParkTransaction.setUpdateBy("SYSTEM");
            pmsParkTransaction.setUpdateTime(new Date());
            //计算停留时间，按分钟计算
            long minutesDiff = Duration.between(entryRecord.getEntryTime(), vehiclePassage.getEventTime()).toMinutes();
            pmsParkTransaction.setParkingDuration(minutesDiff);
        } else {
            insert = true;
            //新增
            pmsParkTransaction.setParkId(vehiclePassage.getPark().getParkId());
            pmsParkTransaction.setParkName(vehiclePassage.getPark().getParkName());
            pmsParkTransaction.setTenantId(vehiclePassage.getPark().getTenantId());
            pmsParkTransaction.setOpuId(vehiclePassage.getPark().getOpuId());
            pmsParkTransaction.setCreateTime(new Date());
            pmsParkTransaction.setCreateBy("SYSTEM");

            pmsParkTransaction.setLicensePlate(vehiclePassage.getLicensePlate());
            pmsParkTransaction.setParkingDuration(0L);
            pmsParkTransaction.setDelFlag("0");
            pmsParkTransaction.setRemark("无入场记录");
        }
        //已出场
        pmsParkTransaction.setStatus("0");
        if (vehiclePassage.getVehicleService() != null) {
            pmsParkTransaction.setExitServiceId(vehiclePassage.getVehicleService().getServiceId());
            pmsParkTransaction.setVehicleId(vehiclePassage.getVehicleService().getVehicleId());
        }
        pmsParkTransaction.setExitRefId(vehiclePassage.getUuid());
        pmsParkTransaction.setExitLaneId(vehiclePassage.getParkLane().getLaneId());
        pmsParkTransaction.setExitDeviceId(vehiclePassage.getDeviceId());
        pmsParkTransaction.setExitRelMode(ReleaseModeEnum.NORMAL.getCode());
        pmsParkTransaction.setExitTime(vehiclePassage.getEventTime());
        pmsParkTransaction.setExitRelTime(vehiclePassage.getReleaseTime());
        pmsParkTransaction.setExitRefId(vehiclePassage.getUuid());
        pmsParkTransaction.setExitLaneName(vehiclePassage.getParkLane().getLaneName());
        //调用数据处理类完成数据库操作
        IPmsParkTransactionService transactionService = SpringUtils.getBean(IPmsParkTransactionService.class);
        if (insert) {
            transactionService.insertTransactionRecord(pmsParkTransaction);
        } else {
            transactionService.updateTransactionRecord(pmsParkTransaction);
        }

        //todo 更新场内车缓存
        Map<String, PmsParkTransaction> entryRecords = redisService.getCacheObject(RedisKeyConstants.PARK_VEHICLE_INSIDE_MAP + vehiclePassage.getPark().getParkId());
        if (entryRecords != null && entryRecords.containsKey(vehiclePassage.getLicensePlate())) {
            entryRecords.remove(vehiclePassage.getLicensePlate());
            redisService.setCacheObject(RedisKeyConstants.PARK_VEHICLE_INSIDE_MAP + vehiclePassage.getPark().getParkId(), entryRecords);
        }
    }

    @Override
    protected PmsParkTransaction getEntryRecord(Long parkId, String licensePlate) {
        PmsParkTransaction entryRecord = null;
        Map<String, PmsParkTransaction> entryRecords = redisService.getCacheObject(RedisKeyConstants.PARK_VEHICLE_INSIDE_MAP + parkId);
        if (entryRecords != null && entryRecords.containsKey(licensePlate)) {
            Object jsonString = entryRecords.get(licensePlate);
            entryRecord = JSON.parseObject(jsonString.toString(), PmsParkTransaction.class);
        } else {
            //从数据库中取
            entryRecord = pmsParkTransactionService.findEntryRecordWithoutExit(parkId, licensePlate);
            //如果非空，更新缓存
            if (entryRecords == null) {
                entryRecords = new HashMap<String, PmsParkTransaction>();
            }
            entryRecords.put(licensePlate, entryRecord);
            redisService.setCacheObject(RedisKeyConstants.PARK_VEHICLE_INSIDE_MAP + parkId, entryRecords);
        }

        return entryRecord;
    }

    @Override
    protected boolean billing(VehiclePassage vehiclePassage) {
        log.info("车辆{}计费中....", vehiclePassage.getLicensePlate());

        PmsParkTransaction entryRecord = this.getEntryRecord(vehiclePassage.getPark().getParkId(), vehiclePassage.getLicensePlate());
        if (null == entryRecord) {
            //todo 应该通知岗亭或者
            vehiclePassage.setNotifyMessage("无入场记录！");
            return false;
        }
        //构建计费对象
        BillingDependency dependency = new BillingDependency();
        dependency.setTenantId(entryRecord.getTenantId());
        dependency.setTransId(entryRecord.getTransId());
        dependency.setOpuId(entryRecord.getOpuId());
        if (null != vehiclePassage.getVehicleService()) {
            dependency.setServiceId(vehiclePassage.getVehicleService().getServiceId());
        } else {
            dependency.setServiceId(vehiclePassage.getParkService().getServiceId());
        }
        dependency.setParkId(vehiclePassage.getPark().getParkId());
        dependency.setParkName(vehiclePassage.getPark().getParkName());
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

}