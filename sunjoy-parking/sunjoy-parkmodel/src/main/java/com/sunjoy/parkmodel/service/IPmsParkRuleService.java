package com.sunjoy.parkmodel.service;

import com.sunjoy.parking.entity.PmsParkRule;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车场规则管理服务类接口
 *
 * @author Habib
 * @date 2024/11/5
 */
@Service
public interface IPmsParkRuleService {
    /**
     * 根据条件查询车场通行规则
     *
     * @param parkRule
     * @return
     */
    List<PmsParkRule> getParkRules(PmsParkRule parkRule);

    /**
     * 创建车场通行规则
     *
     * @param pmsParkRule
     * @return
     */
    void create(PmsParkRule pmsParkRule);

    PmsParkRule getParkRule(Long ruleId);

    /**
     * 更新车场通行规则
     *
     * @param pmsParkRule
     */
    void update(PmsParkRule pmsParkRule);
}