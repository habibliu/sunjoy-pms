package com.sunjoy.parkmodel.service;

import com.sunjoy.parkmodel.entity.PmsLane;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */
public interface IPmsLaneService {
    /**
     * @param pmsLane
     * @return
     */
    List<PmsLane> list(PmsLane pmsLane);

    /**
     * 根据通道ID批量查询
     *
     * @param ids
     * @return
     */
    List<PmsLane> listByLaneIds(List<Long> ids);

    /**
     * 创建通道信息
     *
     * @param pmsLane
     * @return 返回通道的主键ID
     */
    Long ceate(PmsLane pmsLane);

    int update(PmsLane pmsLane);

    int delete(List<Long> laneIds);

}