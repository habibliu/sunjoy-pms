package com.sunjoy.parking.utils;

/**
 * 车辆管理所有redis的key都要在此处定义
 *
 * @author Habib
 * @date 2024/11/5
 */
public class RedisKeyConstants {
    /**
     * 车场规则缓存键
     */
    public static final String PARK_RULE = "parking:rule:list:";
    /**
     * 通道设备关系
     */
    public static final String PARK_LANE_DEVICE = "parking:lane-device:list:";

    //入场防重复处理的缓存，30秒后失效
    public static final String PARK_VEHICLE_ARRIVED = "parking:vehicle:arrived:";

    //出场防重复处理的缓存，30秒后失效
    public static final String PARK_CAR_EXIT = "parking:car:exit:";

    /**
     * 车场设备
     */
    public static final String PARK_DEVICE = "parking:device:list";

    public static final String PARK_LANE_REL = "parking:park-lane:list";
    /**
     * 车辆服务
     */
    public static final String PARK_VEHICLE_SERVICE = "parking:vehicle-service:list:";

    public static final String PARK_INFO = "parking:park:";
    /**
     * 车场服务
     */
    public static final String PARK_SERVICE = "parking:park-service:";
    /**
     * 场内
     */
    public static final String PARK_VEHICLE_COUNT_INSIDE = "parking:vehicle-inside:";
    /**
     * 场内车
     */
    public static final String PARK_VEHICLE_INSIDE_MAP = "parking:vehicle-inside-map:";

    public static final String PARK_PRICE = "parking:price:";
    /**
     * 当前车道上被识别到的车
     */
    public static final String PARK_VEHICLE_IN_LANE = "parking:vehicle-in-lane:";

    public static final String PARK_REGISTED_VEHICLE_SERVER_ORDER_WAITING_FOR_PAYING = "parking:vehicle:registed:service:order:paying:";
}