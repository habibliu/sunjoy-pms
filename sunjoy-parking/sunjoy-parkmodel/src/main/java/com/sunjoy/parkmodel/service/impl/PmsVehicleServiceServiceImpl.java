package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsVehicleService;
import com.sunjoy.parking.enums.EntityStatusEnum;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parkmodel.mapper.PmsVehicleServiceMapper;
import com.sunjoy.parkmodel.service.IPmsVehicleServiceService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 车辆服务实现类
 *
 * @author Habib
 * @date 2024/11/6
 */
@Service
public class PmsVehicleServiceServiceImpl implements IPmsVehicleServiceService {
    @Autowired
    private PmsVehicleServiceMapper pmsVehicleServiceMapper;

    @Autowired
    private RedisService redisService;

    @PostConstruct
    private void initCache() {
        List<PmsVehicleService> allVehicleServices = getAllVehicleService();
        if (!allVehicleServices.isEmpty()) {
            this.redisService.deleteObject(RedisKeyConstants.PARK_VEHICLE_SERVICE);
            this.redisService.setCacheList(RedisKeyConstants.PARK_VEHICLE_SERVICE, allVehicleServices);
        }
    }

    @Override
    public List<PmsVehicleService> getAllVehicleService() {
        return pmsVehicleServiceMapper.selectAll();
    }

    @Override
    public List<PmsVehicleService> getVehicleServiceByVehicleIds(List<Long> vehicelIds) {
        return pmsVehicleServiceMapper.selectByVehicleIds(vehicelIds);
    }

    @Override
    public List<PmsVehicleService> getVehicleServiceByVehicleId(Long vehicleId) {
        return this.pmsVehicleServiceMapper.selectByVehicleId(vehicleId);
    }

    @Override
    public void insertVehicleService(PmsVehicleService vehicleService) {
        if (vehicleService.getDelFlag() == null) {
            vehicleService.setDelFlag("0");
        }
        if (vehicleService.getStatus() == null) {
            vehicleService.setStatus("0");
        }
        if (vehicleService.getCreateBy() == null) {
            vehicleService.setCreateBy(SecurityUtils.getUsername());
        }
        if (vehicleService.getCreateTime() == null) {
            vehicleService.setCreateTime(new Date());
        }

        this.pmsVehicleServiceMapper.insert(vehicleService);
    }

    @Override
    @Transactional
    public void updateVehicleService(PmsVehicleService vehicleService) {
        vehicleService.setUpdateBy(SecurityUtils.getUsername());
        vehicleService.setUpdateTime(new Date());
        //如果当前是要启用服务，就要保证当前车辆启用状态的服务只有唯一一条记录，
        // 如果有另外一条记录，即要将旧记录的状态自动置为停用，并且停用日期为当前启用服务启用日期
        if (vehicleService.getStatus().equals(EntityStatusEnum.ENABLED.getStatus())) {
            List<PmsVehicleService> existServices = this.pmsVehicleServiceMapper.selectByVehicleId(vehicleService.getVehicleId());
            List<PmsVehicleService> activeServices = existServices.stream()
                    .filter(service -> !Objects.equals(service.getId(), vehicleService.getId()) && EntityStatusEnum.ENABLED.getStatus().equals(service.getStatus()))
                    .toList();
            if (!activeServices.isEmpty()) {
                activeServices.forEach(existService -> {
                    existService.setStatus(EntityStatusEnum.DISABLED.getStatus());
                    existService.setEndDate(vehicleService.getStartDate());
                    existService.setUpdateBy(SecurityUtils.getUsername());
                    existService.setUpdateTime(new Date());
                    this.pmsVehicleServiceMapper.update(existService);
                });
            }

        }
        this.pmsVehicleServiceMapper.update(vehicleService);
    }
}