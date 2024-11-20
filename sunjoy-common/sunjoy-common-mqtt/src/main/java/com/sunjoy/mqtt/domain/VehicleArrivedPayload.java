package com.sunjoy.mqtt.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/19
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleArrivedPayload extends Payload {
    /**
     * 设备ID
     */
    private Long deviceId;


    /**
     * 车牌号码
     */
    private String licensePlate;

    /**
     * 1:有车，2:无车
     */
    private String hasVehicle;
    /**
     * 出入场方向: 0--出场，1--入场
     */
    private String direction;

    @JsonCreator
    public VehicleArrivedPayload(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("deviceId") Long deviceId,
            @JsonProperty("eventTime") LocalDateTime eventTime,
            @JsonProperty("licensePlate") String licensePlate,
            @JsonProperty("hasVehicle") String hasVehicle,
            @JsonProperty("data") Map<String, Object> data,
            @JsonProperty("version") String version) {
        super(uuid, eventTime, data, version);

        this.deviceId = deviceId;
        this.licensePlate = licensePlate;
        this.hasVehicle = hasVehicle;

    }
}