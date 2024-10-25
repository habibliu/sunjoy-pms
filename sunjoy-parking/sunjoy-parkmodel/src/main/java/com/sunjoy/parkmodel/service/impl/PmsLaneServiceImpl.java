package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.parkmodel.entity.PmsLane;
import com.sunjoy.parkmodel.mapper.PmsLaneMapper;
import com.sunjoy.parkmodel.service.IPmsLaneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */
@Slf4j
@Service
public class PmsLaneServiceImpl implements IPmsLaneService {

    @Autowired
    private PmsLaneMapper pmsLaneMapper;

    @Override
    public List<PmsLane> list(PmsLane pmsLane) {
        return pmsLaneMapper.selectPmsLanesByCondition(pmsLane);
    }

    @Override
    public List<PmsLane> listByLaneIds(List<Long> laneIds) {
        return pmsLaneMapper.selectPmsLanes(laneIds);
    }

    @Override
    public Long ceate(PmsLane pmsLane) {

        pmsLaneMapper.insertPmsLane(pmsLane);
        return pmsLane.getLaneId();
    }

    @Override
    public int update(PmsLane pmsLane) {
        return pmsLaneMapper.updatePmsLane(pmsLane);
    }

    @Override
    public int delete(List<Long> laneIds) {
        return pmsLaneMapper.deletePmsLanes(laneIds);
    }
}