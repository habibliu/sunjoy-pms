package com.sunjoy.parkmodel.controller;


import com.sunjoy.common.core.constant.SecurityConstants;
import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.common.core.web.page.TableDataInfo;
import com.sunjoy.common.log.annotation.Log;
import com.sunjoy.common.log.enums.BusinessType;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.parking.entity.PmsPark;
import com.sunjoy.parkmodel.pojo.ParkPojo;
import com.sunjoy.parkmodel.service.IPmsLaneService;
import com.sunjoy.parkmodel.service.IPmsParkService;
import com.sunjoy.system.api.RemoteUserService;
import com.sunjoy.system.api.domain.SysDept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 车场基本资料前端控制器
 * </p>
 *
 * @author Habib
 * @since 2024-10-18
 */
@RestController
@RequestMapping("/park")
public class PmsParkController extends BaseController {

    @Autowired
    private IPmsParkService pmsParkService;
    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private IPmsLaneService pmsLaneService;

    @RequiresPermissions("parking:park:list")
    @GetMapping("/list")
    public TableDataInfo list(ParkPojo config) {
        startPage();
        List<PmsPark> list = pmsParkService.listParks(config);
        return getDataTable(list);
    }

    @RequiresPermissions("parking:park:add")
    @Log(title = "车场管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ParkPojo park) {

        return toAjax(pmsParkService.createPark(park.getPmsPark(), park.getLaneList(), park.getLaneDeviceList()));
    }

    /**
     * 获取部门树列表
     */
    @RequiresPermissions("parking:park:list")
    @GetMapping("/opuTree")
    public AjaxResult getOpuTree(SysDept dept) {
        AjaxResult result = this.remoteUserService.deptTree(dept, SecurityConstants.INNER);
        return success(result.get(AjaxResult.DATA_TAG));
    }

    /**
     * 获取车场信息
     *
     * @param parkId
     * @return
     */
    @RequiresPermissions("parking:park:list")
    @GetMapping("/{parkId}")
    public AjaxResult getParkMaster(@PathVariable(value = "parkId", required = true) Long parkId) {

        AjaxResult ajax = AjaxResult.success();

        ajax.put(AjaxResult.DATA_TAG, this.pmsParkService.getParkById(parkId));

        return ajax;
    }

    @RequiresPermissions("parking:park:update")
    @Log(title = "车场管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updatePark(@Validated @RequestBody ParkPojo park) {

        return toAjax(pmsParkService.updatePark(park.getPmsPark()));
    }


}