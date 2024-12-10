package com.sunjoy.parkctrl.service;

import com.sunjoy.system.api.domain.PmsParkPayment;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/25
 */
public interface IPmsParkPaymentService {

    void createPayment(PmsParkPayment parkPayment);

    void updatePayment(PmsParkPayment parkPayment);
}