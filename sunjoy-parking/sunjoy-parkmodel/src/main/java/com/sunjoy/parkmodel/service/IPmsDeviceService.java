package com.sunjoy.parkmodel.service;


import com.sunjoy.parking.entity.PmsDevice;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */

public interface IPmsDeviceService {
    /**
     * 新增设备档案，反回设备档案的编号
     *
     * @param device
     * @return
     */
    Long addDevice(PmsDevice device);

    PmsDevice getDevice(Long deviceId);

    List<PmsDevice> listDevices(PmsDevice pmsDevice);

    void updateDevice(PmsDevice device);

    void deleteDevice(Long deviceId);

    /**
     * 根据
     *
     * @param deviceIds
     * @return
     */
    List<PmsDevice> listDevices(List<Long> deviceIds);
}