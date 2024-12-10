package com.sunjoy.parkctrl.controller;

import com.sunjoy.common.core.domain.R;
import com.sunjoy.common.log.annotation.Log;
import com.sunjoy.common.log.enums.BusinessType;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.parkctrl.service.IPmsParkOrderService;
import com.sunjoy.system.api.domain.PmsParkOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 车场订单控制类
 *
 * @author Habib
 * @date 2024/12/9
 */
@Slf4j
@RestController
@RequestMapping("/parkorder")
public class ParkOrderController {

    @Autowired
    private IPmsParkOrderService pmsParkOrderService;

    @RequiresPermissions("parking:parkorder:add")
    @Log(title = "登记车租用订单", businessType = BusinessType.INSERT)
    @PostMapping("/registed")
    public R<Long> createVehicleServiceOrder(@Validated @RequestBody PmsParkOrder parkOrder) {
        try {
            pmsParkOrderService.createParkOrder(parkOrder);
            log.info("车辆{}的预付费订单创建成功!", parkOrder.getLicensePlate());
            return R.ok(parkOrder.getOrderId());
        } catch (Exception e) {
            log.error(e.getMessage());
            return R.fail(e.getMessage());
        }

    }

}