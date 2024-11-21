package com.sunjoy.parking.vo;

import com.sunjoy.parking.entity.PmsDevice;
import com.sunjoy.parking.entity.PmsPark;
import com.sunjoy.parking.entity.PmsParkLane;
import com.sunjoy.parking.entity.PmsVehicleService;
import com.sunjoy.parking.enums.NotificationMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 出入场信息
 *
 * @author Habib
 * @date 2024/11/4
 */
@Data
@NoArgsConstructor
public class VehiclePassage implements Serializable {
    //车辆入场唯一ID
    private String uuid;
    /**
     * 出入场方向
     */
    private String direction;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 车辆被识别到的时间,到时转化为出、入场时间
     */
    private LocalDateTime eventTime;
    /**
     * 车牌号码
     */
    private String licensePlate;
    /**
     * 通道ID
     */
    private Long laneId;
    /**
     * 车辆信息
     */
    //private PmsVehicle vehicle;
    /**
     * 车场信号
     */
    private PmsPark park;
    /**
     * 车场通道关系
     */
    private PmsParkLane parkLane;
    /**
     * 车辆服务
     */
    private PmsVehicleService vehicleService;

    /**
     * 通道安装的设备
     */
    private List<PmsDevice> laneDevices;


    /**
     * 出入场校验结信息,默认成功的消息
     */
    private String notifyMessage = "欢迎光临！";
    /**
     * 检验失败时提示的方式
     */
    private NotificationMethod[] notifyMethods;
    /**
     * 入场时间
     */
    private LocalDateTime timeEntry;

    /**
     * 出场时间
     */
    private LocalDateTime timeExit;

    /**
     * 场内停留时长
     */
    private Long stayDuration;

    /**
     * 计费时长
     */
    private Long chargeDuration;

    /**
     * 停车费用
     */
    private Float parkingFee;

    /**
     * 开闸放行时间
     */
    private LocalDateTime releaseTime;
    /**
     * 放行方式：0--系统正常自动开闸,1--人工异常开闸
     */
    private String releaseMode;

}