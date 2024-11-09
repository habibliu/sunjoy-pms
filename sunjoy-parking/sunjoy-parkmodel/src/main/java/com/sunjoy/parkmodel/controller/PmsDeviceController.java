package com.sunjoy.parkmodel.controller;

import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.common.core.web.page.TableDataInfo;
import com.sunjoy.common.log.annotation.Log;
import com.sunjoy.common.log.enums.BusinessType;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.parking.entity.PmsDevice;
import com.sunjoy.parkmodel.service.IPmsDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */
@Slf4j
@RestController
@RequestMapping("/device")
public class PmsDeviceController extends BaseController {
    @Autowired
    private IPmsDeviceService pmsDeviceService;

    @RequiresPermissions("parking:device:list")
    @GetMapping("/list")
    public TableDataInfo list(PmsDevice device) {
        startPage();
        List<PmsDevice> list = pmsDeviceService.listDevices(device);
        return getDataTable(list);
    }

    @RequiresPermissions("parking:device:add")
    @Log(title = "设备管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody PmsDevice device) {

        long id = pmsDeviceService.addDevice(device);
        log.info("设备新增成功，编号为{}", id);
        return toAjax(1);
    }

    /**
     * 获取设备信息
     *
     * @param deviceId
     * @return
     */
    @RequiresPermissions("parking:device:list")
    @GetMapping("/{deviceId}")
    public AjaxResult getDeviceMaster(@PathVariable(value = "deviceId", required = true) Long deviceId) {

        AjaxResult ajax = AjaxResult.success();

        ajax.put(AjaxResult.DATA_TAG, this.pmsDeviceService.getDevice(deviceId));

        return ajax;
    }

    /**
     * 更新设备信息
     *
     * @param device
     * @return
     */
    @RequiresPermissions("parking:device:update")
    @Log(title = "设备管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateDevice(@Validated @RequestBody PmsDevice device) {
        pmsDeviceService.updateDevice(device);
        return toAjax(1);
    }
}