package com.sunjoy.parkmodel.controller;

import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.common.core.web.page.TableDataInfo;
import com.sunjoy.common.log.annotation.Log;
import com.sunjoy.common.log.enums.BusinessType;
import com.sunjoy.common.security.annotation.RequiresPermissions;
import com.sunjoy.parking.entity.PmsParkPrice;
import com.sunjoy.parking.entity.PmsParkPriceDetail;
import com.sunjoy.parkmodel.pojo.ParkPricePojo;
import com.sunjoy.parkmodel.service.IPmsParkPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车场收费标准控制类
 *
 * @author Habib
 * @date 2024/11/6
 */
@RestController
@RequestMapping("/price")
public class PmsParkPriceController extends BaseController {

    @Autowired
    private IPmsParkPriceService pmsParkPriceService;

    @RequiresPermissions("parking:price:list")
    @GetMapping("/list")
    public TableDataInfo list(PmsParkPrice config) {
        startPage();
        List<PmsParkPrice> list = pmsParkPriceService.getParkPriceList(config);
        return getDataTable(list);
    }

    @RequiresPermissions("parking:price:add")
    @Log(title = "收费标准设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ParkPricePojo parkPricePojo) {

        return toAjax(pmsParkPriceService.createParkPrice(parkPricePojo.getPmsParkPrice(), parkPricePojo.getPriceDetails()));
    }

    /**
     * 获取收费标准信息
     *
     * @param priceId
     * @return
     */
    @RequiresPermissions("parking:price:list")
    @GetMapping("/{priceId}")
    public AjaxResult getPriceMaster(@PathVariable(value = "priceId", required = true) Long priceId) {

        AjaxResult ajax = AjaxResult.success();
        PmsParkPrice parkPrice = this.pmsParkPriceService.getParkPrice(priceId);
        ajax.put(AjaxResult.DATA_TAG, parkPrice);

        return ajax;
    }

    /**
     * 获取收费标准信息明细列表信息
     *
     * @param priceId
     * @return
     */
    @RequiresPermissions("parking:price:list")
    @GetMapping("/detail/{priceId}")
    public AjaxResult getPriceDetail(@PathVariable(value = "priceId", required = true) Long priceId) {

        AjaxResult ajax = AjaxResult.success();

        List<PmsParkPriceDetail> detailList = this.pmsParkPriceService.getParkPriceDetailList(priceId);

        ajax.put(AjaxResult.DATA_TAG, detailList);

        return ajax;
    }

    @RequiresPermissions("parking:price:add")
    @Log(title = "收费标准设置-增加明细", businessType = BusinessType.INSERT)
    @PostMapping("/detail")
    public AjaxResult addDetail(@Validated @RequestBody PmsParkPriceDetail parkPriceDetail) {
        Long id = pmsParkPriceService.createParkPriceDetail(parkPriceDetail);
        return toAjax(1).put(AjaxResult.DATA_TAG, id);
    }

}