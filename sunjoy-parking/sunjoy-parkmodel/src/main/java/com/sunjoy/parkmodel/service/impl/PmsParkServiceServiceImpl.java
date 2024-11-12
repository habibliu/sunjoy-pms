package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.core.enums.YesNoEnum;
import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsParkService;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parkmodel.mapper.PmsParkServiceMapper;
import com.sunjoy.parkmodel.pojo.ParkServicePojo;
import com.sunjoy.parkmodel.service.IPmsParkServiceService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    @PostConstruct
    private void initCache() {
        PmsParkService condition = new PmsParkService();
        condition.setDelFlag("0");
        List<ParkServicePojo> results = pmsParkServiceMapper.selectByConditions(condition);
        results.forEach(item -> {
            this.redisService.setCacheObject(RedisKeyConstants.PARK_SERVICE + item.getServiceId(), item);
        });
    }

    @Override
    public List<ParkServicePojo> listParkServices(PmsParkService parkService) {
        parkService.setTenantId(SecurityUtils.getTenantId());
        return pmsParkServiceMapper.selectByConditions(parkService);
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