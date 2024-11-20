package com.sunjoy.parking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PmsLaneDevice extends BaseEntity {
    // 车场id
    private Long id;
    // 设备id
    private Long deviceId;
    // 通道id
    private Long laneId;
    // 车场id
    private Long parkId;
    // 通行方向: 0－出，1－入，2－双向
    private String direction;
    // 状态（0正常 1停用）
    private String status;
    // 删除标志（0代表存在 2代表删除）
    private String delFlag;

}