package com.sunjoy.parkctrl.rule.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.common.core.enums.YesNoEnum;
import com.sunjoy.common.core.utils.DateUtils;
import com.sunjoy.parkctrl.pojo.DetailParams;
import com.sunjoy.parkctrl.rule.IAccessRule;
import com.sunjoy.parkctrl.rule.IRuleMatcher;
import com.sunjoy.parking.vo.VehiclePassage;
import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Iterator;

/**
 * 周期限制校验类
 *
 * @author Habib
 * @date 2024/11/18
 */
@Slf4j
public class DayRangeMatcher implements IRuleMatcher {
    @Override
    public boolean match(VehiclePassage vehiclePassage, IAccessRule rule) {
        if (rule.getRule().getDayRange().equals(YesNoEnum.Yes.getCode())) {
            //todo 规则周期包括当前车辆出入场时间点，就返回true
            DayOfWeek[] days = getDays(rule.getRule().getDetailParams());
            if (null != days) {
                Iterator<DayOfWeek> dayIterator = Arrays.stream(days).iterator();
                while (dayIterator.hasNext()) {
                    DayOfWeek day = dayIterator.next();

                    if (DateUtils.isSameDay(vehiclePassage.getEventTime(), day)) {
                        return true;
                    }
                }
            }


        }
        return false;
    }

    private DayOfWeek[] getDays(String detailParams) {
        try {
            String decodedJson = URLDecoder.decode(detailParams, StandardCharsets.UTF_8);

            // 2. 将解码后的字符串转换为 JSON 对象
            ObjectMapper objectMapper = new ObjectMapper();
            DetailParams params = objectMapper.readValue(decodedJson, DetailParams.class);
            DayOfWeek[] days = new DayOfWeek[params.getDayList().size()];
            params.getDayList().forEach(day -> {

            });
            return days;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}