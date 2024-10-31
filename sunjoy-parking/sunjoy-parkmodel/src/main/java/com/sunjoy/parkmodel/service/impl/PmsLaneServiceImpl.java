package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parkmodel.entity.PmsLane;
import com.sunjoy.parkmodel.entity.PmsLaneDevice;
import com.sunjoy.parkmodel.entity.PmsParkLane;
import com.sunjoy.parkmodel.mapper.PmsLaneMapper;
import com.sunjoy.parkmodel.service.IPmsLaneDeviceService;
import com.sunjoy.parkmodel.service.IPmsLaneService;
import com.sunjoy.parkmodel.service.IPmsParkLaneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 通道服务
 *
 * @author Habib
 * @date 2024/10/25
 */
@Slf4j
@Service
public class PmsLaneServiceImpl implements IPmsLaneService {

    @Autowired
    private PmsLaneMapper pmsLaneMapper;

    @Autowired
    private IPmsParkLaneService pmsParkLaneService;

    @Autowired
    private IPmsLaneDeviceService pmsLaneDeviceService;

    @Override
    public List<PmsLane> list(PmsLane pmsLane) {
        return pmsLaneMapper.selectPmsLanesByCondition(pmsLane);
    }

    @Override
    public List<PmsLane> listByLaneIds(List<Long> laneIds) {
        return pmsLaneMapper.selectPmsLanes(laneIds);
    }


    @Override
    public Long create(PmsLane pmsLane) {
        pmsLane.setCreateBy(SecurityUtils.getUsername());
        pmsLane.setCreateTime(new Date());
        pmsLaneMapper.insertPmsLane(pmsLane);

        return pmsLane.getLaneId();
    }

    @Override
    @Transactional
    public Long create(PmsLane pmsLane, Long parkId, String direction) {
        Long laneId = this.create(pmsLane);
        saveParkLaneRelation(parkId, pmsLane.getLaneId(), direction);
        return laneId;
    }

    @Override
    public int update(PmsLane pmsLane) {
        return pmsLaneMapper.updatePmsLane(pmsLane);
    }

    @Override
    public int delete(List<Long> laneIds) {

        return pmsLaneMapper.deletePmsLanes(laneIds);
    }

    @Override
    @Transactional
    public int deleteParkLane(Long parkId, List<Long> laneIds) {

        laneIds.forEach(laneId -> {
            //1、先删除通道设备关系
            this.pmsLaneDeviceService.deleteLaneDeviceByParkIdAndLaneId(parkId, laneId);
            //2、再删除车场通道关系
            this.pmsParkLaneService.deleteParkLaneRelations(parkId, laneId);
        });
        List<PmsParkLane> list = this.pmsParkLaneService.getParkLanes(laneIds);
        if (list != null && !list.isEmpty()) {
            List<Long> existsIds = list.stream().map(PmsParkLane::getLaneId).toList();
            laneIds.removeAll(existsIds);
        }
        //3、最后删除通道，但如果通道绑定了第2个车场，即不能删除通道
        if (laneIds.isEmpty()) {
            return 0;
        }
        return this.delete(laneIds);
    }

    @Override
    @Transactional
    public int saveLaneDevice(List<PmsLaneDevice> laneDeviceList) {
        laneDeviceList.forEach(item -> {
            this.pmsLaneDeviceService.addLaneDevice(item);
        });
        return laneDeviceList.size();
    }

    private void saveParkLaneRelation(Long parkId, Long laneId, String direction) {
        PmsParkLane parkLane = new PmsParkLane();
        parkLane.setParkId(parkId);
        parkLane.setLaneId(laneId);
        parkLane.setDirection(direction);
        parkLane.setCreateTime(new Date());
        parkLane.setCreateBy(SecurityUtils.getUsername());
        this.pmsParkLaneService.createParkLaneRelation(parkLane);
    }
}