package com.sunjoy.parkmodel.controller;

import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.common.core.web.page.TableDataInfo;
import com.sunjoy.common.log.annotation.Log;
import com.sunjoy.common.log.enums.BusinessType;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.parking.entity.PmsVehicle;
import com.sunjoy.parking.entity.PmsVehicleService;
import com.sunjoy.parkmodel.pojo.VehiclePojo;
import com.sunjoy.parkmodel.service.IPmsVehicleService;
import com.sunjoy.parkmodel.service.IPmsVehicleServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return getDataTable(list);
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

    @RequiresPermissions("parking:vehicle:update")
    @Log(title = "车辆登记-收费标准-批量新增", businessType = BusinessType.UPDATE)
    @PostMapping("/service")
    public AjaxResult batchSaveVehicleServices(@Validated @RequestBody List<PmsVehicleService> vehicleServices) {
        vehicleServices.forEach(vehicleService -> {
            //根据vehicleId找到车辆档案，补充服务的完整信息
            PmsVehicle vehicle = pmsVehicleService.getVehicleById(vehicleService.getVehicleId());
            vehicleService.setTenantId(vehicle.getTenantId());
            vehicleService.setLicensePlate(vehicle.getLicensePlate());
            vehicleService.setOwnerName(vehicle.getOwnerName());
            pmsVehicleServiceService.insertVehicleService(vehicleService);
        });

        return toAjax(1);
    }

    @RequiresPermissions("parking:vehicle:delete")
    @Log(title = "车辆登记-收费标准-删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/service/{vehicleId}/{serviceId}")
    public AjaxResult deleteVehicleService(@PathVariable Long vehicleId, @PathVariable Long serviceId) {

        //todo 要校验
        pmsVehicleServiceService.deleteVehicleService(vehicleId, serviceId);
        return toAjax(1);
    }

}