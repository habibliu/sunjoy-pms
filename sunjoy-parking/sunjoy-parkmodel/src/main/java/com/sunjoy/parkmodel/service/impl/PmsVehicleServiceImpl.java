package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsVehicle;
import com.sunjoy.parking.entity.PmsVehicleService;
import com.sunjoy.parkmodel.mapper.PmsVehicleMapper;
import com.sunjoy.parkmodel.service.IPmsVehicleService;
import com.sunjoy.parkmodel.service.IPmsVehicleServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 车辆档案服务实现类
 *
 * @author Habib
 * @date 2024/11/11
 */
@Service
public class PmsVehicleServiceImpl implements IPmsVehicleService {

    @Autowired
    private PmsVehicleMapper pmsVehicleMapper;

    @Autowired
    private IPmsVehicleServiceService pmsVehicleServiceService;

    @Override
    public List<PmsVehicle> getVehiclesByCondition(PmsVehicle vehicle) {
        if (null == SecurityUtils.getTenantId()) {
            vehicle.setTenantId(SecurityUtils.getTenantId());
        }
        return pmsVehicleMapper.select(vehicle);
    }

    @Override
    @Transactional
    public void createVehicle(PmsVehicle vehicle, List<PmsVehicleService> vehicleServiceList) {
        vehicle.setTenantId(SecurityUtils.getTenantId());
        vehicle.setCreateBy(SecurityUtils.getUsername());
        vehicle.setCreateTime(new Date());
        vehicle.setDelFlag("0");
        vehicle.setStatus("0");
        pmsVehicleMapper.insert(vehicle);
        if (null != vehicleServiceList && !vehicleServiceList.isEmpty()) {
            vehicleServiceList.forEach(service -> {
                BeanUtils.copyBeanProp(service, vehicle);
                this.pmsVehicleServiceService.insertVehicleService(service);
            });
        }

    }

    @Override
    @Transactional
    public void updateVehicle(PmsVehicle vehicle, List<PmsVehicleService> vehicleServiceList) {
        vehicle.setUpdateBy(SecurityUtils.getUsername());
        vehicle.setUpdateTime(new Date());
        pmsVehicleMapper.update(vehicle);
        if (null != vehicleServiceList && !vehicleServiceList.isEmpty()) {
            vehicleServiceList.forEach(service -> {
                BeanUtils.copyBeanProp(service, vehicle);
                if (service.getId() != null) {
                    this.pmsVehicleServiceService.updateVehicleService(service);
                } else {
                    this.pmsVehicleServiceService.insertVehicleService(service);
                }
            });
        }
    }

    @Override
    public int deleteVehicle(PmsVehicle vehicle) {
        return 0;
    }

    @Override
    public PmsVehicle getVehicleById(Long vehicleId) {
        return pmsVehicleMapper.selectVehicleById(vehicleId);
    }
}