package com.sunjoy.parkmodel.controller;

import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.common.log.annotation.Log;
import com.sunjoy.common.log.enums.BusinessType;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.parkmodel.entity.PmsDevice;
import com.sunjoy.parkmodel.entity.PmsLane;
import com.sunjoy.parkmodel.entity.PmsLaneDevice;
import com.sunjoy.parkmodel.entity.PmsParkLane;
import com.sunjoy.parkmodel.pojo.LaneDevicePojo;
import com.sunjoy.parkmodel.pojo.LanePojo;
import com.sunjoy.parkmodel.service.IPmsDeviceService;
import com.sunjoy.parkmodel.service.IPmsLaneDeviceService;
import com.sunjoy.parkmodel.service.IPmsLaneService;
import com.sunjoy.parkmodel.service.IPmsParkLaneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/29
 */
@Slf4j
@RestController
@RequestMapping("/lane")
public class PmsLaneController extends BaseController {

    @Autowired
    private IPmsLaneService pmsLaneService;

    @Autowired
    private IPmsParkLaneService pmsParkLaneService;
    @Autowired
    private IPmsLaneDeviceService pmsLaneDeviceService;
    @Autowired
    private IPmsDeviceService pmsDeviceService;

    /**
     * 新增车场通道
     *
     * @param lanePojo
     * @return
     */
    @RequiresPermissions("parking:lane:add")
    @Log(title = "通道管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody LanePojo lanePojo) {
        PmsLane lane = lanePojo.getPmsLane();

        long id = pmsLaneService.create(lane, lanePojo.getParkId(), lanePojo.getDirection());
        log.info("通道新增成功，编号为{}", id);
        return toAjax(1).put(AjaxResult.DATA_TAG, id);

    }

    /**
     * 更新通道信息
     *
     * @param lane
     * @return
     */
    @RequiresPermissions("parking:lane:update")
    @Log(title = "通道管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateDevice(@Validated @RequestBody LanePojo lanePojo) {
        PmsLane lane = lanePojo.getPmsLane();
        pmsLaneService.update(lane);
        return toAjax(1);
    }

    /**
     * 删除通道
     */
    @RequiresPermissions("parking:lane:remove")
    @Log(title = "通道管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{parkId}/{laneIds}")
    public AjaxResult remove(@PathVariable Long parkId, @PathVariable Long[] laneIds) {

        return toAjax(pmsLaneService.deleteParkLane(parkId, Arrays.stream(laneIds).toList()));
    }

    /**
     * 获取车场通道
     *
     * @param parkId
     * @return
     */
    //@RequiresPermissions("parking:park:list")
    @GetMapping("/list/{parkId}")
    public AjaxResult getParkLanes(@PathVariable(value = "parkId", required = true) Long parkId) {
        List<PmsParkLane> parkLanes = pmsParkLaneService.getParkLanes(parkId);
        if (!parkLanes.isEmpty()) {
            // 将列表转换为 Map，以 laneId 为键
            Map<Long, String> parkLaneMap = parkLanes.stream()
                    .collect(Collectors.toMap(PmsParkLane::getLaneId, PmsParkLane::getDirection));

            List<Long> laneIds = parkLanes.stream().map(PmsParkLane::getLaneId).collect(Collectors.toList());
            List<PmsLane> lanes = this.pmsLaneService.listByLaneIds(laneIds);
            if (!lanes.isEmpty()) {
                List<LanePojo> lanePojoList = new ArrayList<LanePojo>();
                lanes.forEach(item -> {
                    LanePojo pojo = new LanePojo();
                    BeanUtils.copyBeanProp(pojo, item);
                    pojo.setDirection(parkLaneMap.get(item.getLaneId()));
                    lanePojoList.add(pojo);
                });
                return success(lanePojoList);
            }

        }
        //返回空
        return success();
    }

    /**
     * 获取车场通道设备列表
     *
     * @param parkId
     * @return
     */
    @RequiresPermissions("parking:park:add")
    @GetMapping("/device/list/{parkId}")
    public AjaxResult getLaneDeviceList(@PathVariable(value = "parkId", required = true) Long parkId) {
        List<PmsLaneDevice> laneDeviceList = this.pmsLaneDeviceService.getLaneDevicesByParkId(parkId);
        if (laneDeviceList != null && !laneDeviceList.isEmpty()) {
            //取出车场设备列表
            List<Long> deviceIds = laneDeviceList.stream().map(PmsLaneDevice::getDeviceId).collect(Collectors.toList());
            //转成map,方法查询定位
            List<PmsDevice> deviceList = this.pmsDeviceService.listDevices(deviceIds);
            Map<Long, PmsDevice> deviceMap = deviceList.stream()
                    .collect(Collectors.toMap(PmsDevice::getDeviceId, device -> device));
            //取出车场通道列表
            List<Long> laneIds = laneDeviceList.stream().map(PmsLaneDevice::getLaneId).collect(Collectors.toList());
            List<PmsLane> laneList = this.pmsLaneService.listByLaneIds(laneIds);
            Map<Long, PmsLane> laneMap = laneList.stream()
                    .collect(Collectors.toMap(PmsLane::getLaneId, lane -> lane));
            //补充非PmsLaneDevice对象属性
            List<LaneDevicePojo> pojoList = new ArrayList<>();
            laneDeviceList.forEach(item -> {
                LaneDevicePojo pojo = new LaneDevicePojo();
                BeanUtils.copyBeanProp(pojo, item);
                //从device对象中复制属性
                if (deviceMap.containsKey(pojo.getDeviceId())) {
                    BeanUtils.copyBeanProp(pojo, deviceMap.get(pojo.getDeviceId()));
                }
                if (laneMap.containsKey(pojo.getLaneId())) {
                    PmsLane lane = laneMap.get(pojo.getLaneId());
                    pojo.setLaneName(lane.getLaneName());
                }
                pojoList.add(pojo);
            });
            return success(pojoList);
        }
        //返回空
        return success();
    }

    /**
     * 解除通道设备关系
     *
     * @param laneDeviceList
     * @return
     */
    @RequiresPermissions("parking:lane:add")
    @Log(title = "通道管理", businessType = BusinessType.INSERT)
    @PostMapping(path = "/device/bind")
    public AjaxResult unbindDevice(@Validated @RequestBody List<PmsLaneDevice> laneDeviceList) {

        return toAjax(pmsLaneService.saveLaneDevice(laneDeviceList));


    }

    /**
     * 保存通道设备关系
     *
     * @param laneDeviceList
     * @return
     */
    @RequiresPermissions("parking:lane:add")
    @Log(title = "通道管理", businessType = BusinessType.DELETE)
    @DeleteMapping(path = "/device/unbind/{ids}")
    public AjaxResult bindDevice(@PathVariable Long[] ids) {

        return toAjax(pmsLaneDeviceService.deleteLaneDevices(Arrays.stream(ids).toList()));


    }
}