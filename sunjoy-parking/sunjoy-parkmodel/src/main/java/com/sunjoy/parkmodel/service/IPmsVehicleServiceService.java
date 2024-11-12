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
}