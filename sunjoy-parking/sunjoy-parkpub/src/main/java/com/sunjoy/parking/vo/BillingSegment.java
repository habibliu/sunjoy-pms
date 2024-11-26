package com.sunjoy.parking.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 时段收费
 *
 * @author Habib
 * @date 2024/11/22
 */
@Data
public class BillingSegment {
    //序号，第X段
    private int order;
    /**
     * 时间范围,例如:12:00-18:00
     */
    private String timeRange;
    /**
     * 计价
     */
    private String unitPrice;
    /**
     * 计费开始时间
     */
    private LocalDateTime startTime;
    /**
     * 计费结束时间
     */
    private LocalDateTime endTime;
    /**
     * 时费时长
     */
    private Long timeDuration;
    /**
     * 计费金额
     */
    private BigDecimal billingAmount;
}