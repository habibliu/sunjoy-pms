package com.sunjoy.parking.entity;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/25
 */

import com.sunjoy.common.core.annotation.Excel;
import com.sunjoy.common.core.web.domain.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PmsLane extends BaseEntity {
    // 车场id
    private Long laneId;
    // 通道名称
    @Excel(name = "通道名称")
    @NotNull
    private String laneName;
    /**
     * 租户ID
     */
    private Long tenantId;
    // 经营单位Id
    @Excel(name = "经营单位Id")
    @NotNull
    private Long opuId;
    // 收费后才放行
    @Excel(name = "收费后才放行")
    @NotNull
    private String rap;
    /**
     * 连通外围
     */
    private String linkOuter;

    private String status;
    /**
     * 删除标志
     */
    private String delFlag;
}