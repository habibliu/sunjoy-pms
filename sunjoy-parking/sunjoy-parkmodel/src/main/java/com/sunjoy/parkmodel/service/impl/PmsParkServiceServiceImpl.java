package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.core.enums.YesNoEnum;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsParkService;
import com.sunjoy.parkmodel.mapper.PmsParkServiceMapper;
import com.sunjoy.parkmodel.pojo.ParkServicePojo;
import com.sunjoy.parkmodel.service.IPmsParkServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        return pmsParkServiceMapper.insertParkService(parkService);
    }
}