package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.parkmodel.entity.PmsDevice;
import com.sunjoy.parkmodel.mapper.PmsDeviceMapper;
import com.sunjoy.parkmodel.service.IPmsDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */
@Service
public class PmsDeviceServiceImpl implements IPmsDeviceService {

    @Autowired
    private PmsDeviceMapper pmsDeviceMapper;

    @Override
    public void addDevice(PmsDevice pmsDevice) {
        pmsDeviceMapper.insert(pmsDevice);
    }

    @Override
    public PmsDevice getDevice(Long deviceId) {
        return pmsDeviceMapper.selectById(deviceId);
    }

    @Override
    public List<PmsDevice> listDevices(PmsDevice pmsDevice) {
        return pmsDeviceMapper.selectPmsDevicesByCondition(pmsDevice);
    }

    @Override
    public void updateDevice(PmsDevice pmsDevice) {
        pmsDeviceMapper.update(pmsDevice);
    }

    @Override
    public void deleteDevice(Long deviceId) {
        pmsDeviceMapper.delete(deviceId);
    }
}