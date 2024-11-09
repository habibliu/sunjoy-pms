package com.sunjoy.mqtt.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.Map;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Playload implements Serializable {
    /**
     * 唯一识别编号
     */
    private String uuid;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 时间
     */
    private Timestamp timestamp;

    /**
     * 车牌号码
     */
    private String licensePlate;

    /**
     * true:有车，false:无车
     */
    private boolean hasVehicle;

    /**
     * 其他传递信息
     */
    private Map<String, Object> data;
    /**
     * 版本号
     */
    private String version = "1.0";
}