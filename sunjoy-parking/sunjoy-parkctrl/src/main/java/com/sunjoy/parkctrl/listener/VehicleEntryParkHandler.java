package com.sunjoy.parkctrl.listener;

import com.sunjoy.common.core.utils.SpringUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.mqtt.domain.VehicleArrivedPayload;
import com.sunjoy.parkctrl.rule.AccessRuleFactory;
import com.sunjoy.parking.vo.VehiclePassage;
import lombok.extern.slf4j.Slf4j;

/**
 * 监听车辆入场信号，并处理车辆身份识别、通行鉴权、计费、放行等动作
 *
 * @author Habib
 * @date 2024/11/4
 */
@Slf4j

public class VehicleEntryParkHandler extends BaseVehicleArrivedHandler {


    private final RedisService redisService;

    private final AccessRuleFactory accessRuleFactory;

    private final VehicleArrivedPayload payload;

    public VehicleEntryParkHandler(VehicleArrivedPayload payload) {
        super(SpringUtils.getBean(RedisService.class), SpringUtils.getBean(AccessRuleFactory.class), payload);
        this.payload = payload;
        this.redisService = SpringUtils.getBean(RedisService.class);
        this.accessRuleFactory = SpringUtils.getBean(AccessRuleFactory.class);

    }


    /**
     * 保存车辆入场记录
     *
     * @param vehiclePassage
     */

    @Override
    void saveVehicleTransctionRecord(VehiclePassage vehiclePassage) {
        //todo 实现逻辑
    }
}