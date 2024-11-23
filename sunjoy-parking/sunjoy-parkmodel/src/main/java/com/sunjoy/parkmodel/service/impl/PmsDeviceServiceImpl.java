package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsDevice;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parkmodel.mapper.PmsDeviceMapper;
import com.sunjoy.parkmodel.service.IPmsDeviceService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 设备管理服务
 *
 * @author Habib
 * @date 2024/10/25
 */
@Service
public class PmsDeviceServiceImpl implements IPmsDeviceService {

    @Autowired
    private PmsDeviceMapper pmsDeviceMapper;

    @Autowired
    private RedisService redisService;
    @Autowired
    private ThreadPoolTaskExecutor parkingModelTaskExecutor;

    @PostConstruct
    private void initCache() {
        Runnable task = () -> {
            PmsDevice pmsDevice = new PmsDevice();
            List<PmsDevice> allDevcies = pmsDeviceMapper.selectPmsDevicesByCondition(pmsDevice);
            if (!allDevcies.isEmpty()) {
                //先删除
                redisService.deleteObject(RedisKeyConstants.PARK_DEVICE);
                redisService.setCacheList(RedisKeyConstants.PARK_DEVICE, allDevcies);
            }
        };
        // 提交任务
        parkingModelTaskExecutor.execute(task);

    }

    @Override
    public Long addDevice(PmsDevice pmsDevice) {
        pmsDevice.setCreateBy(SecurityUtils.getUsername());
        pmsDevice.setCreateTime(new Date());
        pmsDevice.setDelFlag("0");
        pmsDevice.setTenantId(SecurityUtils.getTenantId());
        pmsDeviceMapper.insert(pmsDevice);
        return pmsDevice.getDeviceId();
    }

    @Override
    public PmsDevice getDevice(Long deviceId) {
        return pmsDeviceMapper.selectById(deviceId);
    }

    @Override
    public List<PmsDevice> listDevices(PmsDevice pmsDevice) {
        if (pmsDevice != null) {
            pmsDevice.setTenantId(SecurityUtils.getTenantId());
        }
        return pmsDeviceMapper.selectPmsDevicesByCondition(pmsDevice);
    }

    @Override
    public void updateDevice(PmsDevice pmsDevice) {
        pmsDevice.setUpdateBy(SecurityUtils.getUsername());
        pmsDevice.setUpdateTime(new Date());
        pmsDeviceMapper.update(pmsDevice);
    }

    @Override
    public void deleteDevice(Long deviceId) {
        pmsDeviceMapper.delete(deviceId);
    }

    @Override
    public List<PmsDevice> listDevices(List<Long> deviceIds) {
        return pmsDeviceMapper.selectByIdS(deviceIds);
    }
}