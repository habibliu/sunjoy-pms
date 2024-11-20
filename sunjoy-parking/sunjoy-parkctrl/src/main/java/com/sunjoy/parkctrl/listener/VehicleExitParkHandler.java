package com.sunjoy.parkctrl.listener;

import com.sunjoy.common.core.utils.SpringUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.mqtt.domain.VehicleArrivedPayload;
import com.sunjoy.parkctrl.rule.AccessRuleFactory;
import com.sunjoy.parking.vo.VehiclePassage;
import lombok.extern.slf4j.Slf4j;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/4
 */
@Slf4j
public class VehicleExitParkHandler extends BaseVehicleArrivedHandler {


    private final RedisService redisService;

    private final AccessRuleFactory accessRuleFactory;

    private VehicleArrivedPayload payload;

    public VehicleExitParkHandler(VehicleArrivedPayload payload) {
        super(SpringUtils.getBean(RedisService.class), SpringUtils.getBean(AccessRuleFactory.class), payload);
        this.payload = payload;
        this.redisService = SpringUtils.getBean(RedisService.class);
        this.accessRuleFactory = SpringUtils.getBean(AccessRuleFactory.class);

    }


    @Override
    void saveVehicleTransctionRecord(VehiclePassage vehiclePassage) {

    }
}