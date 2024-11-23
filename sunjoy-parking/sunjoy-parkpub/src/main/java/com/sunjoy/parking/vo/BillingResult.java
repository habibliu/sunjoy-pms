package com.sunjoy.parking.vo;

import com.sunjoy.common.core.utils.DateUtils;
import com.sunjoy.parking.entity.PmsParkPrice;
import com.sunjoy.parking.enums.ParkingUintEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private Long parkId;
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

    public String getBillingResultDescription() {
        StringBuilder calLog = new StringBuilder();
        PmsParkPrice price = this.getParkPrice();
        calLog.append("计费说明:").append("\n");
        calLog.append("车牌号码：").append(this.getLicensePlate()).append("\n");
        calLog.append("入场时间:").append(this.startTime).append("\t").append("出场时间：").append(this.endTime).append("\t");
        calLog.append("日期跨度:").append(DateUtils.daysBetween(this.startTime, this.endTime)).append("天\n");
        calLog.append("\t").append("总停车时长:").append(this.getBillingDuration()).append("分钟\n");
        calLog.append("\t").append("收费标准：").append(this.getParkPrice().getPriceName()).append("\n");
        calLog.append("\t").append("单价：").append(price.getPrice()).append("元/").append(price.getPriceQuantity()).append(ParkingUintEnum.valueOf(price.getPriceUnit()).getDesc()).append("\n");
        calLog.append("\t").append("是否分段收费：").append(price.getDetailList() == null || price.getDetailList().isEmpty() ? "否" : "是").append("\n");
        //分段收费
        if (!(this.getDetails() == null || this.getDetails().isEmpty())) {
            AtomicInteger i = new AtomicInteger(1);
            this.getDetails().forEach(item -> {
                calLog.append("\t\t").append("第").append(i.get()).append("段:");
                calLog.append(item.getTimeRange()).append(", 单价:").append(item.getUnitPrice()).append(",计费时长:").append(item.getTimeDuration()).append("分钟").append(", 小计:").append(item.getAmount()).append("元\n");
                i.getAndIncrement();
            });
            Long subTotalDuration = this.details.stream()
                    .map(BillingSegment::getTimeDuration)
                    .reduce(0L, Long::sum);
            BigDecimal subTotalAmount = this.getDetails().stream().map(BillingSegment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            calLog.append("\t").append("分段收费总时长：").append(subTotalDuration);
            calLog.append("，总费用：").append(subTotalAmount).append("\n");
            calLog.append("\t").append("其他时段按收费标准计费，时长：").append(this.billingDuration - subTotalDuration).append("分钟");
            calLog.append(", 费用: ").append(this.getBillingAmount().subtract(subTotalAmount)).append("元\n");
            calLog.append("\t").append("总费用：").append(this.getBillingAmount()).append("元\n");
        }

        return calLog.toString();
    }
}