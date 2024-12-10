package com.sunjoy.mqtt.domain;

import lombok.Data;

import java.util.List;

/**
 * 设备指令负载
 *
 * @author Habib
 * @date 2024/11/25
 */
@Data
public class DeviceDirectivePayload extends Payload {
    /**
     * 设备动作列表
     */
    List<String> deviceCodes;
    /**
     * 消息
     */
    private String message;
}