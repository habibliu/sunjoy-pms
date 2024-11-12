package com.sunjoy.parkmodel.pojo;

import com.sunjoy.parking.entity.PmsVehicle;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆档案信息pojo
 *
 * @author Habib
 * @date 2024/11/11
 */
@Data
@NoArgsConstructor
public class VehiclePojo extends PmsVehicle {
    /**
     * 关联车位
     */
    private String lots;
    /**
     * 关联的服务套餐
     */
    private String services;
}