package com.sunjoy.system.controller;

import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.common.core.web.page.TableDataInfo;
import com.sunjoy.common.log.annotation.Log;
import com.sunjoy.common.log.enums.BusinessType;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.system.domain.SysTenant;
import com.sunjoy.system.domain.vo.TenantPojo;
import com.sunjoy.system.service.ISysRegionService;
import com.sunjoy.system.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @RequiresPermissions("system:tenant:add")
    @Log(title = "租户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysTenant tenant) {

        sysTenantService.addTenant(tenant);
        return toAjax(1);
    }


    /**
     * 获取租户信息
     *
     * @param tenantId
     * @return
     */
    @RequiresPermissions("system:tenant:view")
    @GetMapping("/{tenantId}")
    public AjaxResult getParkMaster(@PathVariable(value = "tenantId", required = true) Long tenantId) {

        AjaxResult ajax = AjaxResult.success();

        ajax.put(AjaxResult.DATA_TAG, this.sysTenantService.getTenantById(tenantId));

        return ajax;
    }

    @RequiresPermissions("system:tenant:auit")
    @Log(title = "租户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/audit")
    public AjaxResult audit(@Validated @RequestBody TenantPojo tenant) {

        sysTenantService.auditTenant(tenant.getSysTenant(), tenant.getMenuList());
        return toAjax(1);
    }
}