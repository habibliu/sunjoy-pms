package com.sunjoy.parkmodel.service;

import com.sunjoy.parkmodel.entity.PmsDevice;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */

public interface IPmsDeviceService {
    void addDevice(PmsDevice device);

    PmsDevice getDevice(Long deviceId);

    List<PmsDevice> listDevices(PmsDevice pmsDevice);

    void updateDevice(PmsDevice device);

    void deleteDevice(Long deviceId);
}