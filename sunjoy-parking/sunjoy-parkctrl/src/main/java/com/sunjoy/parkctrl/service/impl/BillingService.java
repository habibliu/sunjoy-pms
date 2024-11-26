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
import com.sunjoy.parking.vo.BillingPerdayResult;
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
        //总时长，单位：分钟
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

                //跨天数
                //long daysBetween = ChronoUnit.DAYS.between(dependency.getStartTime(), dependency.getEndTime());
                long daysBetween = DateUtils.daysBetween(dependency.getStartTime(), dependency.getEndTime());
                //按天计算
                for (int i = 0; i < daysBetween; i++) {
                    LocalDateTime perDayStartTime = null;
                    LocalDateTime perDayEndTime = null;
                    if (i == 0) {
                        //第一天
                        perDayStartTime = dependency.getStartTime();
                        perDayEndTime = perDayStartTime.toLocalDate().atTime(LocalTime.MAX).plusNanos(1);
                    } else if (i + 1 == daysBetween) {
                        //最后一天
                        perDayStartTime = dependency.getStartTime().plusDays(i).toLocalDate().atTime(LocalTime.MIN);
                        perDayEndTime = dependency.getEndTime();
                    } else {
                        //中间的任何一天
                        perDayStartTime = dependency.getStartTime().plusDays(i).toLocalDate().atTime(LocalTime.MIN);
                        perDayEndTime = dependency.getStartTime().plusDays(i).toLocalDate().atTime(LocalTime.MAX).plusNanos(1);
                    }
                    //计算每日的费用
                    BillingPerdayResult perdayResult = calculatePerdayFree(perDayStartTime, perDayEndTime, pmsParkPrice);
                    //todo 如果是第一天，还要扣减当天其他停车费用,如果涉及跨天，还要算出属于当天的那部分费用
                    //设置第N天
                    perdayResult.setOrder(i + 1);
                    billingResult.getPerdayResultList().add(perdayResult);

                }
                //计算完毕，设计总费用//汇总所有费用
                BigDecimal totalBillingAmount = billingResult.getPerdayResultList().stream()
                        .map(BillingPerdayResult::getBillingAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal totalRealAmount = billingResult.getPerdayResultList().stream()
                        .map(BillingPerdayResult::getRealAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal totalFreeAmount = billingResult.getPerdayResultList().stream()
                        .map(BillingPerdayResult::getFreeAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                billingResult.setBillingAmount(totalBillingAmount);
                billingResult.setRealAmount(totalRealAmount);
                billingResult.setFreeAmount(totalFreeAmount);
            }

        }
        return billingResult;
    }

    /**
     * 计算每日的费用
     */
    private BillingPerdayResult calculatePerdayFree(LocalDateTime startTime, LocalDateTime endTime, PmsParkPrice pmsParkPrice) {
        BillingPerdayResult perdayResult = new BillingPerdayResult();
        perdayResult.setStartTime(startTime);
        perdayResult.setEndTime(endTime);
        Long minutesDuration = DateUtils.minutesBetween(startTime, endTime);
        perdayResult.setTimeDuration(minutesDuration);

        //如果有分段，就先分段计费，并逐步扣减时长
        StringBuilder remark = new StringBuilder();
        if (pmsParkPrice.getDetailList() != null && !pmsParkPrice.getDetailList().isEmpty()) {
            remark.append("分段收费\n");
            // 计算跨越的自然天数
            List<BillingSegment> resultList = calculateDetail(startTime, endTime, pmsParkPrice);
            perdayResult.setDetails(resultList);
            //如果还有剩余时间，用收费标准主表的收费规则计算
            Long totalDuration = resultList.stream()
                    .map(BillingSegment::getTimeDuration)
                    .reduce(0L, Long::sum);

            Long leftTime = minutesDuration - totalDuration;
            BigDecimal amount = compute(pmsParkPrice.getPrice(), pmsParkPrice.getPriceUnit(), pmsParkPrice.getPriceQuantity(), leftTime);
            BigDecimal detailTotalFee = resultList.stream()
                    .map(BillingSegment::getBillingAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalAmount = detailTotalFee.add(amount);

            perdayResult.setBillingAmount(totalAmount);
            if (totalAmount.compareTo(pmsParkPrice.getMaxFee()) > 0) {
                perdayResult.setRealAmount(pmsParkPrice.getMaxFee());
                perdayResult.setFreeAmount(totalAmount.subtract(pmsParkPrice.getMaxFee()));
                remark.append("超过当天最高收费");
            } else {
                perdayResult.setRealAmount(totalAmount);
                perdayResult.setFreeAmount(BigDecimal.valueOf(0));
            }
            perdayResult.setRemark(remark.toString());
        } else {//按收费标准主数据计算
            BigDecimal amount = compute(pmsParkPrice.getPrice(), pmsParkPrice.getPriceUnit(), pmsParkPrice.getPriceQuantity(), minutesDuration);
            perdayResult.setBillingAmount(amount);
            if (amount.compareTo(pmsParkPrice.getMaxFee()) > 0) {
                perdayResult.setRealAmount(pmsParkPrice.getMaxFee());
                perdayResult.setFreeAmount(amount.subtract(pmsParkPrice.getMaxFee()));
                remark.append("超过当天最高收费");
            } else {
                perdayResult.setRealAmount(amount);
                perdayResult.setFreeAmount(BigDecimal.valueOf(0));
            }
        }
        return perdayResult;
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
                billingSegment.setBillingAmount(amount);
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
                unitQuantity = BigDecimal.valueOf(minutesDuration).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_UP);
                break;
            case DAY:
                unitQuantity = BigDecimal.valueOf(minutesDuration).divide(BigDecimal.valueOf(60 * 24), 2, BigDecimal.ROUND_UP);
                ;
                break;
            case WEEK:
                unitQuantity = BigDecimal.valueOf(minutesDuration).divide(BigDecimal.valueOf(60 * 24 * 7), 2, BigDecimal.ROUND_UP);
                ;
                break;
            case MONTH:
                unitQuantity = BigDecimal.valueOf(minutesDuration).divide(BigDecimal.valueOf(60 * 24 * 30), 1, BigDecimal.ROUND_HALF_UP);
                break;
            default:
                unitQuantity = BigDecimal.valueOf(minutesDuration);
        }
        BigDecimal fractionalPart = unitQuantity.remainder(BigDecimal.ONE);
        if (fractionalPart.compareTo(new BigDecimal("0.98")) > 0) {
            //向上取整
            return unitQuantity.setScale(0, BigDecimal.ROUND_UP);
        } else if (fractionalPart.compareTo(new BigDecimal("0.02")) < 0) {
            //向下取整
            return unitQuantity.setScale(0, BigDecimal.ROUND_DOWN);
        } else {
            return unitQuantity;
        }

    }
}