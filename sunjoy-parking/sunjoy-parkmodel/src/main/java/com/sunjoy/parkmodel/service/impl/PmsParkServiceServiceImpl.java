package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.core.enums.YesNoEnum;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsParkService;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parkmodel.mapper.PmsParkServiceMapper;
import com.sunjoy.parkmodel.service.IPmsParkServiceService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 车场服务的服务实现类
 *
 * @author Habib
 * @date 2024/11/8
 */
@Slf4j
@Service
public class PmsParkServiceServiceImpl implements IPmsParkServiceService {

    @Autowired
    private PmsParkServiceMapper pmsParkServiceMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ThreadPoolTaskExecutor parkingModelTaskExecutor;

    @PostConstruct
    private void initCache() {

        // 创建一个任务
        Runnable task = () -> {
            PmsParkService condition = new PmsParkService();
            condition.setDelFlag("0");
            List<PmsParkService> results = pmsParkServiceMapper.selectByConditions(condition);
            //按車場存放
            if (!results.isEmpty()) {
                Map<Long, List<PmsParkService>> groupedByParkId = results.stream()
                        .collect(Collectors.groupingBy(PmsParkService::getParkId));
                groupedByParkId.forEach((parkId, parkServices) -> {
                    this.redisService.setCacheObject(RedisKeyConstants.PARK_SERVICE + parkId, parkServices);
                });
            }

        };
        // 提交任务
        parkingModelTaskExecutor.execute(task);

    }

    @Override
    public List<PmsParkService> listParkServices(PmsParkService parkService) {
        //必须指定车场ID
        if (parkService.getParkId() != null) {
            parkService.setTenantId(SecurityUtils.getTenantId());
            return pmsParkServiceMapper.selectByConditions(parkService);
        } else {
            return new ArrayList<PmsParkService>();
        }
    }

    @Override
    public int addParkService(PmsParkService parkService) {
        parkService.setTenantId(SecurityUtils.getTenantId());
        parkService.setDelFlag("0");
        parkService.setStatus("0");
        parkService.setCreateBy(SecurityUtils.getUsername());
        parkService.setCreateTime(new Date());
        parkService.setUpdateBy(null);
        parkService.setUpdateBy(null);
        if (null == parkService.getExpiredAllowed()) {
            parkService.setExpiredAllowed(YesNoEnum.No.getCode());
        }
        if (null == parkService.getExpiredDuration()) {
            parkService.setExpiredDuration(0);
        }

        return pmsParkServiceMapper.insert(parkService);
    }

    @Override
    public void enableParkService(Long serviceId) {
        PmsParkService parkService = new PmsParkService();
        parkService.setServiceId(serviceId);
        parkService.setStatus("1");
        parkService.setDelFlag("0");
        parkService.setUpdateBy(SecurityUtils.getUsername());
        parkService.setUpdateTime(new Date());
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();

        // 设置时、分、秒、毫秒为0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 默认当天零晨启用，获取设置后的 Date 对象
        Date dateWithZeroTime = calendar.getTime();
        parkService.setStartDate(dateWithZeroTime);
        this.pmsParkServiceMapper.update(parkService);

    }
}