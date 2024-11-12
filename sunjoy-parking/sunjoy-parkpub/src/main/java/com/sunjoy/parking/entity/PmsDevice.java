package com.sunjoy.parking.entity;

import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.*;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmsDevice extends BaseEntity {
    // 设备id
    private Long deviceId;
    // 设备名称
    private String deviceName;
    // 设备型号
    private String deviceModel;
    /**
     * 设备编号
     */
    private String deviceCode;
    /**
     * 租户ID
     */
    private Long tenantId;
    // 经营单位Id
    private Long opuId;
    // 功能：REL--放行，DIS--显示，VOI--语音，SEN--感应,VEO--摄像
    private String functions;
    // 供应商
    private String vendor;
    // 生产商
    private String producer;
    // 参数解释器
    private String paramsParse;
    // 状态（0正常 1停用）
    private String status;
    // 删除标志（0代表存在 2代表删除）
    private String delFlag;

}