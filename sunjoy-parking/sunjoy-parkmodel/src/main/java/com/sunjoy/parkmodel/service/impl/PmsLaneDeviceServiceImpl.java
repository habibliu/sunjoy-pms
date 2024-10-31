package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parkmodel.entity.PmsLaneDevice;
import com.sunjoy.parkmodel.mapper.PmsLaneDeviceMapper;
import com.sunjoy.parkmodel.service.IPmsLaneDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/29
 */

@Service
public class PmsLaneDeviceServiceImpl implements IPmsLaneDeviceService {
    @Autowired
    private PmsLaneDeviceMapper laneDeviceMapper;

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