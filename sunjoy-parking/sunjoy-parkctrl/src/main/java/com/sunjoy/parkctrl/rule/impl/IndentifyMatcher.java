package com.sunjoy.parkctrl.rule.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.common.core.utils.DateUtils;
import com.sunjoy.common.core.utils.SpringUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.parkctrl.pojo.DetailParams;
import com.sunjoy.parkctrl.rule.IAccessRule;
import com.sunjoy.parkctrl.rule.IRuleMatcher;
import com.sunjoy.parking.entity.PmsVehicleService;
import com.sunjoy.parking.enums.VehicleIndentifyEnum;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parking.vo.VehiclePassage;
import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * 判断身份是否匹配
 *
 * @author Habib
 * @date 2024/11/18
 */
@Slf4j
public class IndentifyMatcher implements IRuleMatcher {

    @Override
    public boolean match(VehiclePassage vehiclePassage, IAccessRule rule) {
        String target = rule.getRule().getTarget();
        if (target.equals(VehicleIndentifyEnum.ALL.getCode())) {
            return true;
        }
        //临时车约束
        //取出车辆的服务信息，
        RedisService redisServicec = SpringUtils.getBean(RedisService.class);

        List<PmsVehicleService> vehicleServiceList = redisServicec.getCacheList(RedisKeyConstants.PARK_VEHICLE_SERVICE + vehiclePassage.getPark().getParkId());
        PmsVehicleService service = vehicleServiceList.stream().findFirst().orElse(null);
        //如果是管控临停车，并且车辆没有登记开通服务，即是临时车
        if (target.equals(VehicleIndentifyEnum.UnRegisted.getCode())) {
            return vehicleServiceList.isEmpty();
            //todo 如果服务过期，要判断过期的处理逻辑，如果有限时间内仍是月租车，即返回false, 否则就是true,但要设置过期时间，用于计费
        } else {//如果登记车约束
            //如果没有开通服务，即不是有效的登记车辆，返回false
            if (null == service) {
                return false;
            } else {
                //否则，判断开通的服务是否与规则匹配
                if (isMatchService(service, rule.getRule().getDetailParams())) {
                    //判断服务是否已经过期
                    return !DateUtils.isExpired(vehiclePassage.getTimestamp().getTimestamp(), service.getEndDate());

                    //todo 后面再加上如果有过期时限，即判断有没有宽限时间，有的话只要没有过宽限期，即仍是有效的
                } else {
                    return false;
                }
            }
        }

    }

    private boolean isMatchService(PmsVehicleService service, String detailParams) {
        try {
            String decodedJson = URLDecoder.decode(detailParams, StandardCharsets.UTF_8);

            // 2. 将解码后的字符串转换为 JSON 对象
            ObjectMapper objectMapper = new ObjectMapper();
            DetailParams params = objectMapper.readValue(decodedJson, DetailParams.class);
            if (null != params.getServiceList() && !params.getServiceList().isEmpty()) {
                return params.getServiceList().stream().anyMatch(item -> Objects.equals(item.getServiceId(), service.getServiceId()));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}