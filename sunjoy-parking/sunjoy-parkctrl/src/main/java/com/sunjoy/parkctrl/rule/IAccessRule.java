package com.sunjoy.parkctrl.rule;

import com.sunjoy.parking.entity.PmsParkRule;
import com.sunjoy.parking.enums.DirectionEnum;
import com.sunjoy.parking.enums.NotificationMethod;
import com.sunjoy.parking.vo.VehiclePassage;

/**
 * 入场规则接口
 *
 * @author Habib
 * @date 2024/11/4
 */
public interface IAccessRule {

    /**
     * 获取规则名称
     *
     * @return
     */
    String getRuleName();

    PmsParkRule getRule();

    /**
     * 入场规则判断
     *
     * @param vehiclePassage
     * @return
     */
    boolean isAllowed(VehiclePassage vehiclePassage);

    /**
     * 取规则约束进出场的方向类型
     *
     * @return
     */
    DirectionEnum getDirection();

    /**
     * 返回不允许通行后的规则说明
     *
     * @return
     */
    String getNotifyMessage();

    /**
     * 获取通知方式
     *
     * @return
     */
    NotificationMethod[] getNotifyMethods();

    /**
     * 是否禁止通行
     *
     * @return
     */
    boolean isForbidden();
}