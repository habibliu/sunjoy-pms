package com.sunjoy.parkmodel.pojo;

import com.sunjoy.parking.entity.PmsParkService;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/8
 */
@Data
@NoArgsConstructor
public class ParkServicePojo extends PmsParkService {
    private String priceName;
    private String free;
    private Integer freeDuration;
    private String uniformPrice;
    private BigDecimal price;
    private String priceUnit;
    private Integer priceQuantity;
    private BigDecimal maxFee;
    private String maxUnit;
    private Integer maxQuantity;

}