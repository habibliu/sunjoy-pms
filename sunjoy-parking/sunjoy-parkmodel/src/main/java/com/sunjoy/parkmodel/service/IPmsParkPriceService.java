package com.sunjoy.parkmodel.service;

import com.sunjoy.parking.entity.PmsParkPrice;
import com.sunjoy.parking.entity.PmsParkPriceDetail;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/6
 */
public interface IPmsParkPriceService {

    /**
     * 查询车场收费标准列表
     *
     * @param condition
     * @return
     */
    List<PmsParkPrice> getParkPriceList(PmsParkPrice condition);

    /**
     * 创建收费标准
     *
     * @param pmsParkPrice
     * @param detailList
     * @return 返回成功数量
     */
    int createParkPrice(PmsParkPrice pmsParkPrice, List<PmsParkPriceDetail> detailList);

    /**
     * 获取收费标准详情
     *
     * @param priceId
     * @return
     */
    PmsParkPrice getParkPrice(Long priceId);

    /**
     * 获取收费标准明细
     *
     * @param priceId
     * @return
     */
    List<PmsParkPriceDetail> getParkPriceDetailList(Long priceId);

    /**
     * 创建一条收费标准明细记录
     *
     * @param pmsParkPriceDetail
     */
    Long createParkPriceDetail(PmsParkPriceDetail pmsParkPriceDetail);

    /**
     * 更新车场收费标准
     *
     * @param pmsParkPrice
     * @return
     */
    int updateParkPrice(PmsParkPrice pmsParkPrice);
}