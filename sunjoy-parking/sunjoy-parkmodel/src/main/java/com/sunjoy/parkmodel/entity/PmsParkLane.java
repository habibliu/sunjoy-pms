package com.sunjoy.parkmodel.entity;

import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.*;

/**
 * 车场通道关系类
 *
 * @author Habib
 * @date 2024/10/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmsParkLane extends BaseEntity {
    // 自增主键
    private Long id;
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