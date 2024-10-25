package com.sunjoy.parkmodel.controller;

import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.page.TableDataInfo;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.parkmodel.entity.PmsDevice;
import com.sunjoy.parkmodel.service.IPmsDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}