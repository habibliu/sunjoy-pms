package com.sunjoy.parkmodel.service;

import com.sunjoy.parking.entity.PmsParkService;
import com.sunjoy.parkmodel.pojo.ParkServicePojo;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/8
 */
public interface IPmsParkServiceService {
    /**
     * 获取车场的服务
     *
     * @param parkService
     * @return
     */
    List<ParkServicePojo> listParkServices(PmsParkService parkService);

    /**
     * 新增车场服务
     *
     * @param parkService
     * @return
     */
    int addParkService(PmsParkService parkService);

    void enableParkService(Long serviceId);
}