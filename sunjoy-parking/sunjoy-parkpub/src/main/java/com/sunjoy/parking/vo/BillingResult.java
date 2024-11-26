package com.sunjoy.parking.vo;

import com.sunjoy.common.core.utils.DateUtils;
import com.sunjoy.parking.entity.PmsParkPrice;
import com.sunjoy.parking.enums.ParkingUintEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 计费结果
 *
 * @author Habib
 * @date 2024/11/22
 */
@Data
public class BillingResult implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String billingId;
    /**
     * 停车事务ID
     */
    private Long transId;
    private Long tenantId;
    private Long opuId;
    private Long parkId;
    private String parkName;
    private String licensePlate;
    private Long serviceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long billingDuration;
    private Long freeDuration;
    private BigDecimal billingAmount;
    private BigDecimal freeAmount;
    private BigDecimal realAmount;
    private PmsParkPrice parkPrice;
    /**
     *
     */
    private String remark;
    /**
     * 分段计费明细
     */
    private List<BillingSegment> details = new ArrayList<>();
    /**
     * 每日的计费信息记录集
     */
    private List<BillingPerdayResult> perdayResultList = new ArrayList<>();

    public String getBillingResultDescription() {
        StringBuilder calLog = new StringBuilder();
        PmsParkPrice price = this.getParkPrice();
        calLog.append("\n计费说明:").append("\n");
        calLog.append("\t车牌号码：").append(this.getLicensePlate()).append("\n");
        calLog.append("\t入场时间:").append(this.startTime).append("\t").append("出场时间：").append(this.endTime).append("\t");
        calLog.append("日期跨度:").append(DateUtils.daysBetween(this.startTime, this.endTime)).append("天\n");
        calLog.append("\t").append("总停车时长:").append(this.getBillingDuration()).append("分钟\n");
        calLog.append("\t").append("收费标准：").append(this.getParkPrice().getPriceName()).append("\n");
        calLog.append("\t").append("单价：").append(price.getPrice()).append("元/").append(price.getPriceQuantity()).append(ParkingUintEnum.valueOf(price.getPriceUnit()).getDesc()).append("\n");
        //如果有设置最高收费标准
        if (null != price.getMaxFee() && null != price.getMaxQuantity() && null != price.getMaxUnit()) {
            calLog.append("\t").append("最高收费:").append(price.getMaxFee()).append("元/").append(price.getMaxQuantity()).append(ParkingUintEnum.valueOf(price.getMaxUnit()).getDesc()).append("\n");
        }
        calLog.append("\t").append("是否分段收费：").append(price.getDetailList() == null || price.getDetailList().isEmpty() ? "否" : "是").append("\n");
        this.getPerdayResultList().forEach(perDay -> {
            calLog.append(perDay.getCalcalateDescription());
        });
        calLog.append("\t").append("总计费金额:").append(this.getBillingAmount()).append("元\t实际收费:").append(this.getRealAmount()).append("元\t免掉金额:").append(this.getFreeAmount()).append("元\n");

        return calLog.toString();
    }
}