package com.sunjoy.parkctrl.rule.impl;

import com.sunjoy.common.core.enums.YesNoEnum;
import com.sunjoy.parkctrl.rule.IAccessRule;
import com.sunjoy.parkctrl.rule.IRuleMatcher;
import com.sunjoy.parking.vo.VehiclePassage;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/18
 */
public class TimeRangeMatcher implements IRuleMatcher {
    @Override
    public boolean match(VehiclePassage vehiclePassage, IAccessRule rule) {
        if (rule.getRule().getTimeRange().equals(YesNoEnum.No.getCode())) {
            Instant instant = vehiclePassage.getTimestamp().getTimestamp().toInstant();
            LocalTime checkTime = LocalTime.ofInstant(instant, ZoneId.systemDefault());
            //LocalTime checkTime=vehiclePassage.getTimestamp().getTimestamp().getTime();
            //todo 取出所有时段逐一匹配，有匹配的返回true
            List<TimeRangeCheck> timeRanges = getTimeRanges(rule.getRule().getDetailParams());
            for (TimeRangeCheck timeRangeCheck : timeRanges) {
                if (timeRangeCheck.isTimeInRange(checkTime)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<TimeRangeCheck> getTimeRanges(String detailParams) {
        List<TimeRangeCheck> list = new ArrayList<TimeRangeCheck>();
        //todo 解释detailParams中的timeList,并构造TimeRangeCheck列表
        return list;
    }
}

class TimeRangeCheck {
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeRangeCheck(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isTimeInRange(LocalTime time) {
        if (null == time) {
            return false;
        }
        if (startTime.isBefore(endTime)) {
            // 范围在同一天内
            return !time.isBefore(startTime) && !time.isAfter(endTime);
        } else {
            // 跨天范围
            return !time.isBefore(startTime) || !time.isAfter(endTime);
        }
    }
}