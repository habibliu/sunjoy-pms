package com.sunjoy.parkmodel.pojo;

import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.parking.entity.PmsPark;
import lombok.Data;

import java.util.List;

/**
 * 车场Pojo类
 * 可以增加非表中字段的属性，方便从前端传递额外数据
 *
 * @auth Habib
 * @date 2024/10/18
 */
@Data
public class ParkPojo extends PmsPark {
    /**
     * 通道列表
     */
    private List<LanePojo> laneList;
    //通道设备关系表
    private List<LaneDevicePojo> laneDeviceList;

    /**
     * 获取PmsPark对象
     *
     * @return
     */
    public PmsPark getPmsPark() {
        PmsPark pmsPark = new PmsPark();
        BeanUtils.copyBeanProp(pmsPark, this);
        return pmsPark;
    }
}