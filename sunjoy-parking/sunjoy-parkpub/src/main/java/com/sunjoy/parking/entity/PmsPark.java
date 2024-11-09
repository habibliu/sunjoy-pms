package com.sunjoy.parking.entity;

import com.sunjoy.common.core.annotation.Excel;
import com.sunjoy.common.core.web.domain.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * <p>
 *
 * </p>
 *
 * @author Habib
 * @since 2024-10-18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@ToString(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PmsPark extends BaseEntity {
    /**
     * 主键
     */
    private Long parkId;
    /**
     * 车场名称
     */
    @Excel(name = "车场名称")
    @NotNull
    private String parkName;

    @Excel(name = "父级Id")
    private String parentId;

    /**
     * 车场类型：1-室内，2-室外
     */
    private String parkType;
    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 经营单位ID
     */
    private Long opuId;

    /**
     * 经营单位
     */
    private String opuName;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 区域
     */
    private String region;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 总车位数
     */
    private Integer totalLots;

    /**
     * 排序号
     */
    private Integer orderNum;


    private String status;
}