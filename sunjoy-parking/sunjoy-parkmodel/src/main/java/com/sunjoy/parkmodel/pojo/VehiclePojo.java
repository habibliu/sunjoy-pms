package com.sunjoy.parkmodel.pojo;

import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.parking.entity.PmsVehicle;
import com.sunjoy.parking.entity.PmsVehicleService;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    /**
     * 车辆收费标准
     */
    private List<PmsVehicleService> vehicleServiceList;

    /**
     * 返回车辆档案
     *
     * @return
     */
    public PmsVehicle getVehicle() {
        PmsVehicle vehicle = new PmsVehicle();
        BeanUtils.copyBeanProp(vehicle, this);
        return vehicle;
    }
}