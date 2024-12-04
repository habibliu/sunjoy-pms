package com.sunjoy.park.client.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * 通道设备
 *
 * @author Habib
 * @date 2024/11/27
 */
@Data
@Entity
public class LaneDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 通道ID，来自于平台
     */
    private Long laneId;
    private String laneName;
    private Long deviceId;
    private String deviceName;
    private String deviceCode;
}