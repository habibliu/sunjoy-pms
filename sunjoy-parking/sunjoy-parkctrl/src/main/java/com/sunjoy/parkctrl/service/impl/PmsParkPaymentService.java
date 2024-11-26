package com.sunjoy.parkctrl.service.impl;

import com.sunjoy.parkctrl.mapper.PmsParkPaymentMapper;
import com.sunjoy.parkctrl.service.IPmsParkPaymentService;
import com.sunjoy.parking.entity.PmsParkPayment;
import com.sunjoy.parking.enums.ParkPaymentStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 支付凭证服务实现类
 *
 * @author Habib
 * @date 2024/11/25
 */
@Slf4j
@Service
public class PmsParkPaymentService implements IPmsParkPaymentService {
    @Autowired
    private PmsParkPaymentMapper parkPaymentMapper;

    @Override
    public void createPayment(PmsParkPayment parkPayment) {
        if (null == parkPayment.getStatus()) {
            parkPayment.setStatus(ParkPaymentStatusEnum.COMPLETED.getCode());
        }
        if (null == parkPayment.getCreateBy()) {
            parkPayment.setCreateBy("SYSTEM");
        }
        if (null == parkPayment.getCreateTime()) {
            parkPayment.setCreateTime(new Date());
        }
        parkPaymentMapper.insertPayment(parkPayment);
    }

    @Override
    public void updatePayment(PmsParkPayment parkPayment) {

    }
}