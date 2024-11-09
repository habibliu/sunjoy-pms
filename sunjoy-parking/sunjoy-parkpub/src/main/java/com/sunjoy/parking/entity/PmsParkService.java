package com.sunjoy.parking.entity;

import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车场服务实体类
 *
 * @author Habib
 * @date 2024/11/8
 */
@Data
@NoArgsConstructor
public class PmsParkService extends BaseEntity {
    /**
     * 车辆id
     */
    private Long serviceId;

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
     * 价格id
     */
    private Long priceId;

    /**
     * 过期是否允许出入场
     */
    private String expiredAllowed;

    /**
     * 过期时间内仍保留身份
     */
    private Integer expiredDuration;

    /**
     * 入场提示信息
     */
    private String entryMessage;

    /**
     * 出场提示信息
     */
    private String exitMessage;

    /**
     * 状态（０--未生效 1--已生效,2--停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

}