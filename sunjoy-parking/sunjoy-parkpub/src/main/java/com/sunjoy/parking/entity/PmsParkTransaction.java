package com.sunjoy.parking.entity;

import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/20
 */
@Data
public class PmsParkTransaction extends BaseEntity {
    /**
     * 流水表id
     */
    private Long transId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 经营单位id
     */
    private Long opuId;

    /**
     * 车场id
     */
    private Long parkId;

    /**
     * 车场名称
     */
    private String parkName;

    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * 车牌号码
     */
    private String licensePlate;

    /**
     * 服务id
     */
    private Long entryServiceId;

    /**
     * 入场通道id
     */
    private Long entryLaneId;

    /**
     * 入场通道名称
     */
    private String entryLaneName;

    /**
     * 入场设备id
     */
    private Long entryDeviceId;

    /**
     * 入场时间
     */
    private LocalDateTime entryTime;

    /**
     * 入场开闸放行时间
     */
    private LocalDateTime entryRelTime;

    /**
     * 入场开闸方式:0-自动放行，1-软件放行，2-遥控放行
     */
    private String entryRelMode;

    /**
     * 车位号码
     */
    private String lotNo;

    /**
     * 出场通道id
     */
    private Long exitLaneId;

    /**
     * 出场通道名称
     */
    private String exitLaneName;

    /**
     * 出场设备id
     */
    private Long exitDeviceId;

    /**
     * 出场时间
     */
    private LocalDateTime exitTime;

    /**
     * 出场开闸放行时间
     */
    private LocalDateTime exitRelTime;

    /**
     * 出场开闸方式:0-自动放行，1-软件放行，2-遥控放行
     */
    private String exitRelMode;

    /**
     * 停车时长
     */
    private Long parkingDuration;

    /**
     * 状态（０--未出场 1--已出场）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
}