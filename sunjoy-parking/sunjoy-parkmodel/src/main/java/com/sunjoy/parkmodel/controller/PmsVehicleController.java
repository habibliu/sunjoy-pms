package com.sunjoy.parkmodel.controller;

import com.alibaba.druid.util.StringUtils;
import com.sunjoy.common.core.constant.SecurityConstants;
import com.sunjoy.common.core.domain.R;
import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.common.core.web.page.TableDataInfo;
import com.sunjoy.common.log.annotation.Log;
import com.sunjoy.common.log.enums.BusinessType;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsParkPrice;
import com.sunjoy.parking.entity.PmsParkService;
import com.sunjoy.parking.entity.PmsVehicle;
import com.sunjoy.parking.entity.PmsVehicleService;
import com.sunjoy.parking.enums.ParkOrderTypeEnum;
import com.sunjoy.parking.enums.ParkPaymentMethods;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parkmodel.pojo.VehiclePojo;
import com.sunjoy.parkmodel.service.IPmsParkPriceService;
import com.sunjoy.parkmodel.service.IPmsParkServiceService;
import com.sunjoy.parkmodel.service.IPmsVehicleService;
import com.sunjoy.parkmodel.service.IPmsVehicleServiceService;
import com.sunjoy.system.api.RemoteParkOperationService;
import com.sunjoy.system.api.domain.PmsParkOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private IPmsParkPriceService pmsParkPriceService;

    @Autowired
    private IPmsParkServiceService pmsParkServiceService;

    @Autowired
    private RemoteParkOperationService remoteParkOperationService;

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
    public AjaxResult getVehicle(@PathVariable(value = "vehicleId", required = true) Long vehicleId) {

        AjaxResult ajax = AjaxResult.success();

        ajax.put(AjaxResult.DATA_TAG, this.pmsVehicleService.getVehicleById(vehicleId));

        return ajax;
    }

    @RequiresPermissions("parking:vehicle:update")
    @Log(title = "车辆登记", businessType = BusinessType.UPDATE)
    @PutMapping("/{vehicleId}/{status}")
    public AjaxResult changeVehicleStatus(@Validated @PathVariable Long vehicleId, @Validated @PathVariable String status) {

        //todo 要校验
        PmsVehicle vehicle = this.pmsVehicleService.getVehicleById(vehicleId);
        vehicle.setStatus(status);
        this.pmsVehicleService.updateVehicle(vehicle, null);
        return toAjax(1);
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

    @RequiresPermissions("parking:vehicle:list")
    @GetMapping("/price/{parkId}/{serviceId}")
    public AjaxResult getPriceInfo(@PathVariable(value = "parkId", required = true) Long parkId, @PathVariable(value = "serviceId", required = true) Long serviceId) {
        // 先从车场的服务表中取出priceId
        PmsParkService condition = new PmsParkService();

        condition.setParkId(parkId);
        condition.setServiceId(serviceId);
        AjaxResult ajax = AjaxResult.success();
        PmsParkService parkService = this.pmsParkServiceService.listParkServices(condition).stream().findFirst().orElse(null);
        if (parkService != null) {
            PmsParkPrice parkPrice = this.pmsParkPriceService.getParkPrice(parkService.getPriceId());
            ajax.put(AjaxResult.DATA_TAG, parkPrice);
        }


        return ajax;
    }

    @RequiresPermissions("parking:vehicle:add")
    @Log(title = "车辆登记-服务充值", businessType = BusinessType.INSERT)
    @PostMapping("/service/order")
    public AjaxResult createVehicleServiceOrder(@Validated @RequestBody PmsParkOrder parkOrder) {

        //调用订单接口，完成停车订单创建，等待用户线上支付
        parkOrder.setOrderType(ParkOrderTypeEnum.Registed.getCode());
        parkOrder.setTenantId(SecurityUtils.getTenantId());
        parkOrder.setOpuId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
        parkOrder.setCreateBy(SecurityUtils.getUsername());
        parkOrder.setCreateTime(new Date());
        parkOrder.setDelFlag("0");
        R<Long> result = remoteParkOperationService.createRegistedVehicleServiceOrder(parkOrder, SecurityConstants.FROM_SOURCE);
        if (null == result.getData()) {
            return AjaxResult.error(result.getMsg());
        }
        Long orderId = result.getData();
        //todo 如果是现金支付，再调用支付订单，完成现金支付订单的创建，并关闭停车停单
        if (StringUtils.equals(ParkPaymentMethods.CASH.getCode(), parkOrder.getPaymentMethod())) {

        } else { //如果是在线支付或者银联支付，即将订单的id为key放进对应的token到缓存中,等待支付订单完成后，通道前端支付成功，继续后续的操作，如是否打印
            //保留10分钟，等待支付
            this.redisService.setCacheObject(RedisKeyConstants.PARK_REGISTED_VEHICLE_SERVER_ORDER_WAITING_FOR_PAYING + orderId, SecurityUtils.getToken(), 10L, TimeUnit.MINUTES);
        }
        return toAjax(1);
    }
}