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
    private int id;
    private int order;//序号，第X天
    private String timeRange;
    private String unitPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long timeDuration;
    private BigDecimal amount;
}