package com.sunjoy.parkmodel.pojo;

import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.parking.entity.PmsLane;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LanePojo extends PmsLane {
    /**
     * 车场编号
     */
    private Long parkId;
    /**
     * 通道的出入方向
     */
    private String direction;

    public PmsLane getPmsLane() {
        PmsLane pmsLane = new PmsLane();
        BeanUtils.copyBeanProp(pmsLane, this);
        return pmsLane;
    }

}