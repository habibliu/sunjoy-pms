package com.sunjoy.parkmodel.controller;

import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.page.TableDataInfo;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.parking.entity.PmsVehicle;
import com.sunjoy.parking.entity.PmsVehicleService;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parkmodel.pojo.ParkServicePojo;
import com.sunjoy.parkmodel.pojo.VehiclePojo;
import com.sunjoy.parkmodel.service.IPmsVehicleService;
import com.sunjoy.parkmodel.service.IPmsVehicleServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 车辆档案信息控制类
 *
 * @author Habib
 * @date 2024/11/11
 */
@RestController
@RequestMapping("/vehicle")
public class PmsVehicleController extends BaseController {

    @Autowired
    private IPmsVehicleService pmsVehicleService;
    @Autowired
    private IPmsVehicleServiceService pmsVehicleServiceService;

    @Autowired
    private RedisService redisService;

    @RequiresPermissions("parking:vehicle:list")
    @GetMapping("/list")
    public TableDataInfo list(PmsVehicle config) {
        startPage();
        List<PmsVehicle> list = pmsVehicleService.getVehiclesByCondition(config);
        List<VehiclePojo> pojoList = new ArrayList<VehiclePojo>();
        if (!list.isEmpty()) {
            //取出所有车辆ID
            List<Long> vehicleIds = list.stream().map(PmsVehicle::getVehicleId).collect(Collectors.toList());
            List<PmsVehicleService> vehicleServices = pmsVehicleServiceService.getVehicleServiceByVehicleIds(vehicleIds);
            Map<Long, Long> vehicleIdToServiceId = vehicleServices.stream()
                    .collect(Collectors.toMap(PmsVehicleService::getVehicleId, PmsVehicleService::getServiceId));

            list.forEach(vehicle -> {
                VehiclePojo pojo = new VehiclePojo();
                BeanUtils.copyBeanProp(pojo, vehicle);
                Long serviceId = vehicleIdToServiceId.get(vehicle.getVehicleId());
                if (redisService.hasKey(RedisKeyConstants.PARK_SERVICE + serviceId)) {
                    ParkServicePojo parkService = (ParkServicePojo) redisService.getCacheObject(RedisKeyConstants.PARK_SERVICE + serviceId);
                    pojo.setServices(parkService.getPriceName());
                }
                pojoList.add(pojo);
            });
        }
        return getDataTable(pojoList);
    }
}