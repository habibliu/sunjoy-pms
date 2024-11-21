package com.sunjoy.parkctrl.service.impl;

import com.sunjoy.parkctrl.mapper.PmsParkTransactionMapper;
import com.sunjoy.parkctrl.service.IPmsParkTransactionService;
import com.sunjoy.parking.entity.PmsParkTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 车辆出入场流水实现类
 *
 * @author Habib
 * @date 2024/11/20
 */
@Slf4j
@Service
public class PmsParkTransactionServiceImpl implements IPmsParkTransactionService {
    @Autowired
    private PmsParkTransactionMapper pmsParkTransactionMapper;

    @Override
    public void insertTransactionRecord(PmsParkTransaction parkTransaction) {
        parkTransaction.setCreateBy("SYSTEM");
        parkTransaction.setCreateTime(new Date());
        pmsParkTransactionMapper.insert(parkTransaction);
    }

    @Override
    public PmsParkTransaction findEntryRecordWithoutExit(Long parkId, String licensePlate) {
        PmsParkTransaction condition = new PmsParkTransaction();
        condition.setParkId(parkId);
        condition.setLicensePlate(licensePlate);
        condition.setStatus("1");
        List<PmsParkTransaction> entryRecords = pmsParkTransactionMapper.findByCondition(condition);
        Optional<PmsParkTransaction> maxEntryRecord = entryRecords.stream()
                .max(Comparator.comparing(PmsParkTransaction::getEntryTime));
        return maxEntryRecord.orElse(null);
    }

    @Override
    public void updateTransactionRecord(PmsParkTransaction parkTransaction) {
        pmsParkTransactionMapper.update(parkTransaction);
    }
}