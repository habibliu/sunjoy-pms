package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsParkLane;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parkmodel.mapper.PmsParkLaneMapper;
import com.sunjoy.parkmodel.service.IPmsParkLaneService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 车场通道关系服务类
 *
 * @author Habib
 * @date 2024/10/25
 */
@Service
public class PmsParkLaneServiceImpl implements IPmsParkLaneService {
    @Autowired
    private PmsParkLaneMapper pmsParkLaneMapper;
    @Autowired
    private RedisService redisService;

    @Autowired
    private ThreadPoolTaskExecutor parkingModelTaskExecutor;

    /**
     * 初始化所有车场通道到缓存中
     */
    @PostConstruct
    private void initCache() {
        Runnable task = () -> {
            List<PmsParkLane> parkLaneList = this.pmsParkLaneMapper.selectAll();
            if (!parkLaneList.isEmpty()) {
                this.redisService.deleteObject(RedisKeyConstants.PARK_LANE_REL);
                this.redisService.setCacheList(RedisKeyConstants.PARK_LANE_REL, parkLaneList);
            }
        };
        // 提交任务
        parkingModelTaskExecutor.execute(task);

    }

    @Override
    public void createParkLaneRelation(PmsParkLane parkLane) {
        parkLane.setCreateBy(SecurityUtils.getUsername());
        parkLane.setCreateTime(new Date());
        pmsParkLaneMapper.insertPmsParkLane(parkLane);
    }

    @Override
    public void deleteParkLaneRelations(List<Long> ids) {
        ids.forEach(id -> {
            this.pmsParkLaneMapper.deletePmsParkLane(id);
        });

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