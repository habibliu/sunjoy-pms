package com.sunjoy.parkmodel.controller;

import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.common.core.web.page.TableDataInfo;
import com.sunjoy.common.log.annotation.Log;
import com.sunjoy.common.log.enums.BusinessType;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
            //从数据库取出与当前所有车辆有关的车辆收费服务
            List<PmsVehicleService> vehicleServices = pmsVehicleServiceService.getVehicleServiceByVehicleIds(vehicleIds);
            //按车辆分组，形成map结构，方便定位
            Map<Long, Long> vehicleIdToServiceId = vehicleServices.stream()
                    .collect(Collectors.toMap(PmsVehicleService::getVehicleId, PmsVehicleService::getServiceId));

            list.forEach(vehicle -> {
                VehiclePojo pojo = new VehiclePojo();
                BeanUtils.copyBeanProp(pojo, vehicle);
                //设置服务套餐
                Long serviceId = vehicleIdToServiceId.get(vehicle.getVehicleId());
                if (redisService.hasKey(RedisKeyConstants.PARK_SERVICE + serviceId)) {
                    ParkServicePojo parkService = (ParkServicePojo) redisService.getCacheObject(RedisKeyConstants.PARK_SERVICE + serviceId);
                    pojo.setServices(parkService.getPriceName());
                }
                PmsVehicleService vehicleService = vehicleServices.stream().filter(item -> Objects.equals(item.getVehicleId(), vehicle.getVehicleId()) && Objects.equals(item.getServiceId(), serviceId)).findFirst().orElse(null);
                if (vehicleService != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(vehicleService.getParkName());
                    if (null != vehicleService.getLotNos()) {
                        sb.append("/").append(vehicleService.getLotNos());
                    }
                    pojo.setLots(sb.toString());
                }
                //设置车位信息

                pojoList.add(pojo);
            });
        }
        return getDataTable(pojoList);
    }

    @RequiresPermissions("parking:vehicle:add")
    @Log(title = "车辆登记", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addVehicle(@Validated @RequestBody VehiclePojo vehiclePojo) {
        pmsVehicleService.createVehicle(vehiclePojo.getVehicle(), vehiclePojo.getVehicleServiceList());
        return toAjax(1);
    }

    @RequiresPermissions("parking:vehicle:update")
    @Log(title = "车辆登记", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateVehicle(@Validated @RequestBody VehiclePojo vehiclePojo) {
        pmsVehicleService.updateVehicle(vehiclePojo.getVehicle(), vehiclePojo.getVehicleServiceList());
        return toAjax(1);
    }

    /**
     * 获取车辆信息
     *
     * @param vehicleId
     * @return
     */
    @RequiresPermissions("parking:vehicle:list")
    @GetMapping("/{vehicleId}")
    public AjaxResult getParkMaster(@PathVariable(value = "vehicleId", required = true) Long vehicleId) {

        AjaxResult ajax = AjaxResult.success();

        ajax.put(AjaxResult.DATA_TAG, this.pmsVehicleService.getVehicleById(vehicleId));

        return ajax;
    }

    /**
     * 查询车辆服务
     */
    @RequiresPermissions("parking:vehicle:list")
    @GetMapping(value = "/service/{vechicleId}")
    public AjaxResult getVehicleServices(@PathVariable Long vechicleId) {

        List<PmsVehicleService> results = pmsVehicleServiceService.getVehicleServiceByVehicleId(vechicleId);
        return success(results);
    }

    @RequiresPermissions("parking:vehicle:update")
    @Log(title = "车辆登记-收费标准-变更状态", businessType = BusinessType.UPDATE)
    @PutMapping("/service")
    public AjaxResult changeVehicleServiceStatus(@Validated @RequestBody PmsVehicleService vehicleService) {

        //todo 要校验
        pmsVehicleServiceService.updateVehicleService(vehicleService);
        return toAjax(1);
    }
}