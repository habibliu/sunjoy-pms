package com.sunjoy.park.client.service;

import com.sunjoy.park.client.entity.LaneDevice;
import com.sunjoy.park.client.repository.LaneDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/27
 */
@Service
public class LaneDeviceService {

    private final LaneDeviceRepository laneDeviceRepository;

    @Autowired
    public LaneDeviceService(LaneDeviceRepository laneDeviceRepository) {
        this.laneDeviceRepository = laneDeviceRepository;
    }

    public List<LaneDevice> findAll() {
        return laneDeviceRepository.findAll();
    }

}