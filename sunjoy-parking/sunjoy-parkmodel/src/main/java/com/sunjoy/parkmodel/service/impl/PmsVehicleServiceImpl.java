package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.parking.entity.PmsVehicleService;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parkmodel.mapper.PmsVehicleServiceMapper;
import com.sunjoy.parkmodel.service.IPmsVehicleService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车辆服务实现类
 *
 * @author Habib
 * @date 2024/11/6
 */
@Service
public class PmsVehicleServiceImpl implements IPmsVehicleService {
    @Autowired
    private PmsVehicleServiceMapper pmsVehicleServiceMapper;

    @Autowired
    private RedisService redisService;

    @PostConstruct
    private void initCache() {
        List<PmsVehicleService> allVehicleServices = getAllVehicleService();
        if (!allVehicleServices.isEmpty()) {
            this.redisService.deleteObject(RedisKeyConstants.PARK_VEHICLE_SERVICE);
            this.redisService.setCacheList(RedisKeyConstants.PARK_VEHICLE_SERVICE, allVehicleServices);
        }
    }

    @Override
    public List<PmsVehicleService> getAllVehicleService() {
        return pmsVehicleServiceMapper.selectAll();
    }
}