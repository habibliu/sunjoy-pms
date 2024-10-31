package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parkmodel.entity.PmsParkLane;
import com.sunjoy.parkmodel.mapper.PmsParkLaneMapper;
import com.sunjoy.parkmodel.service.IPmsParkLaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */
@Service
public class PmsParkLaneServiceImpl implements IPmsParkLaneService {
    @Autowired
    private PmsParkLaneMapper pmsParkLaneMapper;

    @Override
    public void createParkLaneRelation(PmsParkLane parkLane) {
        parkLane.setCreateBy(SecurityUtils.getUsername());
        parkLane.setCreateTime(new Date());
        pmsParkLaneMapper.insertPmsParkLane(parkLane);
    }

    @Override
    public void deleteParkLaneRelations(List<Long> ids) {

    }

    @Override
    public List<PmsParkLane> getParkLanes(Long parkId) {
        return pmsParkLaneMapper.selectParkLanesByParkId(parkId);
    }

    @Override
    public void deleteParkLaneRelations(Long parkId, Long laneId) {
        this.pmsParkLaneMapper.deleteParkLaneByParkIdAndLaneId(parkId, laneId);
    }

    @Override
    public List<PmsParkLane> getParkLanes(List<Long> laneIds) {
        return this.pmsParkLaneMapper.selectParkLanesByLaneIds(laneIds);
    }
}