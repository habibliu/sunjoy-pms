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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequiresPermissions("parking:park:update")
    @Log(title = "车场管理-变更状态", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult enablePark(@Validated @RequestBody Map<String, Object> park) {
        PmsPark pmsPark = new PmsPark();
        pmsPark.setParkId(Long.parseLong(park.get("parkId").toString()));
        pmsPark.setStatus(park.get("status").toString());
        return toAjax(pmsParkService.updatePark(pmsPark));
    }

    /**
     * 获取车场树列表
     */
    @RequiresPermissions("parking:park:list")
    @GetMapping("/tree/{opuId}")
    public AjaxResult getParkTree(@PathVariable(value = "opuId", required = true) Long opuId) {
        ParkPojo condition = new ParkPojo();
        condition.setOpuId(opuId);
        List<PmsPark> opuParks = pmsParkService.listParks(condition);
        if (!opuParks.isEmpty()) {
            List<PmsPark> treeList = buildTree(opuParks);
            return success(treeList);
        }

        return success(opuParks);
    }


    private List<PmsPark> buildTree(List<PmsPark> parks) {
        Map<Long, PmsPark> parkMap = new HashMap<>();
        List<PmsPark> tree = new ArrayList<>();

        // 将所有节点放入 Map 中
        for (PmsPark park : parks) {
            parkMap.put(park.getParkId(), park);
        }

        // 构建树形结构
        for (PmsPark park : parks) {
            if (park.getParentId() == null) {
                // 如果没有父级 ID，说明是根节点
                tree.add(park);
            } else {
                // 如果有父级 ID，将其添加到对应的父节点的 children 列表中
                PmsPark parent = parkMap.get(park.getParentId());
                if (parent != null) {
                    parent.getChildren().add(park);
                }
            }
        }
        return tree;
    }


}