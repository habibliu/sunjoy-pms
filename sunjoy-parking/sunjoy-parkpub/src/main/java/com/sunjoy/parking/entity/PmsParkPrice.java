package com.sunjoy.parking.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 车场收费价目表对象
 *
 * @author Habib
 * @date 2024/11/6
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PmsParkPrice extends BaseEntity {
    /**
     * 车辆id
     */
    private Long priceId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 经营单位id
     */
    private Long opuId;

    /**
     * 价格名称
     */
    private String priceName;

    /**
     * 是否免费:0-否,1-是
     */
    private String free;

    /**
     * 免费时长
     */
    private Integer freeDuration;

    /**
     * 是否统一收费:0-否,1-是
     */
    private String uniformPrice;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 计费单位:TIMES-次数,MIN--分钟,HOUR--小时,DAY--天,WEEK--周,MONTH--月
     */
    private String priceUnit;

    /**
     * 计费量
     */
    private Integer priceQuantity;

    /**
     * 最高收费
     */
    private BigDecimal maxFee;

    /**
     * 最高收费计费单位:TIMES-次数,DAY--天,WEEK--周,MONTH--月
     */
    private String maxUnit;

    /**
     * 最高收费计费量
     */
    private Integer maxQuantity;
    

    /**
     * 状态（０--未生效 1--已生效,2--停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;
    /**
     * 分段计费明细
     */
    private List<PmsParkPriceDetail> detailList;
}