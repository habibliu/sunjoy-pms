package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parkmodel.entity.PmsLane;
import com.sunjoy.parkmodel.entity.PmsPark;
import com.sunjoy.parkmodel.entity.PmsParkLane;
import com.sunjoy.parkmodel.mapper.PmsParkMapper;
import com.sunjoy.parkmodel.pojo.LanePojo;
import com.sunjoy.parkmodel.pojo.ParkPojo;
import com.sunjoy.parkmodel.service.IPmsLaneService;
import com.sunjoy.parkmodel.service.IPmsParkLaneService;
import com.sunjoy.parkmodel.service.IPmsParkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Habib
 * @since 2024-10-18
 */
@Slf4j
@Service
public class PmsParkServiceImpl implements IPmsParkService {
    @Autowired
    private PmsParkMapper pmsParkMapper;
    @Autowired
    private IPmsLaneService pmsLaneService;
    @Autowired
    private IPmsParkLaneService pmsParkLaneService;

    @Override
    public List<PmsPark> listParks(ParkPojo park) {
        return pmsParkMapper.selectParkList(park);
    }

    @Override
    public int createPark(PmsPark park) {

        return pmsParkMapper.insertPark(park);
    }

    @Override
    @Transactional
    public int updatePark(PmsPark park, List<LanePojo> lanes) {
        //处理通道信息
        if (lanes != null && !lanes.isEmpty()) {
            //保存通道
            saveLanes(park, lanes);
        }
        return pmsParkMapper.updatePark(park);
    }

    @Override
    public int removeParks(List<Long> idList) {
        return 0;
    }

    @Override
    public PmsPark getParkById(Long parkId) {
        return pmsParkMapper.selectParkById(parkId);
    }

    /**
     * 保存车场通道
     *
     * @param park
     * @param lanes LanePojo对象集合
     */
    private void saveLanes(PmsPark park, List<LanePojo> lanes) {
        //查出车场有关的通道
        LanePojo condition = new LanePojo();
        condition.setParkId(park.getParkId());
        AtomicReference<List<PmsLane>> originalLanes = new AtomicReference<>(pmsLaneService.list(condition));
        lanes.forEach(lanePojo -> {
            PmsLane lane = lanePojo.getPmsLane();
            lane.setOpuId(park.getOpuId());
            //保存通道
            if (lane.getLaneId() == null) {
                Long laneId = this.pmsLaneService.ceate(lane);
                //保存车场通道关系
                saveParkLaneRelation(park.getParkId(), laneId, lanePojo.getDirection());
            } else {
                this.pmsLaneService.update(lane);
                //删除匹配的元素
                originalLanes.set(originalLanes.get().stream().filter(item -> !item.getLaneId().equals(lane.getLaneId())).collect(Collectors.toList()));
            }

        });
        if (!originalLanes.get().isEmpty()) {
            //删除车场通道关系
            originalLanes.get().forEach(item -> {
                this.pmsParkLaneService.deleteParkLaneRelations(park.getParkId(), item.getLaneId());
            });
            //删除通道信息，delete方法要处理：如果通道被另外一个车场使用，即不用删除
            this.pmsLaneService.delete(originalLanes.get().stream().map(PmsLane::getLaneId)
                    .collect(Collectors.toList()));

        }
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