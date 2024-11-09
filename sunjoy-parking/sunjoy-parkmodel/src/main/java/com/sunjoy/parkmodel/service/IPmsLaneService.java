package com.sunjoy.parkmodel.service;


import com.sunjoy.parking.entity.PmsLane;
import com.sunjoy.parking.entity.PmsLaneDevice;

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
    Long create(PmsLane pmsLane);

    /**
     * 创建通道信息
     *
     * @param pmsLane   通道
     * @param parkId    车场ID，通道所绑定的车场
     * @param direction 出入方向，通道在这个这场的车辆通行方向
     * @return
     */
    Long create(PmsLane pmsLane, Long parkId, String direction);

    int update(PmsLane pmsLane);

    int delete(List<Long> laneIds);

    /**
     * 删除车场通道
     *
     * @param parkId
     * @param laneIds
     * @return
     */
    int deleteParkLane(Long parkId, List<Long> laneIds);

    int saveLaneDevice(List<PmsLaneDevice> laneDeviceList);

}