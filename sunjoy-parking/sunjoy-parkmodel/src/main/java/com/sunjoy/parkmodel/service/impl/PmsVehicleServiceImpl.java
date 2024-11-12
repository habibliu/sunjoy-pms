package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsVehicle;
import com.sunjoy.parkmodel.mapper.PmsVehicleMapper;
import com.sunjoy.parkmodel.service.IPmsVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<PmsVehicle> getVehiclesByCondition(PmsVehicle vehicle) {
        if (null == SecurityUtils.getTenantId()) {
            vehicle.setTenantId(SecurityUtils.getTenantId());
        }
        return pmsVehicleMapper.select(vehicle);
    }

    @Override
    public void createVehicle(PmsVehicle vehicle) {

    }

    @Override
    public void updateVehicle(PmsVehicle vehicle) {

    }

    @Override
    public int deleteVehicle(PmsVehicle vehicle) {
        return 0;
    }
}