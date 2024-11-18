package com.sunjoy.parking.entity;

import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/4
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PmsVehicle extends BaseEntity {
    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 经营单位id
     */
    private Long opuId;

    /**
     * 车牌号码
     */
    private String licensePlate;

    /**
     * 车辆品牌
     */
    private String brand;

    /**
     * 车辆型号
     */
    private String model;

    /**
     * 车辆类型
     */
    private String vehicleType;

    /**
     * 车主姓名
     */
    private String ownerName;

    /**
     * 车主电话
     */
    private String ownerPhone;

    /**
     * 车主地址
     */
    private String ownerAddr;

    /**
     * 登记日期
     */
    private Date registDate;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
}