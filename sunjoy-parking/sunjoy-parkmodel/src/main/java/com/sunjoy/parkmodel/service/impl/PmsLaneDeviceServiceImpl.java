package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsLaneDevice;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parkmodel.mapper.PmsLaneDeviceMapper;
import com.sunjoy.parkmodel.service.IPmsLaneDeviceService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 通道设备服务类
 *
 * @author Habib
 * @date 2024/10/29
 */

@Service
public class PmsLaneDeviceServiceImpl implements IPmsLaneDeviceService {

    @Autowired
    private PmsLaneDeviceMapper laneDeviceMapper;

    @Autowired
    private RedisService redisService;

    @PostConstruct
    private void initCache() {
        List<PmsLaneDevice> allList = this.laneDeviceMapper.selectAll();
        if (!allList.isEmpty()) {
            this.redisService.deleteObject(RedisKeyConstants.PARK_LANE_DEVICE);
            this.redisService.setCacheList(RedisKeyConstants.PARK_LANE_DEVICE, allList);
        }
    }

    @Override
    public int addLaneDevice(PmsLaneDevice laneDevice) {
        laneDevice.setCreateBy(SecurityUtils.getUsername());
        laneDevice.setCreateTime(new Date());
        laneDevice.setUpdateBy(null);
        laneDevice.setUpdateTime(null);
        return laneDeviceMapper.insert(laneDevice);
    }

    @Override
    public PmsLaneDevice getLaneDeviceById(Long id) {
        return laneDeviceMapper.selectById(id);
    }

    @Override
    public int updateLaneDevice(PmsLaneDevice laneDevice) {
        laneDevice.setUpdateBy(SecurityUtils.getUsername());
        laneDevice.setUpdateTime(new Date());
        return laneDeviceMapper.update(laneDevice);
    }

    @Override
    public int deleteLaneDevice(Long id) {
        return laneDeviceMapper.delete(id);
    }

    @Override
    public List<PmsLaneDevice> getAllLaneDevices() {
        return laneDeviceMapper.selectAll();
    }

    @Override
    public int deleteLaneDeviceByParkIdAndLaneId(Long parkId, Long laneId) {
        return laneDeviceMapper.deleteByParkIdAndLaneId(parkId, laneId);
    }

    @Override
    public List<PmsLaneDevice> getLaneDevicesByParkId(Long parkId) {
        return laneDeviceMapper.selectByParkId(parkId);
    }

    @Override
    public int deleteLaneDevices(List<Long> ids) {
        return laneDeviceMapper.deleteBatch(ids);
    }
}