package com.sunjoy.parkmodel.pojo;

import com.sunjoy.common.core.utils.bean.BeanUtils;
import com.sunjoy.parking.entity.PmsParkPrice;
import com.sunjoy.parking.entity.PmsParkPriceDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PmsParkPrice的pojo类
 *
 * @author Habib
 * @date 2024/11/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ParkPricePojo extends PmsParkPrice {
    private static final long serialVersionUID = 1L;
    /**
     * 收费标准明细
     */
    private List<PmsParkPriceDetail> priceDetails;

    public ParkPricePojo(PmsParkPrice pmsParkPrice) {
        BeanUtils.copyBeanProp(this, pmsParkPrice);
    }

    public PmsParkPrice getPmsParkPrice() {
        PmsParkPrice pmsParkPrice = new PmsParkPrice();
        BeanUtils.copyBeanProp(pmsParkPrice, this);
        return pmsParkPrice;
    }
}