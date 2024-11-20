package com.sunjoy.parkctrl.service;

import com.sunjoy.mqtt.domain.VehicleArrivedPayload;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/20
 */
public interface IVehicleArrivedHandler extends Runnable {

    void handle(VehicleArrivedPayload payload);
}