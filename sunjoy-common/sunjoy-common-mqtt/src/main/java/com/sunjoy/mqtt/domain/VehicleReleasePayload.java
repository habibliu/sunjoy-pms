package com.sunjoy.mqtt.domain;

import lombok.Data;

import java.util.List;

/**
 * 车辆放行负载
 *
 * @author Habib
 * @date 2024/11/25
 */
@Data
public class VehicleReleasePayload extends Payload {
    List<DeviceAction> deviceActions;
}

@Data
class DeviceAction {
    String action;
    String deviceCode;
    String message;
}