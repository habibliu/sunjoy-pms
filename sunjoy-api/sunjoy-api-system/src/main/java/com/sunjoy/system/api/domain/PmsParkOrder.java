package com.sunjoy.system.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 车场业务订单类
 *
 * @author Habib
 * @date 2024/11/25
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PmsParkOrder extends BaseEntity {
    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单类型:0-临停车收费，1--月租车收费，2--其他
     */
    private String orderType;

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
    private Long serviceId;

    /**
     * 收费标准名称
     */
    private String serviceName;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 计费单位:TIMES-次数,MIN--分钟,HOUR--小时,DAY--天,WEEK--周,MONTH--月
     */
    private String priceUnit;

    /**
     * 计费量
     */
    private Long priceQuantity;

    /**
     * 计费金额
     */
    private BigDecimal billingAmount;

    /**
     * 实收金额
     */
    private BigDecimal realAmount;

    /**
     * 扣减金额
     */
    private BigDecimal freeAmount;

    /**
     * 订单明细，就是计费结果
     */
    private String orderDetail;

    /**
     * 状态（０--未支付  1--支付中  ,2--已支付 ，9--失效）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 支付方式： ONLINE,BANK,CASH
     */
    private String paymentMethod;

    /**
     * 支付渠道,WECHAT,ALIPAY
     */
    private String paymentChannel;
}