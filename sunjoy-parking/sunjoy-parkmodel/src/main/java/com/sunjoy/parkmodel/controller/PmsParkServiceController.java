package com.sunjoy.parkmodel.controller;

import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.common.log.annotation.Log;
import com.sunjoy.common.log.enums.BusinessType;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.parking.entity.PmsParkService;
import com.sunjoy.parkmodel.pojo.ParkServicePojo;
import com.sunjoy.parkmodel.service.IPmsParkServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车场服务控制类
 *
 * @author Habib
 * @date 2024/11/8
 */
@RestController
@RequestMapping("/service")
public class PmsParkServiceController extends BaseController {

    @Autowired
    private IPmsParkServiceService pmsParkServiceService;

    /**
     * 查询车场服务
     */
    @RequiresPermissions("parking:service:query")
    @GetMapping(value = "/{parkId}/{status}")
    public AjaxResult getParkServiceWithStatus(@PathVariable Long parkId, @PathVariable String status) {
        PmsParkService condition = new PmsParkService();
        condition.setParkId(parkId);

        condition.setStatus(status);

        List<ParkServicePojo> results = pmsParkServiceService.listParkServices(condition);
        return success(results);
    }

    /**
     * 查询车场服务
     */
    @RequiresPermissions("parking:service:query")
    @GetMapping(value = "/{parkId}")
    public AjaxResult getParkService(@PathVariable Long parkId) {
        PmsParkService condition = new PmsParkService();
        condition.setParkId(parkId);

        List<ParkServicePojo> results = pmsParkServiceService.listParkServices(condition);
        return success(results);
    }

    @RequiresPermissions("parking:service:add")
    @Log(title = "车场服务", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult batchAdd(@Validated @RequestBody List<PmsParkService> pmsParkServices) {
        pmsParkServices.forEach(servcie -> {
            pmsParkServiceService.addParkService(servcie);
        });

        return toAjax(pmsParkServices.size());
    }

    @RequiresPermissions("parking:service:update")
    @Log(title = "车场服务-变更状态", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{serviceId}")
    public AjaxResult enableParkService(@PathVariable Long serviceId) {
        pmsParkServiceService.enableParkService(serviceId);
        return toAjax(1);
    }


}