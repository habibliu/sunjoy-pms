package com.sunjoy.parkctrl.service.impl;

import com.sunjoy.common.core.utils.DateUtils;
import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.parkctrl.service.IBillingService;
import com.sunjoy.parking.entity.PmsParkPrice;
import com.sunjoy.parking.entity.PmsParkService;
import com.sunjoy.parking.enums.ParkingUintEnum;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parking.vo.BillingDependency;
import com.sunjoy.parking.vo.BillingResult;
import com.sunjoy.parking.vo.BillingSegment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/22
 */
@Slf4j
@Service
public class BillingService implements IBillingService {

    @Autowired
    private RedisService redisService;

    @Override
    public BillingResult calculate(BillingDependency dependency) {
        List<PmsParkService> parkServices = this.redisService.getCacheObject(RedisKeyConstants.PARK_SERVICE + dependency.getParkId());
        PmsParkService parkService = parkServices.stream().filter(item -> Objects.equals(item.getServiceId(), dependency.getServiceId())).findFirst().orElse(null);
        BillingResult billingResult = new BillingResult();
        BeanUtils.copyBeanProp(billingResult, dependency);
        Long minutesDuration = DateUtils.minutesBetween(dependency.getStartTime(), dependency.getEndTime());
        billingResult.setBillingDuration(minutesDuration);
        if (null != parkService) {
            //取出收费标准
            PmsParkPrice pmsParkPrice = this.redisService.getCacheObject(RedisKeyConstants.PARK_PRICE + parkService.getPriceId());
            //放进计费结果中，方便日后对帐
            billingResult.setParkPrice(pmsParkPrice);

            if (minutesDuration <= pmsParkPrice.getFreeDuration()) {
                log.info("收费标准免费停车{}分钟，当前车辆停车{}分钟，车辆:{}本次停车小于免费停车时间，无费用产生!", pmsParkPrice.getFreeDuration(), minutesDuration, dependency.getLicensePlate());
                billingResult.setBillingAmount(BigDecimal.valueOf(0));
                billingResult.setBillingDuration(minutesDuration);
                billingResult.setFreeAmount(BigDecimal.valueOf(0));
                billingResult.setRealAmount(BigDecimal.valueOf(0));
                billingResult.setRemark("免费时段停车");
            } else {

                //如果有分段，就先分段计费，并逐步扣减时长
                if (pmsParkPrice.getDetailList() != null && !pmsParkPrice.getDetailList().isEmpty()) {
                    // 计算跨越的自然天数
                    List<BillingSegment> resultList = calculateDetail(dependency.getStartTime(), dependency.getEndTime(), pmsParkPrice);
                    billingResult.setDetails(resultList);
                    //如果还有剩余时间，用收费标准主表的收费规则计算
                    Long totalDuration = resultList.stream()
                            .map(BillingSegment::getTimeDuration)
                            .reduce(0L, Long::sum);

                    Long leftTime = minutesDuration - totalDuration;
                    BigDecimal amount = compute(pmsParkPrice.getPrice(), pmsParkPrice.getPriceUnit(), pmsParkPrice.getPriceQuantity(), leftTime);
                    BigDecimal detailTotalFee = resultList.stream()
                            .map(BillingSegment::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal totalAmount = detailTotalFee.add(amount);

                    billingResult.setBillingAmount(totalAmount);
                    billingResult.setRealAmount(totalAmount);
                    billingResult.setFreeAmount(BigDecimal.valueOf(0));
                    billingResult.setRemark("分段计费");
                } else {//按收费标准主数据计算

                    BigDecimal amount = compute(pmsParkPrice.getPrice(), pmsParkPrice.getPriceUnit(), pmsParkPrice.getPriceQuantity(), minutesDuration);
                    billingResult.setRealAmount(amount);
                    billingResult.setBillingAmount(amount);
                    billingResult.setFreeAmount(BigDecimal.valueOf(0));
                }
            }

            //汇总所有费用
        }
        return billingResult;
    }

    private List<BillingSegment> calculateDetail(LocalDateTime startTime, LocalDateTime endTime, PmsParkPrice pmsParkPrice) {
        long daysBetween = ChronoUnit.DAYS.between(startTime, endTime);
        List<BillingSegment> detailedSegments = new ArrayList<>();


        pmsParkPrice.getDetailList().forEach(item -> {

            // 定义时间段 A（每天的 10:00 到 12:00）
            String[] starts = item.getTimeStart().split(":");
            String[] ends = item.getTimeEnd().split(":");
            LocalTime startA = LocalTime.of(Integer.parseInt(starts[0]), Integer.parseInt(starts[1]));
            LocalTime endA = LocalTime.of(Integer.parseInt(ends[0]), Integer.parseInt(ends[1]));
            List<Duration> slots = DateUtils.getMatchingTimeSlots(startTime, endTime, startA, endA);
            //分段计费
            for (int i = 0; i < slots.size(); i++) {

                BillingSegment billingSegment = new BillingSegment();
                billingSegment.setOrder(i);
                billingSegment.setTimeRange(item.getTimeStart() + "-" + item.getTimeEnd());
                billingSegment.setUnitPrice(item.getPrice() + "元/" + item.getPriceQuantity() + ParkingUintEnum.valueOf(item.getPriceUnit()).getDesc());
                BigDecimal amount = compute(item.getPrice(), item.getPriceUnit(), item.getPriceQuantity(), slots.get(i).toMinutes());
                billingSegment.setTimeDuration(slots.get(i).toMinutes());
                billingSegment.setAmount(amount);
                detailedSegments.add(billingSegment);
            }
        });


        return detailedSegments;
    }

    /**
     * 计算费用
     *
     * @param price           单价
     * @param unit            单位
     * @param priceQuantity   单位量
     * @param minutesDuration 停车时长，分钟
     * @return
     */
    private BigDecimal compute(BigDecimal price, String unit, Integer priceQuantity, Long minutesDuration) {

        BigDecimal unitQuantity = tranformToUnitQuantity(minutesDuration, unit);
        return price.multiply(unitQuantity).divide(BigDecimal.valueOf(priceQuantity), 2, RoundingMode.HALF_UP);

    }

    /**
     * 分钟数转换为收费标准的计量单位数量
     *
     * @param minutesDuration
     * @param unit
     * @return
     */
    private BigDecimal tranformToUnitQuantity(Long minutesDuration, String unit) {
        BigDecimal unitQuantity = BigDecimal.valueOf(0);
        switch (ParkingUintEnum.valueOf(unit)) {
            case TIMES:
                unitQuantity = BigDecimal.valueOf(1L);
                break;
            case HOUR:
                unitQuantity = BigDecimal.valueOf(minutesDuration / 60);
                break;
            case DAY:
                unitQuantity = BigDecimal.valueOf(minutesDuration / (60 * 24));
                break;
            case WEEK:
                unitQuantity = BigDecimal.valueOf(minutesDuration / (60 * 24 * 7));
                break;
            case MONTH:
                unitQuantity = BigDecimal.valueOf(minutesDuration / (60 * 24 * 30));
                unitQuantity = unitQuantity.setScale(0, BigDecimal.ROUND_HALF_UP);
                break;
            default:
                unitQuantity = BigDecimal.valueOf(minutesDuration);
        }

        return unitQuantity;
    }
}