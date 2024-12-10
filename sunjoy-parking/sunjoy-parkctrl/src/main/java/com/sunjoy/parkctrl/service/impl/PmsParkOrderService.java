package com.sunjoy.parkctrl.service.impl;

import com.sunjoy.common.core.exception.ServiceException;
import com.sunjoy.parkctrl.mapper.PmsParkOrderMapper;
import com.sunjoy.parkctrl.service.IPmsParkOrderService;
import com.sunjoy.parking.enums.ParkOrderStatusEnum;
import com.sunjoy.system.api.domain.PmsParkOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void createParkOrder(PmsParkOrder parkOrder) throws ServiceException {
        if (null == parkOrder.getStatus()) {
            parkOrder.setStatus(ParkOrderStatusEnum.Unpaid.getCode());
        }
        //todo 检查同一个车辆同一个服务有没有在有效日期存在交叉的订单，如果有，就抛出异常
        PmsParkOrder condition = new PmsParkOrder();
        condition.setTenantId(parkOrder.getTenantId());
        condition.setParkId(parkOrder.getParkId());
        condition.setVehicleId(parkOrder.getVehicleId());
        condition.setServiceId(parkOrder.getServiceId());
        List<PmsParkOrder> existParkOrders = this.pmsParkOrderMapper.selectParkOrder(condition);

        boolean match = existParkOrders.stream().anyMatch(order ->
                (order.getStartTime().isBefore(parkOrder.getEndTime()) &&
                 order.getEndTime().isAfter(parkOrder.getStartTime())));
        if (match) {
            throw new ServiceException("本订单的有效时段与其他订单的有效时段有交叉，系统不允许！");
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