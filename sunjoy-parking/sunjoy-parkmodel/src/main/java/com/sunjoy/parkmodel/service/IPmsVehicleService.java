package com.sunjoy.parkmodel.service;

import com.sunjoy.parking.entity.PmsVehicle;
import com.sunjoy.parking.entity.PmsVehicleService;

import java.util.List;

/**
 * 车辆档案服务接口
 *
 * @author Habib
 * @date 2024/11/11
 */
public interface IPmsVehicleService {
    /**
     * 条件查询
     *
     * @param vehicle
     * @return
     */
    List<PmsVehicle> getVehiclesByCondition(PmsVehicle vehicle);

    void createVehicle(PmsVehicle vehicle, List<PmsVehicleService> vehicleServiceList);

    void updateVehicle(PmsVehicle vehicle, List<PmsVehicleService> vehicleServiceList);

    int deleteVehicle(PmsVehicle vehicle);

    PmsVehicle getVehicleById(Long vehicleId);
}