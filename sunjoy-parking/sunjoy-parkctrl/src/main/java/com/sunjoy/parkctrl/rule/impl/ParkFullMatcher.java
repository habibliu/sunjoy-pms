package com.sunjoy.parkctrl.rule.impl;

import com.sunjoy.common.core.utils.SpringUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.parkctrl.rule.IAccessRule;
import com.sunjoy.parkctrl.rule.IRuleMatcher;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parking.vo.VehiclePassage;

/**
 * 车场满位匹配逻辑
 *
 * @author Habib
 * @date 2024/11/20
 */
public class ParkFullMatcher implements IRuleMatcher {

    @Override
    public boolean match(VehiclePassage vehiclePassage, IAccessRule rule) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        Integer insideVehicles = redisService.getCacheObject(RedisKeyConstants.PARK_VEHICLE_INSIDE + vehiclePassage.getPark().getParkId());
        return insideVehicles != null && insideVehicles >= vehiclePassage.getPark().getTotalLots();
    }
}