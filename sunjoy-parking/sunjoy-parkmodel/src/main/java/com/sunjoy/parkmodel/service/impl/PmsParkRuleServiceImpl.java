package com.sunjoy.parkmodel.service.impl;

import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsParkRule;
import com.sunjoy.parking.enums.EntityStatusEnum;
import com.sunjoy.parking.utils.RedisKeyConstants;
import com.sunjoy.parkmodel.mapper.PmsParkRuleMapper;
import com.sunjoy.parkmodel.service.IPmsParkRuleService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    private void refreshParkRuleToCache(PmsParkRule pmsParkRule) {
        List<PmsParkRule> cachePmsParkRule = redisService.getCacheList(RedisKeyConstants.PARK_RULE + ":" + pmsParkRule.getParkId());
        //先从缓存中删除匹配的规则
        cachePmsParkRule = cachePmsParkRule.stream()
                .filter(rule -> rule.getRuleId() != pmsParkRule.getRuleId())
                .collect(Collectors.toList());
        //如果规则状态为启用，即从新加上
        if (pmsParkRule.getStatus().equals(EntityStatusEnum.ENABLED.getStatus())) {
            cachePmsParkRule.add(pmsParkRule);

        }
        Long parkId = pmsParkRule.getParkId();
        redisService.deleteObject(RedisKeyConstants.PARK_RULE + ":" + parkId);
        redisService.setCacheList(RedisKeyConstants.PARK_RULE + ":" + parkId, cachePmsParkRule);
    }

    @Override
    public List<PmsParkRule> getParkRules(PmsParkRule parkRule) {
        parkRule.setTenantId(SecurityUtils.getTenantId());
        return pmsParkRuleMapper.selectParkRulesByCriteria(parkRule);
    }

    @Override
    public void create(PmsParkRule parkRule) {
        parkRule.setTenantId(SecurityUtils.getTenantId());
        parkRule.setDelFlag("0");
        parkRule.setStatus("0");
        parkRule.setCreateBy(SecurityUtils.getUsername());
        parkRule.setCreateTime(new Date());
        this.pmsParkRuleMapper.insert(parkRule);
    }

    @Override
    public PmsParkRule getParkRule(Long ruleId) {
        return pmsParkRuleMapper.selectById(ruleId);
    }

    @Override
    public void update(PmsParkRule pmsParkRule) {
        pmsParkRule.setUpdateBy(SecurityUtils.getUsername());
        pmsParkRule.setUpdateTime(new Date());


        this.pmsParkRuleMapper.update(pmsParkRule);
        //更新车场缓存
        refreshParkRuleToCache(pmsParkRule);

    }
}