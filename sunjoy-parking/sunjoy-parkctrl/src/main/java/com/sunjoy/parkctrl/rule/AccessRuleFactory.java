package com.sunjoy.parkctrl.rule;

import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.parkctrl.rule.impl.BaseAccessRuleImpl;
import com.sunjoy.parking.entity.PmsParkRule;
import com.sunjoy.parking.enums.DirectionEnum;
import com.sunjoy.parking.utils.RedisKeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 车场规则工厂类
 * 1、从数据库（内存）中取出与当前车场的通行规则
 * ２、造成成
 * 　３、表
 *
 * @author Habib
 * @date 2024/11/4
 */
@Service
public class AccessRuleFactory {

    @Autowired
    private RedisService redisService;


    /**
     * 根据车场获取相关管制规则
     *
     * @param parkId
     * @param passDirection 出入场方向
     * @return
     */
    public List<IAccessRule> getRules(Long parkId, DirectionEnum passDirection) {
        List<PmsParkRule> parkRuleList = redisService.getCacheList(RedisKeyConstants.PARK_RULE + parkId);
        List<PmsParkRule> matchRuleList = parkRuleList.stream().filter(rule -> rule.getDirection().equals(passDirection.getValue()) || rule.getDirection().equals(DirectionEnum.BOTH.getValue())).toList();

        List<IAccessRule> accessRules = new ArrayList<>();
        matchRuleList.forEach(item -> {
            IAccessRule rule = new BaseAccessRuleImpl(item);
            accessRules.add(rule);
        });
        //返回
        return accessRules;
    }

}