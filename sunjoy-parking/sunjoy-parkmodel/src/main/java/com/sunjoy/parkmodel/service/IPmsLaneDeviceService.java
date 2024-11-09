package com.sunjoy.parkmodel.service;


import com.sunjoy.parking.entity.PmsLaneDevice;

import java.util.List;

/**
 * 通道设备服务接口
 *
 * @author Habib
 * @date 2024/10/29
 */
public interface IPmsLaneDeviceService {

    int addLaneDevice(PmsLaneDevice laneDevice);

    PmsLaneDevice getLaneDeviceById(Long id);

    int updateLaneDevice(PmsLaneDevice laneDevice);

    int deleteLaneDevice(Long id);

    List<PmsLaneDevice> getAllLaneDevices();

    int deleteLaneDeviceByParkIdAndLaneId(Long parkId, Long laneId);

    List<PmsLaneDevice> getLaneDevicesByParkId(Long parkId);

    int deleteLaneDevices(List<Long> ids);
}