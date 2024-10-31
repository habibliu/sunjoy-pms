package com.sunjoy.system.controller;

import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.common.core.web.page.TableDataInfo;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.system.domain.SysTenant;
import com.sunjoy.system.service.ISysRegionService;
import com.sunjoy.system.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/30
 */
@RestController
@RequestMapping("/tenant")
public class SysTenantController extends BaseController {

    @Autowired
    private ISysTenantService sysTenantService;

    @Autowired
    private ISysRegionService sysRegionService;

    /**
     * 获取租户列表列表
     */
    @RequiresPermissions("system:tenant:list")
    @GetMapping("/list")
    public TableDataInfo list(SysTenant tenant) {
        startPage();
        List<SysTenant> list = sysTenantService.listTenants(tenant);
        return getDataTable(list);
    }

    /**
     * 获取区域树列表
     */
    //@RequiresPermissions("system:tenant:list")
    @GetMapping("/regionTree")
    public AjaxResult regionTree() {
        return success(sysRegionService.selectRegionTreeList());
    }
}