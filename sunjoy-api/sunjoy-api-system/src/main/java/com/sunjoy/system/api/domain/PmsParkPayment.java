package com.sunjoy.system.api.domain;

import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/25
 */
@Data
public class PmsParkPayment extends BaseEntity {
    /**
     * 支付订单id
     */
    private Long paymentId;

    /**
     * 订单id
     */
    private Long orderId;

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
     * 车牌号码
     */
    private String licensePlate;

    /**
     * 服务id
     */
    private Long serviceId;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 支付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 支付方式:CASH-现金, WECHAT, ALIPAY
     */
    private String paymentMethods;

    /**
     * 支付渠道
     */
    private String paymentChannel;

    /**
     * 支付状态: PENDING, COMPLETED, FAILED, REFUNDED
     */
    private String paymentStatus;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 第三方支付平台交易id
     */
    private String transactionId;

    /**
     * 状态（０--未生效 1--已生效）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

}