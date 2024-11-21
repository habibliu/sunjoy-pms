package com.sunjoy.parkctrl.rule.impl;

import com.sunjoy.common.core.utils.SpringUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.parkctrl.rule.IAccessRule;
import com.sunjoy.parkctrl.rule.IRuleMatcher;
import com.sunjoy.parking.entity.PmsParkTransaction;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parking.vo.VehiclePassage;

import java.util.Map;

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
        Map<String, PmsParkTransaction> cacheMap = redisService.getCacheObject(RedisKeyConstants.PARK_VEHICLE_INSIDE_MAP + vehiclePassage.getPark().getParkId());
        return cacheMap != null && cacheMap.size() >= vehiclePassage.getPark().getTotalLots();
    }
}