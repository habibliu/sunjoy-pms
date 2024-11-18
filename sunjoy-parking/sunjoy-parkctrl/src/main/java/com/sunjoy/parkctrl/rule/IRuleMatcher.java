package com.sunjoy.parkctrl.rule;

import com.sunjoy.parking.vo.VehiclePassage;

/**
 * 具体规则匹配接口
 *
 * @author Habib
 * @date 2024/11/18
 */
public interface IRuleMatcher {
    /**
     * 根据规则匹配
     *
     * @param vehiclePassage
     * @param rule
     * @return
     */
    boolean match(VehiclePassage vehiclePassage, IAccessRule rule);
}