package com.sunjoy.parking.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 每日计费结果
 *
 * @author Habib
 * @date 2024/11/23
 */
@Data
public class BillingPerdayResult {

    /**
     * 第N日
     */
    private Integer order;
    /**
     * 计费开始时间
     */
    private LocalDateTime startTime;
    /**
     * 计费结算时间
     */
    private LocalDateTime endTime;
    /**
     * 时长，分钟
     */
    private Long timeDuration;
    /**
     * 计算费用
     */
    private BigDecimal billingAmount;


    /**
     * 实际费用，如果费用高于当天的费用，要改用最高费用，后期还要考虑到扣减当天其他停车的费用
     */
    private BigDecimal realAmount;

    /**
     * 免除掉的费用=billingAmount-realAmount;
     */
    private BigDecimal freeAmount;
    /**
     * 备注
     */
    private String remark;
    /**
     * 分段计费明细
     */
    private List<BillingSegment> details = new ArrayList<>();

    public String getCalcalateDescription() {
        StringBuilder calLog = new StringBuilder();
        calLog.append("\t第").append(this.order).append("天计费详情：");
        calLog.append("\t\t").append("计费开时间:").append(this.getStartTime()).append("\t计费结束时间:").append(this.getEndTime()).append("\t计费时长:").append(this.getTimeDuration()).append("分钟\n");

        //分段收费
        if (!(this.getDetails() == null || this.getDetails().isEmpty())) {
            AtomicInteger i = new AtomicInteger(1);
            this.getDetails().forEach(item -> {
                calLog.append("\t\t").append("第").append(i.get()).append("段:");
                calLog.append(item.getTimeRange()).append(", 单价:").append(item.getUnitPrice()).append(",计费时长:").append(item.getTimeDuration()).append("分钟").append(", 小计:").append(item.getBillingAmount()).append("元\n");
                i.getAndIncrement();
            });
            Long subTotalDuration = this.details.stream()
                    .map(BillingSegment::getTimeDuration)
                    .reduce(0L, Long::sum);
            BigDecimal subTotalAmount = this.getDetails().stream().map(BillingSegment::getBillingAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            calLog.append("\t\t").append("分段收费总时长：").append(subTotalDuration);
            calLog.append("，总费用：").append(subTotalAmount).append("\n");
            calLog.append("\t\t").append("其他时段按收费标准计费，时长：").append(this.timeDuration - subTotalDuration).append("分钟");
            calLog.append(", 费用: ").append(this.getBillingAmount().subtract(subTotalAmount)).append("元\n");
            calLog.append("\t\t").append("总费用：").append(this.getBillingAmount()).append("元\n");
        }
        calLog.append("\t\t").append("计费金额:").append(this.getBillingAmount()).append("元，\t实际金额:").append(this.getRealAmount()).append("元,\t免掉费用:").append(this.getFreeAmount()).append("元\n");

        return calLog.toString();
    }
}