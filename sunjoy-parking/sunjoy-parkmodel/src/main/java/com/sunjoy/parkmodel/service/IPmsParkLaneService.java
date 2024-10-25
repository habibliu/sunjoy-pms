package com.sunjoy.parkmodel.service;

import com.sunjoy.parkmodel.entity.PmsParkLane;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */
public interface IPmsParkLaneService {
    public void createParkLaneRelation(PmsParkLane parkLane);

    public void deleteParkLaneRelations(List<Long> ids);

    /**
     * 获取车场的通道
     *
     * @param parkId
     * @return
     */
    public List<PmsParkLane> getParkLanes(Long parkId);

    /**
     * 根据车场ID和通道ID删除两者的关联关系
     *
     * @param parkId
     * @param laneId
     */
    public void deleteParkLaneRelations(Long parkId, Long laneId);

}