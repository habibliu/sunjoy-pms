package com.sunjoy.parking.entity;

import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 车辆服务表
 *
 * @author Habib
 * @date 2024/11/6
 */


@Data
@NoArgsConstructor
public class PmsVehicleService extends BaseEntity {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 车辆服务id
     */
    private Long serviceId;

    /**
     * 服务名称
     */
    private String serviceName;

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
     * 车辆id
     */
    private Long vehicleId;


    /**
     * 车牌号码
     */
    private String licensePlate;

    /**
     * 车位号码,多个车位用,分隔
     */
    private String lotNos;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 车主姓名,从车辆档案带过来
     */
    private String ownerName;

    /**
     * 车主电话,从车辆档案带过来
     */
    private String ownerPhone;

    /**
     * 状态（０--未生效 1--已生效,2--停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 车场名称
     */
    private String parkName;


}