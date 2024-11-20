package com.sunjoy.parkctrl.rule.impl;

import com.sunjoy.common.core.utils.DateUtils;
import com.sunjoy.common.core.utils.SpringUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.parkctrl.rule.IAccessRule;
import com.sunjoy.parkctrl.rule.IRuleMatcher;
import com.sunjoy.parking.entity.PmsParkService;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parking.vo.VehiclePassage;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/20
 */
public class ServiceExpiredMatcher implements IRuleMatcher {
    @Override
    public boolean match(VehiclePassage vehiclePassage, IAccessRule rule) {
        if (null != vehiclePassage.getVehicleService()) {
            LocalDateTime endDate = vehiclePassage.getVehicleService().getEndDate();
            LocalDateTime endOfToday = LocalDateTime.of(endDate.toLocalDate(), LocalTime.MAX);
            //取出服务的
            RedisService redisService = SpringUtils.getBean(RedisService.class);
            PmsParkService parkService = redisService.getCacheObject(RedisKeyConstants.PARK_SERVICE + vehiclePassage.getVehicleService().getServiceId());
            Integer expiredDuration = parkService.getExpiredDuration();
            endOfToday.plusDays(expiredDuration);
            return DateUtils.isExpired(vehiclePassage.getEventTime(), endOfToday);
        }
        return false;
    }
}