package com.sunjoy.parkmodel.pojo;

import com.sunjoy.parking.entity.PmsLaneDevice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaneDevicePojo extends PmsLaneDevice {
    private String laneName;
    private String deviceName;
    private String deviceModel;
    private String functions;

}