package com.sunjoy.parkmodel.service;

import com.sunjoy.parking.entity.PmsVehicleService;

import java.util.List;

/**
 * 车辆服务类接口
 *
 * @author Habib
 * @date 2024/11/6
 */
public interface IPmsVehicleServiceService {
    /**
     * @return
     */
    List<PmsVehicleService> getAllVehicleService();

    List<PmsVehicleService> getVehicleServiceByVehicleIds(List<Long> vehicelIds);

    /**
     * 取具体某辆车的服务
     *
     * @param vehicleId
     * @return
     */
    List<PmsVehicleService> getVehicleServiceByVehicleId(Long vehicleId);

    void insertVehicleService(PmsVehicleService vehicleService);

    void updateVehicleService(PmsVehicleService vehicleService);

    /**
     * 删除车辆服务
     *
     * @param vehicleId
     * @param serviceId
     */
    void deleteVehicleService(Long vehicleId, Long serviceId);
}