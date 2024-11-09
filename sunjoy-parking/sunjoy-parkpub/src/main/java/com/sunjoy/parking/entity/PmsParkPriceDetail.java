package com.sunjoy.parking.entity;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/7
 */

import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PmsParkPriceDetail extends BaseEntity {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 价格id
     */
    private Long priceId;

    /**
     * 时段开始
     */
    private String timeStart;

    /**
     * 时段结束
     */
    private String timeEnd;

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
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

}