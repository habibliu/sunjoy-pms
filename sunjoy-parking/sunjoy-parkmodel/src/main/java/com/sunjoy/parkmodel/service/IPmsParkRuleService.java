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
     * 根据条件查询
     *
     * @param parkRule
     * @return
     */
    List<PmsParkRule> getParkRules(PmsParkRule parkRule);
}