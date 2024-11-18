package com.sunjoy.parkctrl.pojo;

import com.sunjoy.parking.entity.PmsParkLane;
import com.sunjoy.parking.entity.PmsVehicleService;
import lombok.Data;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/18
 */
@Data
public class DetailParams {
    private List<PmsParkLane> laneList;
    private List<PmsVehicleService> serviceList;
    private List<String> dayList;
    private List<String> timeList;
}