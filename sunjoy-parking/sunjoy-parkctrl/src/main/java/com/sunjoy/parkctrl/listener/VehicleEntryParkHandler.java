package com.sunjoy.parkctrl.listener;

import com.sunjoy.common.core.utils.SpringUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.parkctrl.rule.AccessRuleFactory;
import com.sunjoy.parkctrl.service.IPmsParkTransactionService;
import com.sunjoy.parking.entity.PmsParkTransaction;
import com.sunjoy.parking.vo.VehiclePassage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.sunjoy.parking.utils.RedisKeyConstants.PARK_VEHICLE_INSIDE_MAP;

/**
 * 监听车辆入场信号，并处理车辆身份识别、通行鉴权、计费、放行等动作
 *
 * @author Habib
 * @date 2024/11/4
 */

@Slf4j
@Service
public class VehicleEntryParkHandler extends BaseVehicleArrivedHandler {

    @Autowired
    private RedisService redisService;
    @Autowired
    private AccessRuleFactory accessRuleFactory;


    @Override
    RedisService getRedisService() {
        return this.redisService;
    }

    @Override
    AccessRuleFactory getAccessRuleFactory() {
        return this.accessRuleFactory;
    }


    /**
     * 保存车辆入场记录,成功后并缓存入场记录
     *
     * @param vehiclePassage
     */
    @Override
    void saveVehicleTransctionRecord(VehiclePassage vehiclePassage) {
        PmsParkTransaction pmsParkTransaction = new PmsParkTransaction();
        pmsParkTransaction.setParkId(vehiclePassage.getPark().getParkId());
        pmsParkTransaction.setParkName(vehiclePassage.getPark().getParkName());
        pmsParkTransaction.setTenantId(vehiclePassage.getPark().getTenantId());
        pmsParkTransaction.setOpuId(vehiclePassage.getPark().getOpuId());
        pmsParkTransaction.setDelFlag("0");
        pmsParkTransaction.setStatus("1");
        pmsParkTransaction.setEntryLaneId(vehiclePassage.getLaneId());
        pmsParkTransaction.setEntryLaneName(vehiclePassage.getParkLane().getLaneName());
        pmsParkTransaction.setEntryDeviceId(vehiclePassage.getDeviceId());
        pmsParkTransaction.setEntryTime(vehiclePassage.getEventTime());
        pmsParkTransaction.setEntryRelMode(vehiclePassage.getReleaseMode());
        pmsParkTransaction.setEntryRelTime(vehiclePassage.getReleaseTime());
        pmsParkTransaction.setLicensePlate(vehiclePassage.getLicensePlate());

        pmsParkTransaction.setEntryRefId(vehiclePassage.getUuid());
        if (null != vehiclePassage.getVehicleService()) {
            pmsParkTransaction.setEntryServiceId(vehiclePassage.getVehicleService().getServiceId());
            pmsParkTransaction.setVehicleId(vehiclePassage.getVehicleService().getVehicleId());
        }
        IPmsParkTransactionService transactionService = SpringUtils.getBean(IPmsParkTransactionService.class);
        transactionService.insertTransactionRecord(pmsParkTransaction);
        //缓存入场记录
        String cacheKey = PARK_VEHICLE_INSIDE_MAP + vehiclePassage.getPark().getParkId();
        Map<String, PmsParkTransaction> entryRecords = redisService.getCacheObject(cacheKey);
        if (null == entryRecords) {
            entryRecords = new HashMap<String, PmsParkTransaction>();
        }
        entryRecords.put(pmsParkTransaction.getLicensePlate(), pmsParkTransaction);
        redisService.setCacheObject(cacheKey, entryRecords);
    }

    @Override
    protected PmsParkTransaction getEntryRecord(Long parkId, String licensePlate) {
        return null;
    }
}