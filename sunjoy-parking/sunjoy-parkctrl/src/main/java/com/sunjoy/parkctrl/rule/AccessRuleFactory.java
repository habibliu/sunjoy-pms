package com.sunjoy.parkctrl.rule;

import com.sunjoy.parking.enums.DirectionEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    /**
     * 车场通行规则
     */
    private static Map<Long, List<IAccessRule>> parkRules = new HashMap<Long, List<IAccessRule>>();

    private void register(Long parkId, IAccessRule rule) {
        if (parkRules.containsKey(parkId)) {
            parkRules.get(parkId).add(rule);
        } else {
            List<IAccessRule> parkRuleList = new ArrayList<IAccessRule>();
            parkRuleList.add(rule);
            parkRules.put(parkId, parkRuleList);
        }

    }

    /**
     * 根据车场获取相关管制规则
     *
     * @param parkId
     * @param veheicleAction
     * @return
     */
    public List<IAccessRule> getRules(Long parkId, DirectionEnum veheicleAction) {
        List<IAccessRule> ruleList = parkRules.get(parkId);

        return ruleList.stream().filter(rule -> rule.getAction() == veheicleAction).collect(Collectors.toList());
    }

}