package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsParkPrice;
import com.sunjoy.parking.entity.PmsParkPriceDetail;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parkmodel.mapper.PmsParkPriceDetailMapper;
import com.sunjoy.parkmodel.mapper.PmsParkPriceMapper;
import com.sunjoy.parkmodel.service.IPmsParkPriceService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 收费标准服务类
 *
 * @author Habib
 * @date 2024/11/6
 */
@Service
public class PmsParkPriceServiceImpl implements IPmsParkPriceService {

    @Autowired
    private PmsParkPriceMapper pmsParkPriceMapper;
    @Autowired
    private PmsParkPriceDetailMapper pmsParkPriceDetailMapper;

    @Autowired
    private RedisService redisService;
    @Autowired
    private ThreadPoolTaskExecutor parkingModelTaskExecutor;


    @PostConstruct
    private void initCache() {
        // 创建一个任务
        Runnable task = () -> {
            PmsParkPrice condition = new PmsParkPrice();
            List<PmsParkPrice> results = getParkPriceList(condition);
            if (!results.isEmpty()) {
                results.forEach(price -> {
                    PmsParkPriceDetail detailCondition = new PmsParkPriceDetail();
                    detailCondition.setPrice(price.getPrice());
                    List<PmsParkPriceDetail> details = pmsParkPriceDetailMapper.selectByConditions(detailCondition);
                    if (details != null && !details.isEmpty()) {
                        price.setDetailList(details);
                    }
                    redisService.setCacheObject(RedisKeyConstants.PARK_PRICE + price.getPriceId(), price);
                });
            }
        };

        parkingModelTaskExecutor.execute(task);


    }

    @Override
    public List<PmsParkPrice> getParkPriceList(PmsParkPrice condition) {
        condition.setTenantId(SecurityUtils.getTenantId());
        return pmsParkPriceMapper.selectByCondition(condition);
    }

    @Override
    @Transactional
    public int createParkPrice(PmsParkPrice pmsParkPrice, List<PmsParkPriceDetail> detailList) {
        pmsParkPrice.setDelFlag("0");
        pmsParkPrice.setStatus("0");
        pmsParkPrice.setCreateBy(SecurityUtils.getUsername());
        pmsParkPrice.setCreateTime(new Date());
        pmsParkPrice.setTenantId(SecurityUtils.getTenantId());
        this.pmsParkPriceMapper.insert(pmsParkPrice);
        insertDetail(pmsParkPrice, detailList);
        return 1;
    }

    @Override
    public PmsParkPrice getParkPrice(Long priceId) {
        return pmsParkPriceMapper.selectById(priceId);
    }

    @Override
    public List<PmsParkPriceDetail> getParkPriceDetailList(Long priceId) {
        PmsParkPriceDetail condition = new PmsParkPriceDetail();
        condition.setPriceId(priceId);
        return this.pmsParkPriceDetailMapper.selectByConditions(condition);
    }

    @Override
    public Long createParkPriceDetail(PmsParkPriceDetail pmsParkPriceDetail) {
        pmsParkPriceDetail.setDelFlag("0");
        pmsParkPriceDetail.setStatus("0");
        pmsParkPriceDetail.setCreateBy(SecurityUtils.getUsername());
        pmsParkPriceDetail.setCreateTime(new Date());
        this.pmsParkPriceDetailMapper.insert(pmsParkPriceDetail);
        return pmsParkPriceDetail.getId();
    }

    @Override
    public int updateParkPrice(PmsParkPrice pmsParkPrice) {
        pmsParkPrice.setUpdateBy(SecurityUtils.getUsername());
        pmsParkPrice.setUpdateTime(new Date());
        this.pmsParkPriceMapper.update(pmsParkPrice);
        return 1;
    }

    private void insertDetail(PmsParkPrice pmsParkPrice, List<PmsParkPriceDetail> detailList) {
        if (detailList != null && !detailList.isEmpty()) {
            for (PmsParkPriceDetail detail : detailList) {
                detail.setStatus("0");
                detail.setDelFlag("0");
                detail.setPriceId(pmsParkPrice.getPriceId());
                this.pmsParkPriceDetailMapper.insert(detail);
            }
        }
    }
}