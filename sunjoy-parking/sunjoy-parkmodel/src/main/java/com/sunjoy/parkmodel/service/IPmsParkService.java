package com.sunjoy.parkmodel.service;


import com.sunjoy.parking.entity.PmsPark;
import com.sunjoy.parkmodel.pojo.LaneDevicePojo;
import com.sunjoy.parkmodel.pojo.LanePojo;
import com.sunjoy.parkmodel.pojo.ParkPojo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Habib
 * @since 2024-10-18
 */
public interface IPmsParkService {
    /**
     * @param park
     * @return
     */
    List<PmsPark> listParks(ParkPojo park);

    public int createPark(PmsPark park, List<LanePojo> lanes, List<LaneDevicePojo> devices);

    public int updatePark(PmsPark park);

    public int removeParks(List<Long> idList);

    /**
     * 根据主键0获取车场资料
     *
     * @param parkId 车场表主键
     * @return 返回PmsPark对象
     */
    public PmsPark getParkById(Long parkId);

}