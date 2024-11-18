package com.sunjoy.parkctrl.rule.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.parkctrl.pojo.DetailParams;
import com.sunjoy.parkctrl.rule.IAccessRule;
import com.sunjoy.parkctrl.rule.IRuleMatcher;
import com.sunjoy.parking.entity.PmsParkLane;
import com.sunjoy.parking.enums.RuleApplyLevelEnum;
import com.sunjoy.parking.vo.VehiclePassage;
import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * 通道约速规则匹配器
 *
 * @author Habib
 * @date 2024/11/18
 */
@Slf4j
public class LaneMatcher implements IRuleMatcher {

    @Override
    public boolean match(VehiclePassage vehiclePassage, IAccessRule rule) {
        //限制级别
        if (rule.getRule().getLevel().equals(RuleApplyLevelEnum.LANE.getValue())) {
            //取出规则约束的通道列表
            List<Long> laneIds = getLaneIds(rule.getRule().getDetailParams());
            if (null != laneIds && !laneIds.isEmpty()) {
                return laneIds.stream().anyMatch(id -> Objects.equals(id, vehiclePassage.getLaneId()));
            }
        }
        return false;
    }

    private List<Long> getLaneIds(String detailParams) {
        try {
            String decodedJson = URLDecoder.decode(detailParams, StandardCharsets.UTF_8);

            // 2. 将解码后的字符串转换为 JSON 对象
            ObjectMapper objectMapper = new ObjectMapper();
            DetailParams params = objectMapper.readValue(decodedJson, DetailParams.class);
            List<Long> laneIds = params.getLaneList().stream().map(PmsParkLane::getLaneId).toList();
            return laneIds;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}