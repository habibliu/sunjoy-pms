package com.sunjoy.parking.utils;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/5
 */
public class MqttTopics {
    /**
     * 车辆出现，车牌被捕捉
     */
    public static final String TOPIC_CAR_CAPTURED = "parking/car/captured";
    /**
     * 车辆入场
     */
    public static final String TOPIC_CAR_ARRIVED = "parking/car/arrived";
    /**
     * 车辆出场
     */
    public static final String TOPIC_CAR_DEPARTED = "parking/car/departed";
    /**
     * 车辆放行-出场
     */
    public static final String TOPIC_VEHICLE_RELEASE = "parking/vehicle/release/exit";
}