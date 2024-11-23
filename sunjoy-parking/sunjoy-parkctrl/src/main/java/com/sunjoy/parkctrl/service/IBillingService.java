package com.sunjoy.parkctrl.service;

import com.sunjoy.parking.vo.BillingDependency;
import com.sunjoy.parking.vo.BillingResult;

/**
 * 计费接口
 *
 * @author Habib
 * @date 2024/11/22
 */
public interface IBillingService {
    BillingResult calculate(BillingDependency dependency);
}