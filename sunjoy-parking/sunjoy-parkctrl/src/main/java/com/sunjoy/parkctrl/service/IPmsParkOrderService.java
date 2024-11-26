package com.sunjoy.parkctrl.service;

import com.sunjoy.parking.entity.PmsParkOrder;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/25
 */
public interface IPmsParkOrderService {

    void createParkOrder(PmsParkOrder parkOrder);

    void updateParkOrder(PmsParkOrder parkOrder);

    PmsParkOrder pickParkOrder(Long orderId);
}