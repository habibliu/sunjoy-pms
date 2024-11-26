package com.sunjoy.parkctrl.service;

import com.sunjoy.parking.entity.PmsParkTransaction;

/**
 * 车场出入场流水服务类
 *
 * @author Habib
 * @date 2024/11/20
 */
public interface IPmsParkTransactionService {
    void insertTransactionRecord(PmsParkTransaction parkTransaction);

    /**
     * 找未出场的入场记录
     *
     * @param parkId
     * @param licensePlate
     * @return
     */
    PmsParkTransaction findEntryRecordWithoutExit(Long parkId, String licensePlate);

    /**
     * 更新出入场流水表
     *
     * @param parkTransaction
     */
    void updateTransactionRecord(PmsParkTransaction parkTransaction);

    PmsParkTransaction pickupParkTransactionRecord(Long transId);
}