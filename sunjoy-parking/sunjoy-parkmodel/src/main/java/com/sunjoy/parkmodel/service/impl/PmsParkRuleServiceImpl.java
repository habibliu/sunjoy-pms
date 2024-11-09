package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsParkRule;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parkmodel.mapper.PmsParkRuleMapper;
import com.sunjoy.parkmodel.service.IPmsParkRuleService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 车场规则管理服务类
 *
 * @author Habib
 * @date 2024/11/5
 */
@Service
public class PmsParkRuleServiceImpl implements IPmsParkRuleService {
    @Autowired
    private PmsParkRuleMapper pmsParkRuleMapper;
    @Autowired
    private RedisService redisService;

    /**
     * 将规则缓存到redis中
     */
    @PostConstruct
    private void initCache() {
        PmsParkRule pmsParkRule = new PmsParkRule();
        pmsParkRule.setStatus("1");
        List<PmsParkRule> allRules = pmsParkRuleMapper.selectParkRulesByCriteria(pmsParkRule);
        if (!allRules.isEmpty()) {
            Map<Long, List<PmsParkRule>> groupedByParkId = allRules.stream()
                    .collect(Collectors.groupingBy(PmsParkRule::getParkId));
            groupedByParkId.forEach((parkId, rules) -> {
                redisService.deleteObject(RedisKeyConstants.PARK_RULE + ":" + parkId);
                redisService.setCacheList(RedisKeyConstants.PARK_RULE + ":" + parkId, rules);
            });
        }
    }

    @Override
    public List<PmsParkRule> getParkRules(PmsParkRule parkRule) {
        parkRule.setTenantId(SecurityUtils.getTenantId());
        return pmsParkRuleMapper.selectParkRulesByCriteria(parkRule);
    }
}