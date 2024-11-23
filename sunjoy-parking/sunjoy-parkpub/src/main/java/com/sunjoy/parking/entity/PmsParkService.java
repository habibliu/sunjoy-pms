package com.sunjoy.parking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车场服务实体类
 *
 * @author Habib
 * @date 2024/11/8
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
     * 启用日期
     */
    private Date startDate;

    /**
     * 停用日期
     */
    private Date endDate;

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
     * 是否临停车的默认收费标准
     */
    private String defaultUnregisted;
    
    /**
     * 状态（０--未生效 1--已生效,2--停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;
    private String priceName;
    private String free;
    private Integer freeDuration;
    private String uniformPrice;
    private BigDecimal price;
    private String priceUnit;
    private Integer priceQuantity;
    private BigDecimal maxFee;
    private String maxUnit;
    private Integer maxQuantity;

    public Integer getExpiredDuration() {
        return expiredDuration == null ? 0 : expiredDuration;
    }

}