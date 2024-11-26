package com.sunjoy.parkctrl.service.impl;

import com.sunjoy.parkctrl.mapper.PmsParkOrderMapper;
import com.sunjoy.parkctrl.service.IPmsParkOrderService;
import com.sunjoy.parking.entity.PmsParkOrder;
import com.sunjoy.parking.enums.ParkOrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/25
 */
@Slf4j
@Service
public class PmsParkOrderService implements IPmsParkOrderService {
    @Autowired
    private PmsParkOrderMapper pmsParkOrderMapper;

    @Override
    public void createParkOrder(PmsParkOrder parkOrder) {
        if (null == parkOrder.getStatus()) {
            parkOrder.setStatus(ParkOrderStatusEnum.Unpaid.getCode());
        }
        this.pmsParkOrderMapper.insertParkOrder(parkOrder);
    }

    @Override
    public void updateParkOrder(PmsParkOrder parkOrder) {
        this.pmsParkOrderMapper.updateParkOrder(parkOrder);
    }

    @Override
    public PmsParkOrder pickParkOrder(Long orderId) {
        return pmsParkOrderMapper.selectParkOrderById(orderId);
    }
}